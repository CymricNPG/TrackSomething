/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime.data;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Cymric
 */
@XmlRootElement()
@XmlAccessorType(XmlAccessType.PROPERTY)
public class JobDescription {

    private String project;
    private String job;
    private List<JobTime> jobTimes;
    private BooleanProperty active;
    private LongProperty totalTime;
    private StringProperty currentActivity;

    JobDescription() {
        totalTime = new SimpleLongProperty();
        active = new SimpleBooleanProperty(Boolean.FALSE);
        currentActivity = new SimpleStringProperty();
    }

    public JobDescription(String project, String job) {
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

    public void setActive(Boolean active) {
        this.active.setValue(active);
    }

    public List<JobTime> getJobTimes() {
        return jobTimes;
    }

    @XmlElementWrapper(name = "jobTimes")
    @XmlElement(name = "JobTime")
    protected void setJobTimes(List<JobTime> jobTimes) {
        this.jobTimes = jobTimes;
        updateTimes();
    }

    public void addjobTime(JobTime jobTime) {
        jobTimes.add(jobTime);
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
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

    public Long getTotalTime(Date date) {
        Comparator<Date> dailyComparator = DateUtil.dailyComparator();
        long totalDayTime = 0;
        for (JobTime jobTime : getJobTimes()) {
            if (dailyComparator.compare(date, jobTime.getStartTime()) == 0) {
                totalDayTime += jobTime.getDuration();
            }
        }
        return totalDayTime;
    }

    public Collection<Date> getDays() {
        Collection<Date> dates = DateUtil.dailySorted();
        for (JobTime jobTime : getJobTimes()) {
            dates.add(jobTime.getStartTime());
        }
        return dates;
    }

    public LongProperty totalTimeProperty() {
        return totalTime;
    }

    public void updateTimes() {
        Long time = 0L;
        for (JobTime jobTime : jobTimes) {
            time += jobTime.getDuration();
        }
        totalTime.setValue(time);
    }

    public void resetJobTimes() {
        jobTimes.clear();
        updateTimes();
    }
}
