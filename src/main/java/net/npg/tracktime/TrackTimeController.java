/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.npg.tracktime.data.JobStorage;
import net.npg.tracktime.model.JobDescriptionModel;
import net.npg.tracktime.model.JobTimeModel;
import net.npg.tracktime.model.ModelConversion;
import net.npg.tracktime.model.TrackTimeDataModel;
import net.npg.tracktime.statistics.StatisticsCreator;
import net.npg.tracktime.statistics.StatisticsView;

import javax.swing.*;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * @author Cymric
 */
public class TrackTimeController implements Initializable {

    @FXML
    private Label currentJobLabel; // todo remove this
    @FXML
    private TextField projectField;
    @FXML
    private TextField jobField;
    private JobTimeModel currentJobTime;
    private JobDescriptionModel currentJob;
    private TrackTimeDataModel data;

    @FXML
    public void quitAction(final ActionEvent event) {
        stopAction(event);
        saveAction(event);
        System.exit(1);
    }

    @FXML
    public void statisticsAction(final ActionEvent event) throws Exception {
        final Stage stage = new Stage();
        final StatisticsView statisticsView = new StatisticsView(StatisticsCreator.create(data));
        statisticsView.start(stage);
    }

    @FXML
    public void saveAction(final ActionEvent event) {
        try {
            JobStorage.writeToStorage(ModelConversion.convert(data));
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }


    @FXML
    public void resetAction(final ActionEvent event) {
        final int showConfirmDialog = JOptionPane.showConfirmDialog(null, "Daten zurücksetzen?");
        if (showConfirmDialog != JOptionPane.OK_OPTION) {
            return;
        }
        stopAction(event);
        for (final JobDescriptionModel job : data.getJobDescriptions()) {
            job.resetJobTimes();
        }
    }

    @FXML
    public void addJobAction(final ActionEvent event) {
        if (isFieldEmpty(projectField) || isFieldEmpty(jobField)) {
            System.out.println("Fields are empty!");
            return;
        }
        final String projectText = projectField.getText();
        final String jobText = jobField.getText();
        if (alreadyExists(projectText, jobText)) {
            System.out.println("Fields already exists!");
            return;
        }
        data.addJobDescription(new JobDescriptionModel(projectText, jobText));
        projectField.setText("");
        jobField.setText("");
    }

    @FXML
    private void stopAction(final ActionEvent event) {
        if (currentJobTime == null) {
            return;
        }
        currentJobLabel.setText("");
        currentJobTime.setEndTime(new Date());
        currentJobTime = null;
        currentJob.currentActivityProperty().set("");
        currentJob.updateTimes();
        currentJob.setActive(Boolean.FALSE);
        currentJob = null;
    }

    void startTimer(final JobDescriptionModel job, final String activityName) {
        closeCurrentJobTime();
        currentJob = job;
        currentJob.setActive(Boolean.TRUE);
        currentJobLabel.setText(job.getProject() + "/" + job.getJob() + ":" + activityName);
        currentJobTime = new JobTimeModel(activityName);
        job.addjobTime(currentJobTime);
    }

    @Override
    public void initialize(final URL url, final ResourceBundle rb) {
        //TODO
    }

    void setData(final TrackTimeDataModel data) {
        this.data = data;
    }

    private static boolean isFieldEmpty(final TextField field) {
        return field == null || field.getText() == null || field.getText().trim().isEmpty();
    }

    private boolean alreadyExists(final String projectText, final String jobText) {
        for (final JobDescriptionModel job : data.getJobDescriptions()) {
            if (job.getJob().equals(jobText) && job.getProject().equals(projectText)) {
                return true;
            }
        }
        return false;
    }

    void stopTimer(final JobDescriptionModel job) {
        closeCurrentJobTime();
    }

    void addActivity(final JobDescriptionModel job, final String newActivityDescription) {
        System.out.println("Current Activity:" + job.currentActivityProperty().getValue());
        closeCurrentJobTime();
        startTimer(job, newActivityDescription);
        currentJob.currentActivityProperty().setValue("");
    }

    private void closeCurrentJobTime() {
        if (currentJobTime != null) {
            stopAction(null);
        }
    }
}
