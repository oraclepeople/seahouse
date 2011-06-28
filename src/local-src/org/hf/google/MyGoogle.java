package org.hf.google;


import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gdata.client.finance.FinanceService;
import com.google.gdata.data.extensions.Money;
import com.google.gdata.data.finance.PortfolioEntry;
import com.google.gdata.data.finance.PortfolioFeed;
import com.google.gdata.data.finance.PositionData;
import com.google.gdata.data.finance.PositionEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.common.base.StringUtil;

public final class MyGoogle {
	
	public static String GOOGLE_FINANCE_SERVER = "http://finance.google.com";

	private FinanceService service;
	private List<TickerPrice> tickers = new ArrayList<TickerPrice>();

	/**
	 * @param service authenticated client connection to a Finance GData service
	 */
	public MyGoogle(FinanceService service){
		this.service = service;
	}

	public void queryPortfolioFeed(int portfolioID)    throws Exception {

		String feedUrl = GOOGLE_FINANCE_SERVER
				+ "/finance/feeds/default/portfolios?returns=false&positions=true";
		
		PortfolioFeed portfolioFeed = service.getFeed(new URL(feedUrl), PortfolioFeed.class);

		for (PortfolioEntry portfolioEntry : portfolioFeed.getEntries()) {
			if(portfolioEntry.getId().endsWith("portfolios/" + portfolioID)){
				analyzePortfolioEntry(portfolioEntry);
			}
		}

		Collections.sort(tickers);

		int nameLength = 40;
		
		//%1$-45s : the dash sign indicates left align
		String titleFormat = "\n\n%1$-" + nameLength +  "s " +
		        "%2$10s  %3$10s  %4$10s(%5$3s)  " +
		        "%6$10s  %7$10s  %8$10s  " +
		        "%9$10s  %10$10s %11$10s\n";
		System.out.format(String.format(titleFormat, new Object[]{
				"Name", 
				"Current", "Peak",  "PeakRatio", "2", 
				HistoricalPrice.MONTHS_BACK + "-MonthMove", "Trough", "TroughRatio",
				"PeakDate", "MonthPeak", "TroughDate"}));

		String format = "%1$-" + nameLength + "s " +
				"%2$10.2f  %3$10.2f  %4$10s(%5$3.2f)  " +
				"%6$10s  %7$10.2f  %8$10s  " +
				"%9$10s  %10$10s %11$10s\n";

		int blankLine = 0;
		for (TickerPrice entry : tickers) {
			
			String name = 	StringUtil.removeChars(entry.getName(), "%");
			if(name.length() >= nameLength){
				name = name.substring(0, nameLength);
			}
			
			Object ex[] = { 
					name, 
					entry.getCurrentPrice(),  entry.getMaxPrice(),	   entry.getPeakRatio(),  entry.getPeakRatioReverse(),
					entry.getMonthChange(),   entry.getTroughPrice(),  entry.getTroughRatio(), 
					entry.getMaxDate(), entry.getMaxDateInMonth(), entry.getTroughPriceDate()};
			
			if(entry.getPeakRatio()/10 > blankLine){
				blankLine = entry.getPeakRatio()/10;
				System.out.println();
			}
			System.out.format(String.format(format, ex));
		}
		
	}

	public void analyzePortfolioEntry(PortfolioEntry portfolioEntry) throws Exception {

//		System.out.println("\n\nPortfolio Entry : " + portfolioEntry.getTitle().getPlainText() );
//	    System.out.println("Atom ID: " + portfolioEntry.getId());

		if (portfolioEntry.getFeedLink().getFeed() == null) {
			System.out.println("\tNo portfolio inlined feed.");
		} else {
			for (PositionEntry positionEntry : portfolioEntry.getFeedLink().getFeed().getEntries()) {
				String ticker = positionEntry.getSymbol().getExchange() + ":" + positionEntry.getSymbol().getSymbol();
				queryPositionEntry(ticker, portfolioEntry.getId().substring(portfolioEntry.getId().lastIndexOf("/") + 1));
			}
		}
	}


	private void queryPositionEntry(String ticker, String portfolioId)    throws Exception {
		String entryURL = GOOGLE_FINANCE_SERVER + "/finance/feeds/default/portfolios/" 
			+ portfolioId
			+ "/positions/" 
			+  ticker;
		PositionEntry positionEntry = service.getEntry(new URL(entryURL), PositionEntry.class);

		System.out.print(". ");
		
		String symbol = positionEntry.getSymbol().getExchange() + ":" + positionEntry.getSymbol().getSymbol();
//		System.out.print(positionEntry.getTitle().getPlainText() + "(" + symbol + ") ");
		//		System.out.println("\tAtom ID: " + entry.getId());

		PositionData positionData = positionEntry.getPositionData();

		
		TickerPrice tickerPrice = new HistoricalPrice().queryPriceInfo(symbol); 


		if (positionData.getMarketValue() == null) {
			System.out.println("\tMarket Value not specified");
		} else {
			for (Money m :  positionData.getMarketValue().getMoney()) {
				tickerPrice.setCurrentPrice(m.getAmount());
//				System.out.format(" %.2f %s ", m.getAmount(), m.getCurrencyCode());
			}
		}

		tickerPrice.setTicker(symbol);
		tickerPrice.setName(positionEntry.getTitle().getPlainText());
		tickers.add(tickerPrice);
	}



	public static void main(String[] args) throws Exception {

		if (args.length != 2 && args.length != 3) {
			printUsage();
			System.exit(0);
		}

		String	userEmail = args[0];
		String	userPassword = args[1];

		FinanceService service = loginUser( userEmail, userPassword);
		if (service == null) {
			printUsage();
			System.exit(0);
		}

		MyGoogle myGoogle = new MyGoogle(service);
		
		int portfolioID = 1;
		if (args.length == 3){
			portfolioID = Integer.parseInt(args[2]);
		}
		
		myGoogle.queryPortfolioFeed(portfolioID);

	}


	private static void printUsage() {
		System.out.println("  Usage:");
		System.out.println("     <account> <password> <portfolio ID>");
	}
	
	/**
	  * Authenticates the user with the Google GOOGLE_FINANCE_SERVER after reading in the user
	  * email and password.
	  *
	  * @param service authenticated client connection to a Finance GData service
	  * @param userID Finance portfolio user ID (e.g. "bob@gmail.com")
	  * @param userPassword Finance portfolio user password (e.g. "Bobs$tocks")
	  * @return login success or failure
	  */
	 public static FinanceService loginUser(String userID, String userPassword) {
	 
	   FinanceService service = new FinanceService("Google-PortfoliosDemo-1.0");
	   try {
	     service.setUserCredentials(userID, userPassword);
	   } catch (AuthenticationException e) {
	     System.err.println("Invalid Credentials!");
	     e.printStackTrace();
	     return null;
	   }
	   return service;
	 }


}
