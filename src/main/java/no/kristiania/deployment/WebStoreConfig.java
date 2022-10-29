package no.kristiania.deployment;

import jakarta.inject.Singleton;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> props = new HashMap<>();
        props.put("jersey.config.server.wadl.disableWadl", "true");
        setProperties(props);
    }



}
