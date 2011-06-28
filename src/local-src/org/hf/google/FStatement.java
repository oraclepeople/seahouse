package org.hf.google;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.hf.google.DateValue.Period;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.google.common.collect.Lists;

public class FStatement {

	public enum OPERATOR  {add, minus, multiple, divide}

	public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd");

	float price = 0.0f;
	float yield = 0.0f;
	float pe = 0.0f;


	LinkedHashMap<KeyClass, List<DateValue>> statsMap = new LinkedHashMap<KeyClass, List<DateValue>>();

	private void loadData(String ticker) throws Exception {
		WebClient webClient = new WebClient();
		webClient.setCssEnabled(false);
		webClient.setJavaScriptEnabled(false);

		//"http://finance.google.com/finance/info?q=NASDAQ:MSFT"
		HtmlPage summaryPage = webClient.getPage("http://www.google.co.uk/finance?q=" + ticker);

		for (HtmlElement htmlElement : summaryPage.getElementsByTagName("span")) {
			if(Pattern.matches("ref_[0-9]+_l", htmlElement.getId())) {
				price = Float.parseFloat(htmlElement.getFirstChild().getNodeValue());
			}
		}

		yield = fetchMultiples(summaryPage, "Div/yield");
		pe = fetchMultiples(summaryPage, "P/E");


		HtmlPage fsPage = webClient.getPage("http://www.google.co.uk/finance?q=" + ticker + "&fstype=ii");
		DomNodeList<HtmlElement> tables = fsPage.getElementsByTagName("table");

		for (HtmlElement htmlElement : tables) {
			HtmlTable t = (HtmlTable) htmlElement;

			List<DateTime> dates = new ArrayList<DateTime>();
			Period p = Period.Quarter;

			for(HtmlTableRow row:  t.getHeader().getRows()) {
				for (HtmlTableCell cell:  row.getCells()) {
					if (cell.asText().contains("20") ){
						String str = "20" +StringUtils.substringAfter( cell.asText(), "20").trim();
						dates.add(DATETIME_FORMAT.parseDateTime(str));
					} else{
						dates.add(new DateTime());
					}
				}
			}

			if (dates.get(dates.size() -1).plusMonths(4).isBefore(dates.get(dates.size() - 2))){
				p = Period.Annual;
			}

			String item = "";
			for(HtmlTableBody body:  t.getBodies()) {
				for (HtmlTableRow row: body.getRows()) {
					int i = 0;
					List<DateValue> list = new ArrayList<DateValue>();
					for (HtmlTableCell cell:  row.getCells()) {
						if ( i == 0) {
							item = cell.asText().trim();
						} else {
							list.add (new DateValue(dates.get(i), cell.asText(), p));	
						}
						++i;
					}
					statsMap.put(new KeyClass(item, p), list);
				}
			} //end of table body	
		}


	}

	private float fetchMultiples(HtmlPage summaryPage, String yieldString) {
		boolean found = false;
		for (DomNode domNode : summaryPage.getElementById("snap-data").getChildNodes()) {
			for (DomNode c : domNode.getChildNodes()) {
				if(found && c.getFirstChild() != null ) {
					String tmp =   c.getFirstChild().getNodeValue();
					return parseString(tmp);
				}
				if ( c.getFirstChild() != null &&  yieldString.equals(c.getFirstChild().getNodeValue())){
					found = true;
				}
			}
		}
		return 0.0f;
	}

	private float parseString(String yieldStr) {
		float f = 0.0f;

		String str  =  yieldStr; 
		if (yieldStr.contains("/")) {
		  str = StringUtils.substringAfter(yieldStr, "/");
		}
		
		if(str.endsWith("*")){
			str = StringUtils.remove(yieldStr, "*");
		}
		
		try {
		 f = Float.parseFloat(str);
		} catch (NumberFormatException e){
			f = 0.0f;
		}
		return f;
	}

