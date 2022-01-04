package hu.kits.wallet.infrastructure.db;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import com.mysql.cj.jdbc.MysqlDataSource;

import hu.kits.wallet.domain.Purchase;
import hu.kits.wallet.domain.Purchase.Account;
import hu.kits.wallet.domain.Purchase.Category;

public class DataLoader {

    public static void main(String[] args) throws IOException {
        
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://localhost:3306/wallet?reconnect=true&useUnicode=true&characterEncoding=utf-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=CET&useSSL=false");
        dataSource.setUser("wallet");
        dataSource.setPassword("Alma1234");
        
        PurchaseJdbiRepository purchaseJdbiRepository = new PurchaseJdbiRepository(dataSource);
        
        List<Purchase> purchase = parsePurchases();
        
        purchase.stream().forEach(purchaseJdbiRepository::create);
    }

    private static List<Purchase> parsePurchases() throws IOException {
        
        List<String> lines = Files.readAllLines(Paths.get("./data/purchases.txt"));
        
        return lines.stream().map(DataLoader::parsePurchase).collect(toList());
    }
    
    private static Purchase parsePurchase(String line) {
     
        String parts[] = line.split("\t");
        
        try {
            return new Purchase(null,
                    Account.valueOf(parts[5]),
                    LocalDate.parse(parts[0]),
                    Integer.parseInt(parts[1]),
                    Category.valueOf(parts[2]),
                    parts[3],
                    parts[4],
                    parts[5],
                    List.of(),
                    null);    
        } catch(Exception ex) {
            System.err.println("Error parsing line: " + line);
            throw new RuntimeException(ex);
        }
        
    }
    
}
