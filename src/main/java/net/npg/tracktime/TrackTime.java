/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime;

import com.google.common.io.Resources;
import net.npg.tracktime.data.JobDescription;
import net.npg.tracktime.data.TrackTimeData;
import net.npg.tracktime.data.JobStorage;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.net.URI;
import java.net.URL;

/**
 *
 * @author Cymric
 */
public class TrackTime extends Application {

    /**
     * The main() method is ignored in correctly deployed JavaFX application. main() serves only as fallback in case the
     * application can not be launched through deployment artifacts, e.g., in IDEs with limited FX support. NetBeans
     * ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    static public ObservableList<JobDescription> data;
    @FXML
    Scene root;
    @FXML
    TableView tableView;
    private FXMLLoader fxmlLoader;

    @Override
    public void start(final Stage stage) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL location = classloader.getResource("TrackTime.fxml");
        fxmlLoader = new FXMLLoader(location);
        AnchorPane anchorPane = (AnchorPane) fxmlLoader.load();
        root = new Scene(anchorPane);
        stage.setScene(root);

        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                quit();
            }
        });

        stage.setTitle("Zeiterfassung");
        TrackTimeData defaultData = JobStorage.readFromStorage();
        data = defaultData.observableJobDescriptions();

        ((TrackTimeController) fxmlLoader.getController()).setData(defaultData);

        //  tableView =                 lookup(parentNode,"#ScrollJobs",ScrollPane.class);
        tableView = (TableView) root.lookup("#tableView");
        if (tableView == null) {
            throw new RuntimeException("Tableview not found");
        }
        tableView.setPlaceholder(new Label("Bitte Projekt/Job hinzufügen .."));
        connectColumnWithProperty(tableView, "projectsColumn", "project");
        connectColumnWithProperty(tableView, "jobsColumn", "job");
        connectColumnWithProperty(tableView, "currentTimesColumn", "totalTime");
        TableColumn startButtonsColumn = connectColumnWithProperty(tableView, "startButtonsColumn", "active");
        addStartButtonCellFactory(startButtonsColumn);

        TableColumn currentActivityColumn = connectColumnWithProperty(tableView, "currentActivityColumn", "currentActivity");
        addActivityCellFactory(currentActivityColumn);
        tableView.setItems(data);
        //Enabling editing
        stage.show();
    }

    private TableCell<JobDescription, Boolean> createButtonCell() {
        return new ButtonTableCell<>(new StartButtonHandler());
    }

    private TableColumn getColumn(TableView tableView, String name) {
        for (TableColumn column : (ObservableList<TableColumn>) tableView.getColumns()) {
            if (column.getId().equals(name)) {
                return column;
            }
        }
        throw new RuntimeException("Unknown column:" + name);
    }

    public void startTimer(JobDescription job) {
        ((TrackTimeController) fxmlLoader.getController()).startTimer(job, "");
    }

    public void addActivity(JobDescription job, String newActivity) {
        ((TrackTimeController) fxmlLoader.getController()).addActivity(job, newActivity);
    }

    public void quit() {
        ((TrackTimeController) fxmlLoader.getController()).quitAction(null);
    }

    public void stopTimer(JobDescription job) {
        ((TrackTimeController) fxmlLoader.getController()).stopTimer(job);
    }

    private TableColumn connectColumnWithProperty(TableView tableView, String columnName, String propertyName) {
        TableColumn column = getColumn(tableView, columnName);
        column.setCellValueFactory(new PropertyValueFactory(propertyName));
        return column;
    }

    private void addStartButtonCellFactory(TableColumn startButtonsColumn) {
        startButtonsColumn.setCellFactory(new Callback<TableColumn<JobDescription, Boolean>, TableCell<JobDescription, Boolean>>() {
            @Override
            public TableCell<JobDescription, Boolean> call(TableColumn<JobDescription, Boolean> p) {
                return createButtonCell();
            }
        });
    }

    private void addActivityCellFactory(TableColumn currentActivityColumn) {
        currentActivityColumn.setCellFactory(new Callback<TableColumn<JobDescription, String>, TableCell<JobDescription, String>>() {
            @Override
            public TableCell<JobDescription, String> call(TableColumn<JobDescription, String> p) {
                return createActivityCell();
            }
        });
    }

    private TableCell<JobDescription, String> createActivityCell() {
        return new TextFieldTableCell<>(new ActivityFieldHandler());
    }

    private class ActivityFieldHandler implements TextFieldTableCell.TextAddEventHandler {

        @Override
        public void addText(int row, String text) {
            addActivity(TrackTime.data.get(row), text);
        }
    }

    private class StartButtonHandler implements ButtonTableCell.ButtonPressedEventHandler {

        @Override
        public void stopAction(int row) {
            stopTimer(TrackTime.data.get(row));
        }

        @Override
        public void startAction(int row) {
            startTimer(TrackTime.data.get(row));
        }
    }
}
