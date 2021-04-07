package websiteScrapper.other;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.util.*;

public class MAL {
    private static final String seasonSpring21 = "https://myanimelist.net/anime/season/2021/spring";

    public static ArrayList<Container> animeInfo(String url, boolean showURL) {
        ArrayList<Container> list = new ArrayList<>();
        WebClient client = new WebClient(BrowserVersion.CHROME);
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setUseInsecureSSL(false);
        try {
            HtmlPage page = client.getPage(url);
            List<HtmlElement> items = page.getByXPath("//div[@class='seasonal-anime js-seasonal-anime']");
            String[] urlInfo = url.split("/");
            System.out.println(items.size()+" results for the MAL search ("+urlInfo[urlInfo.length-1]+" "+urlInfo[urlInfo.length-2]+")");
            if(items.isEmpty()) return null;
            else {
                for(HtmlElement htmlItem : items) {
                    HtmlAnchor itemAnchor = ((HtmlAnchor) htmlItem.getFirstByXPath(".//a[@class='link-title']"));
                    HtmlElement spanMembers = ((HtmlElement) htmlItem.getFirstByXPath(".//span[@class='member fl-r']"));
                    HtmlElement spanScore = ((HtmlElement) htmlItem.getFirstByXPath(".//span[starts-with(@class,'score score-label')]"));
                    HtmlImage imageCover = ((HtmlImage) htmlItem.getFirstByXPath(".//div[@class='image']//img"));
                    HtmlElement spanDate = ((HtmlElement) htmlItem.getFirstByXPath(".//span[@class='remain-time']"));
                    String coverLink = imageCover.getSrcAttribute();
                    if(coverLink.equals("")) {
                        coverLink = imageCover.getAttribute("data-src");
                    }
                    String urlString = showURL ? itemAnchor.getHrefAttribute():"";
                    Container product = new Container(itemAnchor.asNormalizedText(), urlString, spanScore.asNormalizedText(), Integer.parseInt(spanMembers.asNormalizedText().replace(",","")), coverLink, setCalendar(spanDate.asNormalizedText()));
                    list.add(product);
                }
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Container> animeInfo(boolean showURL) {
        return animeInfo(seasonSpring21, showURL);
    }

    public static ArrayList<Container> animeInfo(String season, int year, boolean showURL) {
        return animeInfo("https://myanimelist.net/anime/season/"+year+"/"+season, showURL);
    }

    private static String setCalendar(String dateString) {
        Calendar date = Calendar.getInstance(TimeZone.getTimeZone("JST"));
        int parts = dateString.split(",").length;
        dateString=dateString.replace(",","");
        String[] info = dateString.split(" ");
        if(info[2].equals("????")) return "Unknown date";
        else date.set(Calendar.YEAR,Integer.parseInt(info[2]));
        switch (info[0]) {
            case "Jan" -> {date.set(Calendar.MONTH,0);}
            case "Feb" -> {date.set(Calendar.MONTH,1);}
            case "Mar" -> {date.set(Calendar.MONTH,2);}
            case "Apr" -> {date.set(Calendar.MONTH,3);}
            case "May" -> {date.set(Calendar.MONTH,4);}
            case "Jun" -> {date.set(Calendar.MONTH,5);}
            case "Jul" -> {date.set(Calendar.MONTH,6);}
            case "Aug" -> {date.set(Calendar.MONTH,7);}
            case "Sep" -> {date.set(Calendar.MONTH,8);}
            case "Oct" -> {date.set(Calendar.MONTH,9);}
            case "Nov" -> {date.set(Calendar.MONTH,10);}
            case "Dec" -> {date.set(Calendar.MONTH,11);}
            case "???" -> {return "In "+date.get(Calendar.YEAR);}
        }
        if(info[1].equals("??")) {
            return "In "+date.get(Calendar.MONTH)+" "+date.get(Calendar.YEAR);
        } else {
            date.set(Calendar.DAY_OF_MONTH,Integer.parseInt(info[1]));
        }
        if(parts!=2) {
            String[] hour = info[3].split(":");
            date.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour[0]));
            date.set(Calendar.MINUTE, Integer.parseInt(hour[1]));
        } else {
            return "On "+date.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())+" "+date.get(Calendar.DAY_OF_MONTH)+" "+date.get(Calendar.YEAR);
        }
        String dayOfWeek = date.getDisplayName(Calendar.DAY_OF_WEEK ,Calendar.LONG, Locale.ENGLISH);
        date.setTimeZone(TimeZone.getTimeZone("Europe/Lisbon"));
        return "On "+date.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)+" "+date.get(Calendar.DAY_OF_MONTH)+" "+date.get(Calendar.YEAR)+", at: "+date.get(Calendar.HOUR_OF_DAY)+":"+date.get(Calendar.MINUTE)+" (Every "+dayOfWeek+")";
    }
}
