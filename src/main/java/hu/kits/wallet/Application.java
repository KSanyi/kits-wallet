package hu.kits.wallet;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.LogManager;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.mysql.cj.jdbc.MysqlDataSource;

import hu.kits.wallet.domain.PurchaseRepository;
import hu.kits.wallet.infrastructure.db.PurchaseJdbiRepository;
import hu.kits.wallet.infrastructure.web.HttpServer;

public class Application {

    static {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.install();
        java.util.logging.Logger.getLogger("global").setLevel(Level.SEVERE);
    }
    
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    public static void main(String[] args) throws Exception {

        int port = getPort();

        URI dbUri = getDatabaseUri();
        
        DataSource dataSource = createDataSource(dbUri);
        
        PurchaseRepository purchaseRepository = new PurchaseJdbiRepository(dataSource);
        
        new HttpServer(port, purchaseRepository).start();;
    }
    
    private static int getPort() {
        String port = System.getenv("PORT");
        if (port == null) {
            throw new IllegalArgumentException("System environment variable PORT is missing");
        }

        try {
            int portNumber = Integer.parseInt(port);
            logger.info("PORT: " + port);
            return portNumber;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Illegal system environment variable PORT: " + port);
        }
    }
    
    private static URI getDatabaseUri() throws URISyntaxException {
        String databaseUrl = System.getenv("CLEARDB_DATABASE_URL");
        if (databaseUrl == null) {
            throw new IllegalArgumentException("System environment variable CLEARDB_DATABASE_URL is missing");
        }
        
        return new URI(databaseUrl);
    }
    
    private static DataSource createDataSource(URI dbUri) throws URISyntaxException {
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String jdbcUrl = "jdbc:mysql://" + dbUri.getHost() + dbUri.getPath() + "?" + dbUri.getQuery(); 
        
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(jdbcUrl);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        return dataSource;
    }
    
}
