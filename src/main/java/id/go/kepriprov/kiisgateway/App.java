package id.go.kepriprov.kiisgateway;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

import id.go.kepriprov.kiisgateway.lib.Configuration;

public class App {

	public static void main(String[] args) throws Exception {
		Configuration config = new Configuration();
		
		Server server = new Server(config.getJettyPort());
		
		ServletContextHandler context = new ServletContextHandler(server,"/*",ServletContextHandler.SESSIONS);
		
		ServletHolder sh = new ServletHolder(ServletContainer.class);
		sh.setInitParameter(ServerProperties.PROVIDER_CLASSNAMES, "id.go.kepriprov.kiisgateway.services.EntryPoint");
		sh.setInitParameter(ServerProperties.PROVIDER_SCANNING_RECURSIVE, "true");
        sh.setInitParameter(ServerProperties.TRACING, "ALL");
        sh.setInitParameter("jersey.config.server.tracing", "ALL");
        sh.setInitOrder(1);
		
        context.addServlet(sh, "/*");
        server.start();
        server.join();
		
	}
}