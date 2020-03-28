package hu.kits.wallet.infrastructure.db;

import java.lang.invoke.MethodHandles;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.kits.wallet.domain.Purchase;
import hu.kits.wallet.domain.PurchaseRepository;
import hu.kits.wallet.domain.Purchases;

public class PurchaseJdbiRepository implements PurchaseRepository {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    private static final String TABLE_PURCHASE = "PURCHASE";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_ACCOUNT = "ACCOUNT";
    private static final String COLUMN_DATE = "DATE";
    private static final String COLUMN_AMOUNT = "AMOUNT";
    private static final String COLUMN_CATEGORY = "CATEGORY";
    private static final String COLUMN_SHOP = "SHOP";
    private static final String COLUMN_SUBJECT = "SUBJECT";
    private static final String COLUMN_COMMENT = "COMMENT";
    
    private final Jdbi jdbi;

    public PurchaseJdbiRepository(DataSource dataSource) {
        jdbi = Jdbi.create(dataSource);
    }

    public Purchases loadAll() {
        String sql = String.format("SELECT * FROM %s", TABLE_PURCHASE);
        
        List<Purchase> purchaseList = jdbi.withHandle(handle -> 
            handle.createQuery(sql).map((rs, ctx) -> mapToPurchase(rs)).list());
        
        return new Purchases(purchaseList);
    }
    
    private static Purchase mapToPurchase(ResultSet rs) throws SQLException {
        
        return new Purchase(
                rs.getLong(COLUMN_ID),
                Purchase.Account.valueOf(rs.getString(COLUMN_ACCOUNT)),
                rs.getDate(COLUMN_DATE).toLocalDate(),
                rs.getInt(COLUMN_AMOUNT),
                Purchase.Category.valueOf(rs.getString(COLUMN_CATEGORY)),
                rs.getString(COLUMN_SHOP),
                rs.getString(COLUMN_SUBJECT),
                rs.getString(COLUMN_COMMENT));
    }

    public void create(Purchase purchase) {
        Map<String, Object> values = new HashMap<>();
        values.put(COLUMN_ACCOUNT, purchase.account().name());
        values.put(COLUMN_DATE, purchase.date());
        values.put(COLUMN_AMOUNT, purchase.amount());
        values.put(COLUMN_CATEGORY, purchase.category().name());
        values.put(COLUMN_SHOP, purchase.shop());
        values.put(COLUMN_SUBJECT, purchase.subject());
        values.put(COLUMN_COMMENT, purchase.comment());
        
        jdbi.withHandle(handle -> JdbiUtil.createInsert(handle, TABLE_PURCHASE, values).execute());
        
        log.info("Purchase saved: {}", purchase);
    }

    @Override
    public void delete(long id) {
        jdbi.withHandle(handle -> handle.execute(String.format("DELETE FROM %s WHERE %s = ?", TABLE_PURCHASE, COLUMN_ID), id));
        log.info("Purchase with id {} deleted", id);
    }

    @Override
    public void updateOrCreate(Purchase purchase) {
        if(purchase.id() != null) {
            delete(purchase.id());
        }
        create(purchase);
    }
    
}
