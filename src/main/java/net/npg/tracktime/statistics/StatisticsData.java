/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime.statistics;

import java.util.*;

/**
 * @author Cymric
 */
public class StatisticsData {

    private List<DayStatisticsData> dailyStatistics;

    public List<DayStatisticsData> getDailyStatistics() {
        return dailyStatistics;
    }

    public void setDailyStatistics(final List<DayStatisticsData> dailyStatistics) {
        this.dailyStatistics = dailyStatistics;
    }

    public StatisticsData() {
        dailyStatistics = new ArrayList<>();
    }

    public Collection<String> getProjects() {
        final Collection<String> projects = new HashSet<>();
        for (final DayStatisticsData day : dailyStatistics) {
            for (final String project : day.getProjectStatistics().keySet()) {
                projects.add(project);
            }
        }
        return projects;
    }

    public List<Date> getDays() {
        final List<Date> days = new ArrayList<>();
        for (final DayStatisticsData day : dailyStatistics) {
            days.add(day.getDate());
        }
        return days;
    }

    public DayStatisticsData getDay(final Date date) {
        for (final DayStatisticsData day : dailyStatistics) {
            if (day.getDate().equals(date)) {
                return day;
            }
        }
        return null;
    }
}
