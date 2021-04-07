package websiteScrapper.onlineStores;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.util.List;

public class PCDiga {
    public static final String exampleURL="https://www.pcdiga.com/componentes/placas-graficas/placas-graficas-nvidia?z_gpu_model=6487";

    public static String productInfo(String url, boolean showURL, boolean onlyInStock) {
        StringBuffer sb = new StringBuffer();
        sb.append("These are the products in stock:");
        WebClient client = new WebClient(BrowserVersion.CHROME);
        client.waitForBackgroundJavaScriptStartingBefore(2000);
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setUseInsecureSSL(false);
        try {
            HtmlPage page = client.getPage(url);
            List<HtmlElement> items = page.getByXPath("//li[@class='product-item item product ']");
            if(items.isEmpty()) return ("No valid results for this page");
            else {
                HtmlElement spanResults = ((HtmlElement) page.getFirstByXPath(".//span[@data-role='total']"));
                if(spanResults!=null) {
                    String results = " (Total: "+spanResults.asNormalizedText()+" results)";
                    sb.append(results);
                }
                for(HtmlElement htmlItem : items) {
                    HtmlAnchor itemAnchor = ((HtmlAnchor) htmlItem.getFirstByXPath(".//a[@class='product-item-link']"));
                    HtmlElement spanPrice = ((HtmlElement) htmlItem.getFirstByXPath(".//span[@class='price']"));
                    HtmlElement spanStock = ((HtmlElement) htmlItem.getFirstByXPath(".//div[starts-with(@class,'skrey_estimate_date_wrapper')]//span"));
                    String urlString = showURL ? itemAnchor.getHrefAttribute():"";
                    String stockString = spanStock.asNormalizedText();
                    if(stockString.equals("Sem stock")&&onlyInStock) continue;
                    Container product = new Container(itemAnchor.asNormalizedText(), spanPrice.asNormalizedText(), urlString, stockString);
                    sb.append("\n");
                    sb.append(product.toString());
                }
            }
            return sb.toString().equals("These are the products in stock:")? "There are no products in stock.":sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String productInfo(String url) {
        return productInfo(url, true, false);
    }

    public static String searchProduct(String product) {
        product=product.replace(" ","%20");
        return "https://www.pcdiga.com/#/embedded/query="+product+"&page=1&query_name=match_and";
    }
}
