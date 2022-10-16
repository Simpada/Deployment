package no.kristiania.deployment;

import jakarta.json.Json;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class WebShopServerTest {

    private WebShopServer server;

    @BeforeEach
    void setup() throws Exception {
        server = new WebShopServer(0);
        server.start();
    }

    private HttpURLConnection getConnection(String path) throws IOException {
        return (HttpURLConnection) new URL(server.getUrl(), path).openConnection();
    }

    @Test
    void shouldGetIndexHtml() throws Exception {

        HttpURLConnection connection = getConnection("/");

        assertThat(connection.getResponseCode())
                .as(connection.getResponseCode() + " " + connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);
        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("<title>Mimpada's WebStore</title>");

    }

    @Test
    void apiShouldGetProducts() throws Exception {

        var connection = getConnection("/api/products");

        assertThat(connection.getResponseCode())
                .as(connection.getResponseCode() + " " + connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);
        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("\"name\":\"A dork\"");
    }

    @Test
    void shouldAddProducts() throws IOException {
        var postConnection = getConnection("/api/products");
        postConnection.setRequestMethod("POST");
        postConnection.setDoOutput(true);
        postConnection.getOutputStream().write(
                Json.createObjectBuilder()
                        .add("name", "Archaon The Everchosen")
                        .add("category", "ALSO_FORKS")
                        .add("price", "15")
                        .build()
                        .toString()
                        .getBytes(StandardCharsets.UTF_8)
        );

        AssertionsForClassTypes.assertThat(postConnection.getResponseCode())
                .as(postConnection.getResponseCode() + " " + postConnection.getResponseMessage() + " for " + postConnection.getURL())
                .isEqualTo(200);

        var connection = getConnection("/api/products");
        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("\"name\":\"Archaon The Everchosen\"");
    }

    @Test
    void shouldGive400ForMalformedProductPost() throws IOException {
        var postConnection = getConnection("/api/products");
        postConnection.setRequestMethod("POST");
        postConnection.setDoOutput(true);
        postConnection.getOutputStream().write(
                Json.createObjectBuilder()
                        .add("name", "Github Fork")
                        .add("category", "FRK")
                        .add("price", 10_0000_000)
                        .build()
                        .toString()
                        .getBytes(StandardCharsets.UTF_8)
        );

        AssertionsForClassTypes.assertThat(postConnection.getResponseCode())
                .as(postConnection.getResponseCode() + " " + postConnection.getResponseMessage() + " for " + postConnection.getURL())
                .isEqualTo(400);

        var connection = getConnection("/api/products");
        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .doesNotContain("\"name\":\"Github Fork\"");
    }


}
