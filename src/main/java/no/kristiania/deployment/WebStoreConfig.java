package no.kristiania.deployment;

import jakarta.inject.Singleton;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

public class WebStoreConfig extends ResourceConfig {

    public WebStoreConfig() {
        super(ProductEndPoint.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(ProductRepositoryImpl.class)
                        .to(ProductRepository.class)
                        .in(Singleton.class);
            }
        });
    }



}
