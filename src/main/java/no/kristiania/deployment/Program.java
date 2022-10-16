package no.kristiania.deployment;

public class Program {
    public static void main(String[] args) throws Exception {

        var webShop = new WebShopServer(8080);
        webShop.start();
    }
}
