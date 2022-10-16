package no.kristiania.deployment;

import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {

    private final List<Product> products = new ArrayList<>();

    ProductRepositoryImpl (){
        var product = new Product("A fork", ProductCategory.FORKS, 4090);
        var product2 = new Product("Another fork", ProductCategory.ALSO_FORKS, 13);
        var product3 = new Product("A pretty fork", ProductCategory.A_FORK, 9001);
        var product4 = new Product("A dork", ProductCategory.FORKSES, -404);
        products.add(product);
        products.add(product2);
        products.add(product3);
        products.add(product4);
    }

    @Override
    public List<Product> listAll() {
        return products;
    }

    @Override
    public void save(Product product) {
        products.add(product);
    }

}
