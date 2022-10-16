package no.kristiania.deployment;

import java.util.List;

public interface ProductRepository {

    List<Product> listAll();

    void save(Product product);
}
