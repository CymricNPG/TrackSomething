/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime;

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
import net.npg.tracktime.data.JobDescription;
import net.npg.tracktime.data.JobStorage;
import net.npg.tracktime.data.TrackTimeData;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URL;

/**
 * @author Cymric
 */
public class TrackTimeUI extends Application {

    static public ObservableList<JobDescription> data;
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

        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(final WindowEvent event) {
                quit();
            }
        });

        stage.setTitle("Zeiterfassung");
        final TrackTimeData defaultData = JobStorage.readFromStorage();
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
        final TableColumn startButtonsColumn = connectColumnWithProperty(tableView, "startButtonsColumn", "active");
        addStartButtonCellFactory(startButtonsColumn);

        final TableColumn currentActivityColumn = connectColumnWithProperty(tableView, "currentActivityColumn", "currentActivity");
        addActivityCellFactory(currentActivityColumn);
        tableView.setItems(data);
        //Enabling editing
        stage.show();
    }

    private TableCell<JobDescription, Boolean> createButtonCell() {
        return new ButtonTableCell<>(new StartButtonHandler());
    }

    private static TableColumn getColumn(final TableView tableView, final String name) {
        for (final TableColumn column : (ObservableList<TableColumn>) tableView.getColumns()) {
            if (column.getId().equals(name)) {
                return column;
            }
        }
        throw new RuntimeException("Unknown column:" + name);
    }

    public void startTimer(final JobDescription job) {
        ((TrackTimeController) fxmlLoader.getController()).startTimer(job, "");
    }

    public void addActivity(final JobDescription job, final String newActivity) {
        ((TrackTimeController) fxmlLoader.getController()).addActivity(job, newActivity);
    }

    public void quit() {
        ((TrackTimeController) fxmlLoader.getController()).quitAction(null);
    }

    public void stopTimer(final JobDescription job) {
        ((TrackTimeController) fxmlLoader.getController()).stopTimer(job);
    }

    private static TableColumn connectColumnWithProperty(final TableView tableView, final String columnName, final String propertyName) {
        final TableColumn column = getColumn(tableView, columnName);
        column.setCellValueFactory(new PropertyValueFactory(propertyName));
        return column;
    }

    private void addStartButtonCellFactory(final TableColumn startButtonsColumn) {
        startButtonsColumn.setCellFactory(new Callback<TableColumn<JobDescription, Boolean>, TableCell<JobDescription, Boolean>>() {
            @Override
            public TableCell<JobDescription, Boolean> call(final TableColumn<JobDescription, Boolean> p) {
                return createButtonCell();
            }
        });
    }

    private void addActivityCellFactory(final TableColumn currentActivityColumn) {
        currentActivityColumn.setCellFactory(new Callback<TableColumn<JobDescription, String>, TableCell<JobDescription, String>>() {
            @Override
            public TableCell<JobDescription, String> call(final TableColumn<JobDescription, String> p) {
                return createActivityCell();
            }
        });
    }

    private TableCell<JobDescription, String> createActivityCell() {
        return new TextFieldTableCell<>(new ActivityFieldHandler());
    }

    private class ActivityFieldHandler implements TextFieldTableCell.TextAddEventHandler {

        @Override
        public void addText(final int row, final String text) {
            addActivity(TrackTimeUI.data.get(row), text);
        }
    }

    private class StartButtonHandler implements ButtonTableCell.ButtonPressedEventHandler {

        @Override
        public void stopAction(final int row) {
            stopTimer(TrackTimeUI.data.get(row));
        }

        @Override
        public void startAction(final int row) {
            startTimer(TrackTimeUI.data.get(row));
        }
    }
}
