package no.kristiania.deployment;

import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumSet;

public class WebShopServer {

    private final Logger log = LoggerFactory.getLogger(WebShopServer.class);
    private final Server server;
    public WebShopServer(int port) throws IOException {
        server = new Server(port);
        server.setHandler(createApiContext());
    }

    private WebAppContext createApiContext() throws IOException {
        var context = new WebAppContext();
        context.setContextPath("/");

        var resources = Resource.newClassPathResource("/webShop");

        var directory = new File(resources.getFile().getAbsoluteFile()
                .toString()
                .replace('\\', '/')
                .replace("target/classes", "src/main/resources"));


        if (directory.isDirectory()) {
            context.setBaseResource(Resource.newResource(directory));
            context.setInitParameter(DefaultServlet.CONTEXT_INIT + "useFileMappedBuffer", "false");
        } else {
            context.setBaseResource(resources);
        }
        
        var apiServlet = context.addServlet(ServletContainer.class, "/api/*");

        /*
        context.addServlet(new ServletHolder(new ServletContainer(
                new WebStoreConfig()
        )), "/*");
         */

        apiServlet.setInitParameter("jersey.config.server.provider.packages", "no.kristiania.deployment");

        context.addFilter(new FilterHolder(new webShopFilter()), "/*", EnumSet.of(DispatcherType.REQUEST));


        return context;
    }

    public URL getUrl() throws MalformedURLException {
        return server.getURI().toURL();
    }

    public void start() throws Exception {
        server.start();
        log.info("Started server at {}", getUrl());
    }


}
