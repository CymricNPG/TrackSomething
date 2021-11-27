/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cymric
 */

public class TrackTimeData {

    private List<JobDescription> jobDescriptions;

    private ObservableList<JobDescription> observableJobDescriptions;

    public TrackTimeData() {
        jobDescriptions = new ArrayList<>();
    }

    public List<JobDescription> getJobDescriptions() {
        return jobDescriptions;
    }

    protected void setJobDescriptions(final List<JobDescription> jobDescriptions) {
        this.jobDescriptions = jobDescriptions;
    }

    public void addJobDescription(final JobDescription jobDescription) {
        observableJobDescriptions().add(jobDescription);
    }

    public ObservableList<JobDescription> observableJobDescriptions() {
        if (observableJobDescriptions == null) {
            observableJobDescriptions = FXCollections.observableList(jobDescriptions);
        }
        return observableJobDescriptions;
    }
}
