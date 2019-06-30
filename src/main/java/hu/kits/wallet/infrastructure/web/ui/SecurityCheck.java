package hu.kits.wallet.infrastructure.web.ui;

import java.util.List;

import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;

public class SecurityCheck {

    private static final String SECURITY_KEY_PARAM = "SKEY";
    private static final String SECURITY_KEY = "ksymcd82me252md239d";
    
    public static void checkSecurityKey(BeforeEvent event) {
        
        Location location = event.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        List<String> paramValues = queryParameters.getParameters().get(SECURITY_KEY_PARAM);
        if(paramValues != null && paramValues.contains(SECURITY_KEY)) {
            System.out.println("Hurray");
        } else {
            System.out.println("Ahhhh");
        }
        
    }
    
}
