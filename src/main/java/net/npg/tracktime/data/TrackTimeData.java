/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime.data;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Cymric
 */
@XmlRootElement(namespace = "net.npg.tracktime")
public class TrackTimeData {

    @XmlElementWrapper(name = "jobDescriptions")
    @XmlElement(name = "JobDescription")
    private List<JobDescription> jobDescriptions;
    
    private ObservableList<JobDescription> observableJobDescriptions;

    public TrackTimeData() {
        jobDescriptions = new ArrayList<>();
    }

    public List<JobDescription> getJobDescriptions() {
        return jobDescriptions;
    }

    protected void setJobDescriptions(List<JobDescription> jobDescriptions) {
        this.jobDescriptions = jobDescriptions;
    }

    public void addJobDescription(JobDescription jobDescription) {
        observableJobDescriptions().add(jobDescription);
    }

    public ObservableList<JobDescription> observableJobDescriptions() {
        if (observableJobDescriptions == null) {
            observableJobDescriptions = FXCollections.observableList(jobDescriptions);
        }
        return observableJobDescriptions;
    }
}
