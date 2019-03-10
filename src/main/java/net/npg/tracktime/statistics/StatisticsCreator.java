/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime.statistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.npg.tracktime.data.DateUtil;
import net.npg.tracktime.data.JobDescription;
import net.npg.tracktime.data.TrackTimeData;

/**
 *
 * @author Cymric
 */
public class StatisticsCreator {

    public static StatisticsData create(TrackTimeData data) {
        StatisticsData statisticsData = new StatisticsData();
        List<DayStatisticsData> dailyStatistics = fillDailyStatistics(data);
        statisticsData.setDailyStatistics(dailyStatistics);
        return statisticsData;
    }

    private static List<DayStatisticsData> fillDailyStatistics(TrackTimeData data) {
        List<DayStatisticsData> dailyStatistics = new ArrayList<>();
        Collection<Date> dates = findDays(data);
        for (Date date : dates) {
            Map<String, Long> projectStatistics = fillProjectStatistics(data, date);
            DayStatisticsData dayStatisticsData = new DayStatisticsData(date);
            dayStatisticsData.setProjectStatistics(projectStatistics);
            dailyStatistics.add(dayStatisticsData);
        }
        return dailyStatistics;
    }

    private static Collection<Date> findDays(TrackTimeData data) {
        Collection<Date> dates = DateUtil.dailySorted();
        for (JobDescription jobDescription : data.getJobDescriptions()) {
            dates.addAll(jobDescription.getDays());
        }
        return dates;
    }

    private static Map<String, Long> fillProjectStatistics(TrackTimeData data, Date date) {
        Map<String, Long> projectStatistics = new HashMap<>();
        for (JobDescription jobDescription : data.getJobDescriptions()) {
            projectStatistics.put(jobDescription.getName(), jobDescription.getTotalTime(date));
        }
        return projectStatistics;
    }
}
