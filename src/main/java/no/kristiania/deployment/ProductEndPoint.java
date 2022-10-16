package no.kristiania.deployment;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Path("/products")
public class ProductEndPoint {

/*    @Inject
    private ProductRepository productRepository;*/

    private static final List<Product> products = new ArrayList<>();
    static {
        var product = new Product("A fork", ProductCategory.FORKS, 4090);
        var product2 = new Product("Another fork", ProductCategory.ALSO_FORKS, 13);
        var product3 = new Product("A pretty fork", ProductCategory.A_FORK, 9001);
        var product4 = new Product("A dork", ProductCategory.FORKSES, -404);
        products.add(product);
        products.add(product2);
        products.add(product3);
        products.add(product4);
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts() {

        var result = Json.createArrayBuilder();
        for (var product : products) {
            result.add(Json.createObjectBuilder()
                    .add("name", product.name())
                    .add("category", product.category().toString())
                    .add("price", product.price())
            );
        }
        return Response.ok(result.build().toString()).build();
    }

    @POST
    public Response addProduct(String body) {
        JsonObject obj = Json.createReader(new StringReader(body)).readObject();

        String name = obj.getString("name");
        ProductCategory category = ProductCategory.parse(obj.getString("category"));
        if (category == null) return Response.status(400).build();
        int price = Integer.parseInt(obj.getString("price"));

        products.add(new Product(name, category, price));

        return Response.ok().build();
    }
}
