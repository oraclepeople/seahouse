package org.hf.google;

import java.text.DecimalFormat;


public final class RatioRules {
	public enum Score { Excellent, Pass, Fail, Unknow }
	public enum Grade {Important, Bonus, Optional };
	public static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("###,###.##"); 

	private float price = 0.0f;
	private float dividendYield = 0.0f;
	private float pe = 0.0f;
	
	private float sale = 0.0f;
	private float salesGrowth;
	private float incomeBeforeTax;
	private float netIncome;
	private float netIncomeAvgGrowth;
	private float cash;
	private float currentAsset;
	private float totalAsset = 0.0f;
	private float shortDebt;
	private float longDebt;
	private float currentLiabilities;
	private float totalEquity;
	private float freeCashFlow = 0.0f;
	private float commonShares;
	private float averageShares;
	
	public void checkRatios (){
		checkPEG();
		checkEquityLiabilityRatio();
		checkCurrentRatio();
		checkCashRatio();
		checkFoolishFlowRatio();
		checkFCFPriceRatio();
		checkSalesPE();
		checkPriceSaleRatio();
		checkSaleAndGrowth();
		
		checkPriceBookRatio();
		checkCashTotalDebtRatio();
		checkNetCashPosition();

		checkFCFSaleRatio();
		checkPreTaxProfitMargin();
	}
	
	private void checkPreTaxProfitMargin() {
		
		Score s = Score.Unknow ;
		float preTaxMargin = incomeBeforeTax/sale;
		printScore ("PreTax Margin(Check industry level)", preTaxMargin,  s, Grade.Optional);
	}

	private void checkPEG () {
		
		Score s = Score.Unknow ;
		float peg = pe/ ((netIncomeAvgGrowth + dividendYield) * 100);
		if (peg > 0.0f && peg <= 0.5f) s =  Score.Excellent;
		if (peg > 0.5f && peg <= 1.0f) s =  Score.Pass;
		if (peg > 1.0) s =  Score.Fail;
		
		printScore("PEG (< 1)", peg, s, Grade.Important);
	}



	private void checkEquityLiabilityRatio() {
		
		float equityLiabilityRatio = totalEquity/(totalAsset - totalEquity);
		Score s = Score.Unknow ;
		
		if (equityLiabilityRatio >= 5 ){
			s = Score.Excellent;
		}

		if (equityLiabilityRatio < 5 && equityLiabilityRatio >= 1.6){
			s = Score.Pass;
		}
				
		if (equityLiabilityRatio < 1.6){
			s = Score.Fail;
		}
		
		printScore("Equity/Liability ( > 1.6)", equityLiabilityRatio,  s, Grade.Important);
		
	}
	
	private void checkSalesPE (){
		Score s = Score.Unknow ;
		if (sale > 1000f && pe > 0 & pe < 40){
			s = Score.Pass;
		}
		printScore("Sale above 1 billion and PE < 40", sale, s, Grade.Bonus);
	}
	
	private void checkSaleAndGrowth () {
		Score s = Score.Fail ;
		
		if (sale > 4000f && salesGrowth > 0.04) {
			s = Score.Pass;
		}
		printScore("Sale above 4 billion and Growth > 0.04 ", salesGrowth, s, Grade.Bonus);
	}
	
	
	private void checkFCFPriceRatio (){
		Score s = Score.Fail ;
		
		float fcfYield = freeCashFlow/commonShares/price;
		if(fcfYield >= 0.1 ) {
			s = Score.Pass;
		}
		if (fcfYield >= 0.2) {
			s = Score.Excellent;
		}
		printScore("FreeCashFlow-Price Ratio ( > 0.1)", fcfYield, s, Grade.Important);
	}
	
	private void checkNetCashPosition (){
		Score s = Score.Unknow ;
		
		float cashPriceRatio = (cash - longDebt )/commonShares/price;
		if ( cashPriceRatio >= 0.3 ){
			s = Score.Excellent;
		}
		
		printScore("Net Cash Position (> 0.3)", cashPriceRatio, s, Grade.Bonus);
	}
	
	private void checkCashTotalDebtRatio () {
		Score s = Score.Fail;
		
		float cashDebt;
		if((shortDebt + longDebt) == 0.0f){
		    cashDebt = 999999.00f;
		} else {
		   cashDebt = cash / (shortDebt + longDebt);
		}
		
		if ( cashDebt > 1.5 ){
			s = Score.Pass;
		}
		printScore ("Cash-TotalDebt Ratio (> 1.5)", cashDebt, s, Grade.Bonus);
	}
	
