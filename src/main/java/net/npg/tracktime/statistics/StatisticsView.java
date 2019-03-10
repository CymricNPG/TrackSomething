/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime.statistics;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import net.npg.tracktime.data.DateUtil;

import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Cymric
 */
public class StatisticsView extends Application {

    private FXMLLoader fxmlLoader;
    private Scene root;
    private TableView tableView;
    private final StatisticsData statistics;

    public StatisticsView(StatisticsData statistics) {
        this.statistics = statistics;
    }

    @Override
    public void start(Stage stage) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL location = classloader.getResource("Statistics.fxml");
        fxmlLoader = new FXMLLoader(location);
        AnchorPane anchorPane = (AnchorPane) fxmlLoader.load();
        root = new Scene(anchorPane);
        stage.setScene(root);

        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //closed
            }
        });

        stage.setTitle("Zeitübersicht");

        tableView = (TableView) root.lookup("#tableView");
        if (tableView == null) {
            throw new RuntimeException("Tableview not found");
        }

        populateTable(tableView, statistics);

        stage.show();
    }

    private void populateTable(
            final TableView<ObservableList<StringProperty>> table,
            StatisticsData data) {
        table.getItems().clear();
        table.getColumns().clear();
        final List<Date> headerValues = data.getDays();
        final Collection<String> rowValues = data.getProjects();

        int pos = 0;
        table.getColumns().add(createColumn(pos, "Datum", 140.0));
        for (Date header : headerValues) {
            pos++;
            table.getColumns().add(createColumn(pos, DateUtil.shortDate(header), 75.0));
        }

        for (String project : rowValues) {
            ObservableList<StringProperty> row = FXCollections
                    .observableArrayList();
            row.add(new SimpleStringProperty(project));
            for (Date day : headerValues) {
                Long value = data.getDay(day).getProjectStatistics().get(project);
                row.add(new SimpleStringProperty(value.toString()));
            }
            table.getItems().add(row);
        }
    }

    private TableColumn<ObservableList<StringProperty>, String> createColumn(
            final int columnIndex, String columnTitle, double width) {
        TableColumn<ObservableList<StringProperty>, String> column = new TableColumn();
        String title;
        if (columnTitle == null || columnTitle.trim().length() == 0) {
            title = "Column " + (columnIndex + 1);
        } else {
            title = columnTitle;
        }
        column.setText(title);
        column.setPrefWidth(width);
        column
                .setCellValueFactory(new Callback<CellDataFeatures<ObservableList<StringProperty>, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(
                            CellDataFeatures<ObservableList<StringProperty>, String> cellDataFeatures) {
                        ObservableList<StringProperty> values = cellDataFeatures.getValue();
                        if (columnIndex >= values.size()) {
                            return new SimpleStringProperty("");
                        } else {
                            return cellDataFeatures.getValue().get(columnIndex);
                        }
                    }
                });
        return column;
    }
}
