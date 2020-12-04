package hu.kits.wallet.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hu.kits.wallet.common.Clock;

public class DateInterval implements Comparable<DateInterval> {

    private final LocalDate from;
    private final LocalDate to;
    
    public DateInterval(LocalDate from, LocalDate to) {
        if(from == null) from = MIN;
        if(to == null) to = MAX;
        if(to.isBefore(from)) {
            throw new IllegalArgumentException("Invalid interval: " + from + " - " + to);
        }
        this.from = from;
        this.to = to;
    }
    
    public LocalDate from() {
        return from;
    }

    public LocalDate to() {
        return to;
    }

    private static final LocalDate MIN = LocalDate.of(2000,1,1);
    private static final LocalDate MAX = LocalDate.of(2050,1,1);
    
	public static DateInterval defaultValue() {
	    return new DateInterval(Clock.today().withDayOfYear(1), Clock.today());
	}
	
	public static DateInterval thisMonth(LocalDate today) {
        return new DateInterval(today.withDayOfMonth(1), today.plusMonths(1).withDayOfMonth(1).minusDays(1));
    }
	
	public static DateInterval thisYear(LocalDate today) {
        return new DateInterval(today.withDayOfYear(1), today);
    }
	
	public static DateInterval openUntilToday() {
        return new DateInterval(MIN, Clock.today());
    }
	
	public boolean contains(LocalDate value) {
		return !value.isBefore(from) && !value.isAfter(to);
	}
	
	public boolean contains(DateInterval other) {
        return !this.from.isAfter(other.from) && !this.to.isBefore(other.to);
    }
	
	public List<LocalDate> days() {
	    List<LocalDate> days = new ArrayList<>();
	    LocalDate date = from;
	    while(!date.isAfter(to)) {
	        days.add(date);
	        date = date.plusDays(1);
	    }
	    return Collections.unmodifiableList(days);
	}
	
	public int numberOfDays() {
	    return (int)Duration.between(from.atStartOfDay(), to.atStartOfDay()).toDays();
	}
	
	@Override
    public String toString() {
		String fromString = from .equals(LocalDate.MIN) ? "" : from.toString();
		String toString = from .equals(MAX) ? "" : to.toString();
        return "[" + fromString + " - " + toString + "]";
    }

	@Override
	public int compareTo(DateInterval other) {
		return from.compareTo(other.from);
	}
	
}
	
