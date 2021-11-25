/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime.statistics;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Cymric
 */
class DayStatisticsData {

    private Map<String, Long> projectStatistics;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public DayStatisticsData(final Date date) {
        this.projectStatistics = new HashMap<>();
        this.date = date;
    }

    public Map<String, Long> getProjectStatistics() {
        return projectStatistics;
    }

    public void setProjectStatistics(final Map<String, Long> projectStatistics) {
        this.projectStatistics = projectStatistics;
    }
}
