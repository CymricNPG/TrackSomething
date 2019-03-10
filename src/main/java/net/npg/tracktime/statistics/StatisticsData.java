/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime.statistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Cymric
 */
public class StatisticsData {

    private List<DayStatisticsData> dailyStatistics;

    public List<DayStatisticsData> getDailyStatistics() {
        return dailyStatistics;
    }

    public void setDailyStatistics(List<DayStatisticsData> dailyStatistics) {
        this.dailyStatistics = dailyStatistics;
    }

    public StatisticsData() {
        dailyStatistics = new ArrayList<>();
    }

    public Collection<String> getProjects() {
        Collection<String> projects = new HashSet<>();
        for (DayStatisticsData day : dailyStatistics) {
            for (String project : day.getProjectStatistics().keySet()) {
                projects.add(project);
            }
        }
        return projects;
    }

    public List<Date> getDays() {
        List<Date> days = new ArrayList<>();
        for (DayStatisticsData day : dailyStatistics) {
            days.add(day.getDate());
        }
        return days;
    }

    public DayStatisticsData getDay(Date date) {
        for (DayStatisticsData day : dailyStatistics) {
            if (day.getDate().equals(date)) {
                return day;
            }
        }
        return null;
    }
}
