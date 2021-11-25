/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime.statistics;

import net.npg.tracktime.data.DateUtil;
import net.npg.tracktime.data.JobDescription;
import net.npg.tracktime.data.TrackTimeData;

import java.util.*;

/**
 * @author Cymric
 */
public class StatisticsCreator {

    public static StatisticsData create(final TrackTimeData data) {
        final StatisticsData statisticsData = new StatisticsData();
        final List<DayStatisticsData> dailyStatistics = fillDailyStatistics(data);
        statisticsData.setDailyStatistics(dailyStatistics);
        return statisticsData;
    }

    private static List<DayStatisticsData> fillDailyStatistics(final TrackTimeData data) {
        final List<DayStatisticsData> dailyStatistics = new ArrayList<>();
        final Collection<Date> dates = findDays(data);
        for (final Date date : dates) {
            final Map<String, Long> projectStatistics = fillProjectStatistics(data, date);
            final DayStatisticsData dayStatisticsData = new DayStatisticsData(date);
            dayStatisticsData.setProjectStatistics(projectStatistics);
            dailyStatistics.add(dayStatisticsData);
        }
        return dailyStatistics;
    }

    private static Collection<Date> findDays(final TrackTimeData data) {
        final Collection<Date> dates = DateUtil.dailySorted();
        for (final JobDescription jobDescription : data.getJobDescriptions()) {
            dates.addAll(jobDescription.getDays());
        }
        return dates;
    }

    private static Map<String, Long> fillProjectStatistics(final TrackTimeData data, final Date date) {
        final Map<String, Long> projectStatistics = new HashMap<>();
        for (final JobDescription jobDescription : data.getJobDescriptions()) {
            projectStatistics.put(jobDescription.getName(), jobDescription.getTotalTime(date));
        }
        return projectStatistics;
    }
}
