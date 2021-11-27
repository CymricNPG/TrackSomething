/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime.statistics;

import net.npg.tracktime.model.DateUtil;
import net.npg.tracktime.model.JobDescriptionModel;
import net.npg.tracktime.model.TrackTimeDataModel;

import java.util.*;

/**
 * @author Cymric
 */
public class StatisticsCreator {

    public static StatisticsData create(final TrackTimeDataModel data) {
        final StatisticsData statisticsData = new StatisticsData();
        final List<DayStatisticsData> dailyStatistics = fillDailyStatistics(data);
        statisticsData.setDailyStatistics(dailyStatistics);
        return statisticsData;
    }

    private static List<DayStatisticsData> fillDailyStatistics(final TrackTimeDataModel data) {
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

    private static Collection<Date> findDays(final TrackTimeDataModel data) {
        final Collection<Date> dates = DateUtil.dailySorted();
        for (final JobDescriptionModel jobDescription : data.getJobDescriptions()) {
            dates.addAll(jobDescription.getDays());
        }
        return dates;
    }

    private static Map<String, Long> fillProjectStatistics(final TrackTimeDataModel data, final Date date) {
        final Map<String, Long> projectStatistics = new HashMap<>();
        for (final JobDescriptionModel jobDescription : data.getJobDescriptions()) {
            projectStatistics.put(jobDescription.getName(), jobDescription.getTotalTime(date));
        }
        return projectStatistics;
    }
}
