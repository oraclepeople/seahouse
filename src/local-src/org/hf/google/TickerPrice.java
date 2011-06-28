package org.hf.google;

import java.util.Date;


public final class TickerPrice implements Comparable<TickerPrice> {

	private String name;
	private String ticker;
	private double currentPrice;
	private double historicalPrice;
	private Date   historicalDate;
	
	private double maxPrice;
	private String maxDate;
	
	private double maxPriceInMonth;
	private String maxDateInMonth;
	
	private double minPriceInMonth;
	private String minDateInMonth;
	
	private double troughPrice;
	private String troughPriceDate;

	

	public  String getTicker() {
		return ticker;
	}
	public  void setTicker(String ticker) {
		this.ticker = ticker;
	}
	public  double getCurrentPrice() {
		return currentPrice;
	}
	public  void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}
//	public  double getHistoricalPrice() {
//		return historicalPrice;
//	}
//	
//	public  void setHistoricalPrice(double historicalPrice) {
//		this.historicalPrice = historicalPrice;
//	}
	
	public  Date getHistoricalDate() {
		return historicalDate;
	}
	
	public  void setHistoricalDate(Date historicalDate) {
		this.historicalDate = historicalDate;
	}
	
	
	public int compareTo(TickerPrice o) {
		return new Double(getPeakRatio()).compareTo(new Double(o.getPeakRatio()));
	}

	public  int getRatio() {
		if(historicalPrice ==0 ){
			return 0;
		}
		return (int) (100 * currentPrice / historicalPrice ) ;
	}

	public  double getPeakRatioReverse() {
		if(maxPrice == 0 ){
			return 0;
		}
		return ( maxPrice /currentPrice ) ;
	}

	public  int getPeakRatio() {
		if(maxPrice == 0 ){
			return 0;
		}
		return (int) (100 * currentPrice / maxPrice ) ;
	}

	public  int getTroughRatio() {
		if(troughPrice == 0 ){
			return 0;
		}
		return (int) (100 * currentPrice / troughPrice ) ;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}
	public String getMaxDate() {
		return maxDate;
	}
	public void setMaxDate(String maxDate) {
		this.maxDate = maxDate;
	}
	public double getMaxPriceInMonth() {
		return maxPriceInMonth;
	}
	public void setMaxPriceInMonth(double maxPriceInMonth) {
		this.maxPriceInMonth = maxPriceInMonth;
	}
	public String getMaxDateInMonth() {
		return maxDateInMonth;
	}
	public void setMaxDateInMonth(String maxDateInMonth) {
		this.maxDateInMonth = maxDateInMonth;
	}
	
	public  String getMonthChange() {
		if(maxPriceInMonth == 0 ){
			return "0";
		}
		int max =  (int) (100 * currentPrice / maxPriceInMonth -100) ;
		int min =  (int) (100 * currentPrice / minPriceInMonth -100) ;
		return  max + "/" + min;
	}
	
	public double getTroughPrice() {
		return troughPrice;
	}
	
	public void setTroughPrice(double lowPrice) {
		this.troughPrice = lowPrice;
	}
	
	public String getTroughPriceDate() {
		return troughPriceDate;
	}
	
	public void setTroughPriceDate(String lowPriceDate) {
		this.troughPriceDate = lowPriceDate;
	}
	
	public final double getMinPriceInMonth() {
		return minPriceInMonth;
	}
	
	public final void setMinPriceInMonth(double minPriceInMonth) {
		this.minPriceInMonth = minPriceInMonth;
	}
	public final String getMinDateInMonth() {
		return minDateInMonth;
	}
	
	public final void setMinDateInMonth(String minDateInMonth) {
		this.minDateInMonth = minDateInMonth;
	}

	
}
