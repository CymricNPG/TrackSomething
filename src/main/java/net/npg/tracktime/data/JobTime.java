/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime.data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author Cymric
 */
@XmlRootElement()
public class JobTime {

    private Date startTime;
    private Date endTime;
    private String activity;

    public JobTime() {
    }

    public JobTime(final String activity) {
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
        return (endTime.getTime() - startTime.getTime()) / JobDescription.MS_TO_MIN;
    }

    public void setActivity(final String activity) {
        this.activity = activity;
    }
}
