/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.npg.tracktime.data.JobStorage;
import net.npg.tracktime.model.JobDescriptionModel;
import net.npg.tracktime.model.ModelConversion;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URL;

/**
 * @author Cymric
 */
public class TrackTimeUI extends Application {

    private ObservableList<JobDescriptionModel> data;
    @FXML
    Scene root;
    @FXML
    TableView tableView;
    private FXMLLoader fxmlLoader;


    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        final String[] args = getParameters().getRaw().toArray(new String[0]);

        this.applicationContext = new SpringApplicationBuilder()
                .sources(TrackTimeUI.class)
                .run(args);
    }


    @Override
    public void start(final Stage stage) throws Exception {
        final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        final URL location = classloader.getResource("TrackTime.fxml");
        fxmlLoader = new FXMLLoader(location);
        final AnchorPane anchorPane = fxmlLoader.load();
        root = new Scene(anchorPane);
        stage.setScene(root);

        stage.setOnHiding(event -> quit());

        stage.setTitle("Zeiterfassung");
        final var defaultData = ModelConversion.convert(JobStorage.readFromStorage());
        data = defaultData.observableJobDescriptions();

        ((TrackTimeController) fxmlLoader.getController()).setData(defaultData);

        tableView = (TableView) root.lookup("#tableView");
        if (tableView == null) {
            throw new RuntimeException("Tableview not found");
        }
        tableView.setPlaceholder(new Label("Bitte Projekt/Job hinzufügen .."));
        connectColumnWithProperty(tableView, "projectsColumn", "project");
        connectColumnWithProperty(tableView, "jobsColumn", "job");
        connectColumnWithProperty(tableView, "currentTimesColumn", "totalTime");
        final TableColumn startButtonsColumn = connectColumnWithProperty(tableView, "startButtonsColumn", "active");
        addStartButtonCellFactory(startButtonsColumn);

        final TableColumn currentActivityColumn = connectColumnWithProperty(tableView, "currentActivityColumn", "currentActivity");
        addActivityCellFactory(currentActivityColumn);
        tableView.setItems(data);
        //Enabling editing
        stage.show();
    }


    private static TableColumn getColumn(final TableView tableView, final String name) {
        for (final TableColumn column : (ObservableList<TableColumn>) tableView.getColumns()) {
            if (column.getId().equals(name)) {
                return column;
            }
        }
        throw new RuntimeException("Unknown column:" + name);
    }

    private void startTimer(final JobDescriptionModel job) {
        ((TrackTimeController) fxmlLoader.getController()).startTimer(job, "");
    }

    private void addActivity(final JobDescriptionModel job, final String newActivity) {
        ((TrackTimeController) fxmlLoader.getController()).addActivity(job, newActivity);
    }

    private void quit() {
        ((TrackTimeController) fxmlLoader.getController()).quitAction(null);
    }

    private void stopTimer(final JobDescriptionModel job) {
        ((TrackTimeController) fxmlLoader.getController()).stopTimer(job);
    }

    private static TableColumn connectColumnWithProperty(final TableView tableView, final String columnName, final String propertyName) {
        final TableColumn column = getColumn(tableView, columnName);
        column.setCellValueFactory(new PropertyValueFactory(propertyName));
        return column;
    }

    private void addStartButtonCellFactory(final TableColumn startButtonsColumn) {
        startButtonsColumn.setCellFactory(p -> new ButtonTableCell<>(new StartButtonHandler(data)));
    }

    private void addActivityCellFactory(final TableColumn currentActivityColumn) {
        currentActivityColumn.setCellFactory(p -> new TextFieldTableCell<>(new ActivityFieldHandler(data)));
    }

    private class ActivityFieldHandler implements TextFieldTableCell.TextAddEventHandler {

        private final ObservableList<JobDescriptionModel> data;

        public ActivityFieldHandler(final ObservableList<JobDescriptionModel> data) {
            this.data = data;
        }

        @Override
        public void addText(final int row, final String text) {
            addActivity(data.get(row), text);
        }
    }

    private class StartButtonHandler implements ButtonTableCell.ButtonPressedEventHandler {
        private final ObservableList<JobDescriptionModel> data;

        StartButtonHandler(final ObservableList<JobDescriptionModel> data) {
            this.data = data;
        }

        @Override
        public void stopAction(final int row) {
            stopTimer(data.get(row));
        }

        @Override
        public void startAction(final int row) {
            startTimer(data.get(row));
        }
    }
}
