package org.hf.aic;
import java.util.Map;
import java.util.TreeMap;

import com.meterware.httpunit.HTMLElement;
import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.TableCell;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;


public final  class ITDiscount {
	
	String serverName = "http://theaic.co.uk";

	public void getList(boolean listAll) throws Exception{
	
	    String[] names = new String[] {
	    		"BlackRock World Mining",
	    		"BlackRock Latin American",
	    		"F&C Commercial Property",
	    		"JPMorgan Indian",
	    		"JPMorgan Russian Securities",
	    		"Polar Capital Technology",
	    		"TR Property",
	    		"Henderson TR Pacific",
	    		
	    		
//	    		"Aberforth Smaller Companies",
//	    		"Albany",
//	    		"Aurora",
	    		"Alliance Trust",
//	    		"Bankers",
//	    		"Brunner",
//	    		"British Empire Securities & General",
//	    		"British & American",
//	    		"Caledonia",  // Not as good as Alliance Trust
	    		"City of London",
////	    		"Candover",  //Private equity
//	    		"Dunedin Income Growth",
//	    		"Edinburgh Investment",
	    		"Edinburgh Dragon",
//	    		"F&C Global Smaller Companies",
//	    		"F&C Capital & Income", 
	    		"Foreign & Colonial",
//	    		"INVESCO Income Growth",
//	    		"Lowland",
	    		"Fidelity China Special Situations",
//	    		"JPMorgan Claverhouse",
	    		"JPMorgan European Smaller Companies",
//	    		"Mercantile",
//	    		"Merchants",
//	    		"Murray Income",
////	    		"Northern Investors", // Private equity
//	    		"Personal Assets",
	    		"Scottish Investment Trust",
	    		"Scottish Mortgage",
                "Scottish American",
//                "Schroder Income Growth",
//                "Temple Bar",
	    		"Templeton Emerging Markets"
//	    		"UK Select Trust",
//	    		"Value and Income",
//	    		"Witan"
	    		};
		
		HttpUnitOptions.setScriptingEnabled(false);
		WebConversation webConversation = new WebConversation();
		
		String url = serverName
				+ "/Search-for-an-investment-company/A-Z-lists/Investment-companies/?filterBy=Letter|";
		WebResponse response = webConversation.getResponse(url);
		
		Map<String, String> urls = new TreeMap<String, String>();
		
		
		if(! listAll) {
			for (String name : names) {
				String urlString = response.getLinkWith(name).getURLString();
				urls.put(name, serverName + urlString.replaceFirst("Overview", "Performance"));
			}
		} else{

			WebTable table = response.getTableWithSummary("Investment companies A-Z list search results");
			for ( int i = 1 ; i < table.getRowCount(); ++i) {
				TableCell cell = table.getTableCell(i, 0);
				urls.put(cell.getText(), serverName + cell.getLinks()[0].getURLString().replaceFirst("Overview", "Performance"));
			}
		}
		
		
		String titleFormat = "\n%1$50s  %2$10s  %3$10s %4$10s %5$10s %6$10s %7$10s %8$10s";
		System.out.format(String.format(titleFormat, new Object[]{
				"Name", "Dis", "Gearing" , "DY", "1 Year", "3 Year", "5 Year", "TER"
		}));

		
		for (String name : urls.keySet()) {
			
			if(name.contains("VCT") || name.contains("Private Equity") || name.contains("Enterprise") ) {
				continue;
			}
			
			String priceTab = urls.get(name);
			response = webConversation.getResponse(priceTab);
			WebTable overviewTable = response.getTableWithSummary("Overview");
			WebTable perfTable = response.getTableWithSummary("Historical performance");
			
			WebResponse response2 = webConversation.getResponse(priceTab.replaceFirst("Performance", "Profile"));
			HTMLElement[] divs = response2.getElementsByTagName("div");
			
			boolean b = false;
			String ter = "N/A";
			for (HTMLElement div : divs) {
				if (!  b){
				 b =	"Lipper TER%:".equalsIgnoreCase(div.getText());
				} else{
				   ter = div.getText();
				   break;
				}
			}
			
			try {
				String dy = overviewTable.getTableCell(1, 7).getText();
				if	( !listAll || listAll && ! dy.contains("/")  && Float.parseFloat(dy) > 6.0 ){ 

					System.out.format(String.format(titleFormat, new Object[]{
							name,
							overviewTable.getTableCell(1, 3).getText(),
							overviewTable.getTableCell(1, 4).getText(),
							overviewTable.getTableCell(1, 7).getText(),
							perfTable.getTableCell(1, 1).getText(),
							perfTable.getTableCell(1, 2).getText(),
							perfTable.getTableCell(1, 3).getText(),
							ter
					}));
				}
				} catch (NumberFormatException e) {
					System.out.format(String.format(titleFormat, new Object[]{
							name,
							overviewTable.getTableCell(1, 3).getText(),
							overviewTable.getTableCell(1, 4).getText(),
							overviewTable.getTableCell(1, 7).getText(),
							perfTable.getTableCell(1, 1).getText(),
							perfTable.getTableCell(1, 2).getText(),
							perfTable.getTableCell(1, 3).getText(),
							ter
					}));

				}

			}
		
	}
	
   public static void main(String[] args) throws Exception {
	 ITDiscount dd = new ITDiscount();
	 dd.getList(args.length > 0 );
	 
	 
}

}
