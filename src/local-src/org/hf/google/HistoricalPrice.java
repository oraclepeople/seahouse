package org.hf.google;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.xml.sax.SAXException;

import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;


public final class HistoricalPrice {
	
	private static final String GOOGLE_UK = "http://www.google.co.uk";

	/** How many month to go back in order to get the month move */
    static final int MONTHS_BACK = 2;

//	/** The date when the price of JRS peaks */
//	public static final String JRS_PEAK_DATE = "Jun 6, 2008";
    
	private static final String FROM_DATE = "Jun 06, 2007";
	
	static final DateTimeFormatter fmt = DateTimeFormat.forPattern("MMM dd, yyyy" ).withLocale(Locale.US);
	static final DateTimeFormatter gbDatFormat = DateTimeFormat.forPattern("dd/MM/yy");
	
	DecimalFormat numberFormat = new DecimalFormat("000,000.##");
	
	/**
	 * Get historical price on one day and highest price between two dates 
	 */
	public TickerPrice queryPriceInfo(String ticker) throws Exception{
		

		DateTime fromDate = fmt.parseDateTime(FROM_DATE);
		DateTime toDate = new DateTime().plusWeeks(2);
		boolean weekly = true;
		
		WebTable webTable = getPriceTable(ticker, fromDate, toDate, weekly);
		
		double  maxPrice = 0.00;
		double minPrice = 1000000.00;
		String maxDate = "";
		String minDate = "";
		
		// Find out the column containing close price info
		int lastColumn = -1;
		for(int i = 0; i < webTable.getColumnCount(); ++i){
			if ("Close".equals(webTable.getTableCell(0, i).getText().trim())){
				lastColumn = i;
			}
		}
		
		for ( int i = 1 ;  i < webTable.getRowCount(); ++i){
			
				double aPrice = numberFormat.parse(webTable.getTableCell(i, lastColumn)
						 .getText()).doubleValue();
				if(aPrice > maxPrice){
					maxPrice = aPrice;
					maxDate = webTable.getTableCell(i, 0).getText();
				}
				
				if(aPrice < minPrice){
					minPrice = aPrice;
					minDate = webTable.getTableCell(i, 0).getText();
				}
		}
		
		WebTable dailyTable = getPriceTable(ticker, new DateTime().minusMonths(MONTHS_BACK), 
				new DateTime().plusDays(2), false);

		double maxPriceInMonth = 0.00;
		String maxDateInMonth = "";
		
		double minPriceInMonth = -1;
		String minDateInMonth = "";
		
		for ( int i = 1 ; dailyTable != null &&  i < dailyTable.getRowCount(); ++i){
			
			double aPrice = numberFormat.parse(dailyTable.getTableCell(i, lastColumn).getText()).doubleValue();
			if(aPrice > maxPriceInMonth){
				maxPriceInMonth = aPrice;
				maxDateInMonth = dailyTable.getTableCell(i, 0).getText();
			}
			
			if(aPrice < minPriceInMonth || minPriceInMonth < 0 ){
				minPriceInMonth = aPrice;
				minDateInMonth = dailyTable.getTableCell(i, 0).getText();
			}
			
	}
	
		
		TickerPrice tickerPrice = new TickerPrice();
		tickerPrice.setTicker(ticker);
		
		tickerPrice.setMaxPrice(maxPrice);
		if(!"".equals(maxDate)){
		  tickerPrice.setMaxDate(gbDatFormat.print(fmt.parseDateTime(maxDate)));
		}
 
		tickerPrice.setTroughPrice(minPrice);
		if(!"".equals(minDate)){
			  tickerPrice.setTroughPriceDate(gbDatFormat.print(fmt.parseDateTime(minDate)));
		}

		tickerPrice.setMaxPriceInMonth(maxPriceInMonth);
		if(!"".equals(maxDateInMonth)){
		 tickerPrice.setMaxDateInMonth(gbDatFormat.print(fmt.parseDateTime(maxDateInMonth)));
		}
		
		tickerPrice.setMinPriceInMonth(minPriceInMonth);
		if(!"".equals(minDateInMonth)){
		 tickerPrice.setMinDateInMonth(gbDatFormat.print(fmt.parseDateTime(minDateInMonth)));
		}
		
		return tickerPrice;
		
	}

 
	private WebTable getPriceTable(String ticker, DateTime fromDate,
			DateTime toDate, boolean weekly) throws IOException, SAXException {
		String url= GOOGLE_UK + "/finance/historical?q="
				+ ticker
				+ "&startdate=" + fmt.print( fromDate)  
				+ "&enddate=" + fmt.print(toDate)
				+ "&histperiod=" + (weekly? "weekly" : "") 
				+ "&start=0&num=200";
		
		HttpUnitOptions.setScriptingEnabled(false);
		WebConversation webConversation = new WebConversation();
		
		WebResponse response = webConversation.getResponse(url);
		WebTable webTable = response.getTables()[2];
		return webTable;
	}

	/**
	 * @param ticker   e.g., LON:BRIC
	 * @param dateString in the format of Sep 20, 2006
	 */
	public double queryByTicker(String ticker, String dateString) throws Exception{
		
		String url= GOOGLE_UK + "/finance/historical?q="
				+ ticker
				+ "&startdate=" + dateString  
				+ "&enddate=" + dateString
				+ "&histperiod=weekly&start=0&num=500";
		
		HttpUnitOptions.setScriptingEnabled(false);
		WebConversation webConversation = new WebConversation();
		WebResponse response = webConversation.getResponse(url);
		WebTable webTable = response.getTableWithID("historical_price");
		
		double  price = 0.00;
		for ( int i = 0 ; webTable != null &&  i < webTable.getRowCount(); ++i){
			if (dateString.equals(webTable.getTableCell(i, 0).getText().trim())){
				String numberText = webTable.getTableCell(i, 1).getText();
//				System.out.println(numberText);
				price = numberFormat.parse(numberText).doubleValue();
			}
		}
		return price;
		
	}

	public static void main(String[] args) throws Exception {
		HistoricalPrice historicalPrice = new HistoricalPrice();
		historicalPrice.queryByTicker("LON:BRIC", "Jul 6, 2007");
	}

}
