package no.kristiania.deployment;

import java.util.Optional;

public class Program {
    public static void main(String[] args) throws Exception {

        var port = Optional.ofNullable(System.getenv("HTTP_PLATFORM_PORT"))
                .map(Integer::parseInt)
                .orElse(8080);

        new WebShopServer(port).start();
    }
}
