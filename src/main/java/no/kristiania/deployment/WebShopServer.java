package no.kristiania.deployment;

import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.CustomRequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
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
        server.setHandler( new HandlerList(createApiContext(), createWebAppContext()));
        server.setRequestLog(new CustomRequestLog());
    }

    private WebAppContext createWebAppContext() throws IOException {
        var context = new WebAppContext();
        context.setContextPath("/");

        var resources = Resource.newClassPathResource("/webShop");
        File directory = getSourceDirectory(resources);
        if (directory != null) {
            context.setBaseResource(Resource.newResource(directory));
            context.setInitParameter(DefaultServlet.CONTEXT_INIT + "useFileMappedBuffer", "false");
        } else {
            context.setBaseResource(resources);
        }
        
        var apiServlet = context.addServlet(ServletContainer.class, "/api/*");
        apiServlet.setInitParameter("jersey.config.server.provider.packages", "no.kristiania.deployment");

        context.addFilter(new FilterHolder(new webShopFilter()), "/*", EnumSet.of(DispatcherType.REQUEST));

        return context;
    }

    private static File getSourceDirectory(Resource resource) throws IOException {

        if (resource.getFile() == null) {
            return null;
        }

        var sourceDirectory = new File(resource.getFile().getAbsoluteFile()
                .toString()
                .replace('\\', '/')
                .replace("target/classes", "src/main/resources"));
        return sourceDirectory.exists() ? sourceDirectory : null;
    }

    public ServletContextHandler createApiContext() {
        var context = new ServletContextHandler(server, "/api");
        context.addServlet(new ServletHolder(new ServletContainer(
                new WebStoreConfig()
        )), "/*");
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
