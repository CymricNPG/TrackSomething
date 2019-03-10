/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime.data;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.TreeSet;

/**
 *
 * @author Cymric
 */
public class DateUtil {

    private static final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);

    public static String shortDate(Date date) {
        return dateFormat.format(date);
    }

    public static Comparator<Date> dailyComparator() {
        return new Comparator<Date>() {
            @Override
            public int compare(Date o1, Date o2) {
                String date1 = DateUtil.shortDate(o1);
                String date2 = DateUtil.shortDate(o2);
                if (date1.equals(date2)) {
                    return 0;
                } else {
                    return o1.compareTo(o2);
                }
            }
        };
    }

    public static Collection<Date> dailySorted() {
        return new TreeSet<>(DateUtil.dailyComparator());
    }
}
