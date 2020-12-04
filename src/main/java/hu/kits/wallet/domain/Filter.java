package hu.kits.wallet.domain;

public class Filter {

    private final String filterString;
    private final DateInterval dateInterval;
    
    public Filter(String filterString, DateInterval dateInterval) {
        this.filterString = filterString;
        this.dateInterval = dateInterval;
    }

    public String filterString() {
        return filterString;
    }
    
    public DateInterval dateInterval() {
        return dateInterval;
    }
    
}
