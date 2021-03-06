/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime.model;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.TreeSet;

/**
 * @author Cymric
 */
public class DateUtil {

    private static final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);

    public static String shortDate(final Date date) {
        return dateFormat.format(date);
    }

    public static Comparator<Date> dailyComparator() {
        return (o1, o2) -> {
            final String date1 = DateUtil.shortDate(o1);
            final String date2 = DateUtil.shortDate(o2);
            if (date1.equals(date2)) {
                return 0;
            } else {
                return o1.compareTo(o2);
            }
        };
    }

    public static Collection<Date> dailySorted() {
        return new TreeSet<>(DateUtil.dailyComparator());
    }
}
