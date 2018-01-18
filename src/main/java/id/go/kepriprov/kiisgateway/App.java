package id.go.kepriprov.kiisgateway;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import id.go.kepriprov.kiisgateway.lib.conf.Configuration;

public class App {

	public static void main(String[] args) {
		Configuration config = new Configuration();
		
		Server server = new Server(config.getJettyPort());
		
		ServletContextHandler ctx = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
		
		ctx.setContextPath("/");
        server.setHandler(ctx);
        
        ServletHolder serHol = ctx.addServlet(ServletContainer.class, "/*");
        serHol.setInitOrder(1);
        serHol.setInitParameter("jersey.config.server.provider.packages","id.go.kepriprov.kiisgateway.services");
        try {
            server.start();
            server.join();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            server.destroy();
        }		
		
	}
}