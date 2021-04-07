package websiteScrapper.other;

public class Container {
    String title;
    String url;
    String score;
    int members;
    String cover;
    String dateInfo;

    Container(String title, String url, String score, int members, String cover, String dateInfo) {
        this.title = title;
        this.url = url;
        this.score=score;
        this.members=members;
        this.cover=cover;
        this.dateInfo=dateInfo;
    }

    public int getMembers() {
        return members;
    }

    @Override
    public String toString() {
        String out = "Title: " + title + ", Members: " + members + ", Score: " + score + ", Cover Image: "+cover+", Starts airing: "+dateInfo;
        return url.equals("") ? (out) : (out + ", URL: " + url);
    }
}
