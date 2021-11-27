/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime.model;

import javafx.beans.property.*;

import java.util.*;

/**
 * @author Cymric
 */
public class JobDescriptionModel {

    private String project;
    private String job;
    private List<JobTimeModel> jobTimes;
    private final BooleanProperty active;
    private final LongProperty totalTime;
    private final StringProperty currentActivity;

    public JobDescriptionModel() {
        totalTime = new SimpleLongProperty();
        active = new SimpleBooleanProperty(Boolean.FALSE);
        currentActivity = new SimpleStringProperty();
    }

    public JobDescriptionModel(final String project, final String job) {
        this.project = project;
        this.job = job;
        jobTimes = new LinkedList<>();
        totalTime = new SimpleLongProperty();
        active = new SimpleBooleanProperty(Boolean.FALSE);
        currentActivity = new SimpleStringProperty();
    }

    public String getName() {
        return project + "/" + job;
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public StringProperty currentActivityProperty() {
        return currentActivity;
    }

    public void setActive(final Boolean active) {
        this.active.setValue(active);
    }

    public List<JobTimeModel> getJobTimes() {
        return jobTimes;
    }

    protected void setJobTimes(final List<JobTimeModel> jobTimes) {
        this.jobTimes = jobTimes;
        updateTimes();
    }

    public void addjobTime(final JobTimeModel jobTime) {
        jobTimes.add(jobTime);
    }

    public String getProject() {
        return project;
    }

    public void setProject(final String project) {
        this.project = project;
    }

    public String getJob() {
        return job;
    }

    public void setJob(final String job) {
        this.job = job;
    }

    public static final long MS_TO_MIN = 1000L * 60L;

    /**
     * get time spent in minutes
     *
     * @return
     */
    public Long getTotalTime() {
        return totalTime.getValue();
    }

    public Long getTotalTime(final Date date) {
        final Comparator<Date> dailyComparator = DateUtil.dailyComparator();
        long totalDayTime = 0;
        for (final JobTimeModel jobTime : getJobTimes()) {
            if (dailyComparator.compare(date, jobTime.getStartTime()) == 0) {
                totalDayTime += jobTime.getDuration();
            }
        }
        return totalDayTime;
    }

    public Collection<Date> getDays() {
        final Collection<Date> dates = DateUtil.dailySorted();
        for (final JobTimeModel jobTime : getJobTimes()) {
            dates.add(jobTime.getStartTime());
        }
        return dates;
    }

    public LongProperty totalTimeProperty() {
        return totalTime;
    }

    public void updateTimes() {
        Long time = 0L;
        for (final JobTimeModel jobTime : jobTimes) {
            time += jobTime.getDuration();
        }
        totalTime.setValue(time);
    }

    public void resetJobTimes() {
        jobTimes.clear();
        updateTimes();
    }
}
