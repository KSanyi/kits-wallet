package hu.kits.wallet.infrastructure.web;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.kits.wallet.domain.PurchaseRepository;
import hu.kits.wallet.infrastructure.web.vaadin.VaadinJettyServer;
import io.javalin.Javalin;

public class HttpServer {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    private final Javalin app;
    
    public HttpServer(int port, PurchaseRepository purchaseRepository) {
        
        app = Javalin.create()
            .server(() -> new VaadinJettyServer("/ui", port, purchaseRepository));
            //.post("/lead/", this::handleLeadArrival);
    }

    public void start() {
        app.start();
        logger.info("KIST Wallet started");
    }
    
    public void stop() {
        app.stop();
        logger.info("KITS Wallet stopped");
    }
    
}