	private StatsBO calGrowthRatio(KeyClass key){
		List<DateValue> values = statsMap.get(key);

		List<Float> growth = Lists.newArrayList();
		List<Float> raw = Lists.newArrayList();

		for (int i = 0; i < values.size() - 1 ; ++i){
			float rate = values.get(i).getValue()/values.get(i + 1).getValue() -1;
			growth.add(rate);
		}

		for (DateValue dateValue : values) {
			raw.add(dateValue.getValue());
		}

//		new StatsBO(key.getName(), raw, false).prettyPrint();
		StatsBO statsBO = new StatsBO(key.getName() + "%", growth, true);
//		statsBO.prettyPrint();

		return statsBO;
	}


	private StatsBO calRatio(KeyClass numerator,  KeyClass denominator, OPERATOR op, String name){
		List<DateValue> numeratorList = statsMap.get(numerator);
		List<DateValue> denominatorList = statsMap.get(denominator);

		List<Float> results = Lists.newArrayList();

		int i = 0;
		for (DateValue num : numeratorList){
			float rate = 0.0f;
			switch (op) {
			case divide: 	 rate = num.getValue()/denominatorList.get(i).getValue(); break;
			case add:  	 rate = num.getValue() + denominatorList.get(i).getValue(); break;
			case multiple:  	 rate = num.getValue() * denominatorList.get(i).getValue(); break;
			case minus: rate = num.getValue() - denominatorList.get(i).getValue();
			}

			results.add(rate);
			++i;
		}

		StatsBO stats = new StatsBO (name, results, op.equals(OPERATOR.divide));
		stats.prettyPrint();

		if ( !op.equals(OPERATOR.divide)) {
			//ok. try to be a bit clever here. calculates difference
			List<Float> ratios = Lists.newArrayList();

			for ( int j = 0 ; j < results.size() - 1 ; ++j) {
				ratios.add( results.get(j)/results.get(j + 1) - 1);
			}
			new StatsBO(name + "%",ratios, true).prettyPrint();
		}
		return stats;
	}	

	 
	
	private void calPCashFlow (){
		List<DateValue> commonShares = statsMap.get(new KeyClass("Total Common Shares Outstanding", Period.Annual));
		List<DateValue> averageShares = statsMap.get(new KeyClass("Diluted Weighted Average Shares", Period.Annual));

		List<DateValue> cfoList = statsMap.get(new KeyClass("Cash from Operating Activities", Period.Annual));
		List<DateValue> capExList = statsMap.get(new KeyClass("Capital Expenditures", Period.Annual));

		List<DateValue> cash = statsMap.get(new KeyClass("Cash and Short Term Investments", Period.Annual));
		List<DateValue> bookValues = statsMap.get(new KeyClass("Total Equity", Period.Annual));
		List<DateValue> currentAssets = statsMap.get(new KeyClass("Total Current Assets", Period.Annual));
		List<DateValue> totalAssets = statsMap.get(new KeyClass("Total Assets", Period.Annual));
		List<DateValue> shortDebts = statsMap.get(new KeyClass("Notes Payable/Short Term Debt", Period.Annual));
		List<DateValue> longDebts = statsMap.get(new KeyClass("Total Long Term Debt", Period.Annual));
		List<DateValue> currentLiabilites = statsMap.get(new KeyClass("Total Current Liabilities", Period.Annual));
  
		List<DateValue> totalRevenue = statsMap.get(new KeyClass("Total Revenue", Period.Annual));
		StatsBO saleGrowthRatio = calGrowthRatio(new KeyClass("Total Revenue", Period.Annual));
		
		List<DateValue> preTaxIncome = statsMap.get(new KeyClass("Income Before Tax", Period.Annual));
		List<DateValue> netIncome = statsMap.get(new KeyClass("Net Income", Period.Annual));
		StatsBO niGrowthRatio = calGrowthRatio(new KeyClass("Net Income", Period.Annual));
		float pegLatest = pe/(niGrowthRatio.getValues().get(0) * 100 + yield);
		float pegAvg = pe/(niGrowthRatio.averageValue()*100 + yield);

		new StatsBO("Price, PE, Yield",  Lists.newArrayList(price, pe, yield), false).prettyPrint();
		new StatsBO("PEG latest, PEG average " + niGrowthRatio.getValues().size(),  Lists.newArrayList(pegLatest, pegAvg), false).prettyPrint();

		RatioRules r = new RatioRules();
        r.setPrice(price);
        r.setPe(pe);
        r.setDividendYield(yield/100);
        r.setSale(totalRevenue.get(0).getValue());
        r.setSalesGrowth(saleGrowthRatio.getValues().get(0));
        r.setIncomeBeforeTax(preTaxIncome.get(0).getValue());
        r.setNetIncome(netIncome.get(0).getValue());
        r.setNetIncomeAvgGrowth(niGrowthRatio.averageValue());
        r.setCash(cash.get(0).getValue());
        r.setCurrentAsset(currentAssets.get(0).getValue());
        r.setTotalAsset(totalAssets.get(0).getValue());
        r.setShortDebt(shortDebts.get(0).getValue());
        r.setLongDebt(longDebts.get(0).getValue());
        r.setCurrentLiabilities(currentLiabilites.get(0).getValue());
        r.setTotalEquity(bookValues.get(0).getValue());
        r.setFreeCashFlow(cfoList.get(0).getValue() + capExList.get(0).getValue());
        r.setAverageShares(averageShares.get(0).getValue());
        r.setCommonShares(commonShares.get(0).getValue());
        r.checkRatios();
	}


