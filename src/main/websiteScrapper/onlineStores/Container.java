package websiteScrapper.onlineStores;

class Container {
    String title;
    double price;
    String url;
    String stock;

    Container(String title, String price, String url, String stock) {
        this.title = title;
        this.price = priceToLong(price);
        this.url = url;
        this.stock = stock;
    }

    double priceToLong(String price) {
        price = price.replace("€", "");
        price = price.replace(" ", "");
        price = price.replace(",", ".");
        return Double.parseDouble(price);
    }

    @Override
    public String toString() {
        String out = "Product: " + title + ", Price: " + price + ", Stock: " + stock;
        return url.equals("") ? (out) : (out + ", URL: " + url);
    }
}
