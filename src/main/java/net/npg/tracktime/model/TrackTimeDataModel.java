/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cymric
 */

public class TrackTimeDataModel {

    private List<JobDescriptionModel> jobDescriptions;

    private ObservableList<JobDescriptionModel> observableJobDescriptions;

    public TrackTimeDataModel() {
        jobDescriptions = new ArrayList<>();
    }

    public List<JobDescriptionModel> getJobDescriptions() {
        return jobDescriptions;
    }

    protected void setJobDescriptions(final List<JobDescriptionModel> jobDescriptions) {
        this.jobDescriptions = jobDescriptions;
    }

    public void addJobDescription(final JobDescriptionModel jobDescription) {
        observableJobDescriptions().add(jobDescription);
    }

    public ObservableList<JobDescriptionModel> observableJobDescriptions() {
        if (observableJobDescriptions == null) {
            observableJobDescriptions = FXCollections.observableList(jobDescriptions);
        }
        return observableJobDescriptions;
    }
}
