/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime.model;


import java.util.Date;

/**
 * @author Cymric
 */

public class JobTimeModel {

    private Date startTime;
    private Date endTime;
    private String activity;

    public JobTimeModel() {
    }

    public JobTimeModel(final Date startTime, final Date endTime, final String activity) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.activity = activity;
    }

    public JobTimeModel(final String activity) {
        this.activity = activity;
        startTime = new Date();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(final Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(final Date endTime) {
        this.endTime = endTime;
    }

    public String getActivity() {
        return activity;
    }

    public long getDuration() {
        if (endTime == null || startTime == null) {
            return 0;
        }
        return (endTime.getTime() - startTime.getTime()) / JobDescriptionModel.MS_TO_MIN;
    }

    public void setActivity(final String activity) {
        this.activity = activity;
    }
}
