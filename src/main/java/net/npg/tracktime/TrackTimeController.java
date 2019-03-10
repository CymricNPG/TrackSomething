/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime;

import net.npg.tracktime.data.JobTime;
import net.npg.tracktime.data.JobDescription;
import net.npg.tracktime.data.TrackTimeData;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import net.npg.tracktime.data.JobStorage;
import net.npg.tracktime.statistics.StatisticsView;
import net.npg.tracktime.statistics.StatisticsCreator;

/**
 *
 * @author Cymric
 */
public class TrackTimeController implements Initializable {

    @FXML
    private Label currentJobLabel; // todo remove this
    @FXML
    private TextField projectField;
    @FXML
    private TextField jobField;
    private JobTime currentJobTime;
    private JobDescription currentJob;
    private TrackTimeData data;

    @FXML
    public void quitAction(ActionEvent event) {
        stopAction(event);
        saveAction(event);
        System.err.println("Servus");
        System.exit(1);
    }

    @FXML
    public void statisticsAction(ActionEvent event) throws Exception {
        Stage stage = new Stage();
        StatisticsView statisticsView = new StatisticsView(StatisticsCreator.create(data));
        statisticsView.start(stage);
    }

    @FXML
    public void saveAction(ActionEvent event) {
        try {
            JobStorage.writeToStorage(data);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void resetAction(ActionEvent event) {
        int showConfirmDialog = JOptionPane.showConfirmDialog(null, "Daten zurücksetzen?");
        if (showConfirmDialog != JOptionPane.OK_OPTION) {
            return;
        }
        stopAction(event);
        for (JobDescription job : data.getJobDescriptions()) {
            job.resetJobTimes();
        }
    }

    @FXML
    public void addJobAction(ActionEvent event) {
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
        data.addJobDescription(new JobDescription(projectText, jobText));
        projectField.setText("");
        jobField.setText("");
    }

    @FXML
    private void stopAction(ActionEvent event) {
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

    void startTimer(JobDescription job, String activityName) {
        closeCurrentJobTime();
        currentJob = job;
        currentJob.setActive(Boolean.TRUE);
        currentJobLabel.setText(job.getProject() + "/" + job.getJob() + ":" + activityName);
        currentJobTime = new JobTime(activityName);
        job.addjobTime(currentJobTime);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }

    void setData(TrackTimeData data) {
        this.data = data;
    }

    private boolean isFieldEmpty(TextField field) {
        return field == null || field.getText() == null || field.getText().trim().isEmpty();
    }

    private boolean alreadyExists(String projectText, String jobText) {
        for (JobDescription job : data.getJobDescriptions()) {
            if (job.getJob().equals(jobText) && job.getProject().equals(projectText)) {
                return true;
            }
        }
        return false;
    }

    void stopTimer(JobDescription job) {
        closeCurrentJobTime();
    }

    void addActivity(JobDescription job, String newActivityDescription) {
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