	private void checkFCFSaleRatio (){
       Score s = Score.Fail;
		
		float fcfSaleRatio = freeCashFlow / sale;
		if ( fcfSaleRatio > 0.1 ){
			s = Score.Pass;
		}
		printScore ("FreeCashFlow-Sale Ratio (> 0.1)", fcfSaleRatio, s, Grade.Bonus);
	}
	
	/**
	 *      (Current Assets - Cash*) 
Flow Ratio = -------------------------------- 
            (Current Liabilities - ST Debt**)
	 * 
	 * We go in search of Flow Ratios that run lower than 1.25, ideally below 1.0. 
	 * If they get below 1.0, it means that the business is
	 */
	
	private void checkFoolishFlowRatio (){
		float ratio = (currentAsset - cash) / (currentLiabilities - shortDebt);
		Score s = Score.Unknow ;
		if ( ratio < 1.0) 	s = Score.Excellent;
		if( ratio >= 1.0 && ratio <= 1.25f)  s = Score.Pass;
		if (ratio > 1.25f)  s = Score.Fail;
		printScore("Foolish Flow Ratio (< 1.25)", ratio, s, Grade.Important);
	}
	
	private void checkPriceBookRatio () {
		Score s = Score.Fail;
		
		float priceBookRatio = price/(totalEquity/commonShares) ;
		if ( priceBookRatio < 5 && netIncome/averageShares > 0.15) {
			s = Score.Pass;
		}
		
		printScore("Price-Book Ratio (< 5)", priceBookRatio, s, Grade.Bonus);
	}
	
	private void checkPriceSaleRatio () {
		Score s = Score.Fail;
		float priceSaleRatio = price/(sale/averageShares);
		if ( priceSaleRatio <= 3 && netIncome/sale > 0.10) {
			s = Score.Pass;
		}
		printScore("Price/Sale(<3) & NI/sale(>0.10)", priceSaleRatio, s, Grade.Bonus);
	}
	
	private void checkCurrentRatio () {
		Score s = Score.Fail;
		float currentRatio = currentAsset/currentLiabilities;
		if (currentRatio >= 2.0) s = Score.Pass;
		printScore("current Ratio (> 2)", currentRatio, s, Grade.Important);
	}
	
	private void checkCashRatio () {
		Score s = Score.Fail;
		float cashRatio = cash/currentLiabilities;
		if (cashRatio >= 1.0) s = Score.Pass;
		printScore("Cash Ratio (> 1)", cashRatio, s, Grade.Important);
	}
	
	private void printScore (String title, float ratio, Score s, Grade g){
		String strFormat = "%1$-50s%2$-15s%3$-20s%4$-20s\n";
		Object[] obj = new Object[] {
				" Checking " + title ,
				NUMBER_FORMAT.format(ratio),
				"Result : " + s,
				"Grade : " + g

		};
		System.out.format(strFormat, obj);
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public void setPe(float pe) {
		this.pe = pe;
	}

	public void setSale(float sale) {
		this.sale = sale;
	}

	public void setSalesGrowth(float salesGrowth) {
		this.salesGrowth = salesGrowth;
	}

	public void setNetIncome(float netIncome) {
		this.netIncome = netIncome;
	}

	public void setNetIncomeAvgGrowth(float netIncomeAvgGrowth) {
		this.netIncomeAvgGrowth = netIncomeAvgGrowth;
	}

	public void setCash(float cash) {
		this.cash = cash;
	}

	public void setCurrentAsset(float currentAsset) {
		this.currentAsset = currentAsset;
	}

	public void setTotalAsset(float totalAsset) {
		this.totalAsset = totalAsset;
	}

	public void setShortDebt(float shortDebt) {
		this.shortDebt = shortDebt;
	}

	public void setLongDebt(float longDebt) {
		this.longDebt = longDebt;
	}

	public void setCurrentLiabilities(float currentLiabilities) {
		this.currentLiabilities = currentLiabilities;
	}

	public void setTotalEquity(float totalEquity) {
		this.totalEquity = totalEquity;
	}

	public void setFreeCashFlow(float freeCashFlow) {
		this.freeCashFlow = freeCashFlow;
	}

	public void setCommonShares(float commonShares) {
		this.commonShares = commonShares;
	}

	public void setAverageShares(float averageShares) {
		this.averageShares = averageShares;
	}

	public void setDividendYield(float dividendYield) {
		this.dividendYield = dividendYield;
	}

	public void setIncomeBeforeTax(float incomeBeforeTax) {
		this.incomeBeforeTax = incomeBeforeTax;
	}

	
}