	public  void calculateMarketRatio( String ticker) throws Exception {
		
		System.out.println("");
		System.out.println("-------------------------------------------------------------");
		System.out.println("Analysis " + ticker + " multiples ");
		System.out.println("-------------------------------------------------------------");
		
		loadData(ticker);
		calGrowthRatio(new KeyClass("Total Revenue", Period.Annual));
//		calGrowthRatio(new KeyClass("Gross Profit", Period.Annual));
//		calGrowthRatio(new KeyClass("Total Operating Expense", Period.Annual));
//		calRatio(new KeyClass("Gross Profit", Period.Annual), new KeyClass("Total Revenue", Period.Annual),OPERATOR.divide, "Gross Profit Magin");

		calGrowthRatio(new KeyClass("Operating Income", Period.Annual)).prettyPrint();
		calRatio(new KeyClass("Operating Income", Period.Annual), new KeyClass("Total Revenue", Period.Annual),	OPERATOR.divide, "EBIT Magin");
		//		calGrowthRatio(new KeyClass("Income Before Tax", Period.Annual));
		//		calGrowthRatio(new KeyClass("Income After Tax", Period.Annual));
		calRatio(new KeyClass("Net Income", Period.Annual), new KeyClass("Total Revenue", Period.Annual), OPERATOR.divide, "Net Profit Magin");
		calGrowthRatio(new KeyClass("Net Income", Period.Annual)).prettyPrint();

//		calRatio(new KeyClass("Cash and Short Term Investments", Period.Annual), new KeyClass("Total Current Liabilities", Period.Annual),	OPERATOR.divide, "Cash Ratio");
		calRatio(new KeyClass("Total Assets", Period.Annual), new KeyClass("Total Liabilities", Period.Annual),	OPERATOR.divide, "Asset/Liabilities");
		
//		calGrowthRatio(new KeyClass("Total Liabilities", Period.Annual));
//		calGrowthRatio(new KeyClass("Cash from Operating Activities", Period.Annual));
		calRatio(new KeyClass("Cash from Operating Activities", Period.Annual), new KeyClass("Capital Expenditures", Period.Annual),OPERATOR.add,	"Free Cash Flow");

		calPCashFlow();
	}

	public static void main(String[] args) throws Exception {

		String[] tickers = new String[]{ "NASDAQ:MSFT", "NYSE:MCD",  "NASDAQ:AAPL", "NASDAQ:GOOG", "NYSE:KO", "NYSE:KFT" }; //"NASDAQ:ORCL", , "NASDAQ:NTAP", "NASDAQ:AMZN" , "NYSE:GE", "NYSE:WMT"}; , "NASDAQ:HMIN", "NASDAQ:ORCL"
		FStatement statement = new FStatement();
		
		for (String ticker : tickers) {
		  statement.calculateMarketRatio( ticker);
		}
	}
}
