package hu.kits.wallet.infrastructure.web.vaadin;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;

import hu.kits.wallet.domain.PurchaseRepository;
import hu.kits.wallet.infrastructure.web.ui.WalletUI;

/*
 * If this server is used as the embedded Javalin server, then Vaadin apps can be run along with the standard Javalin http endpoints 
 */
public class VaadinJettyServer extends Server {

    public VaadinJettyServer(String vaadinContextRoot, int port, PurchaseRepository purchaseRepository) {
        super(port);
        setHandler(createHandler(vaadinContextRoot, purchaseRepository));
    }
    
    private Handler createHandler(String vaadinContextRoot, PurchaseRepository purchaseRepository) {
        ContextHandlerCollection handlerCollection = new ContextHandlerCollection();
        handlerCollection.setContextClass(ServletContextHandler.class);
        ServletContextHandler servletContextHandler = (ServletContextHandler)handlerCollection.addContext(vaadinContextRoot, "");
        servletContextHandler.setBaseResource(Resource.newClassPathResource("."));
        servletContextHandler.setSessionHandler(new SessionHandler());
        servletContextHandler.addServlet(createServletHolder(purchaseRepository), "/*"); 
        return handlerCollection;
    }
    
    private ServletHolder createServletHolder(PurchaseRepository purchaseRepository) {
        ServletHolder servletHolder = new ServletHolder(new WalletVaadinServlet(purchaseRepository));
        servletHolder.setInitParameter("UI", WalletUI.class.getCanonicalName());
        return servletHolder;
    }
    
}

