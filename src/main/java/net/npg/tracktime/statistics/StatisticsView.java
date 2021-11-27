/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime.statistics;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.npg.tracktime.model.DateUtil;
import org.apache.commons.lang3.StringUtils;

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

    public StatisticsView(final StatisticsData statistics) {
        this.statistics = statistics;
    }

    @Override
    public void start(final Stage stage) throws Exception {
        final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        final URL location = classloader.getResource("Statistics.fxml");
        fxmlLoader = new FXMLLoader(location);
        final AnchorPane anchorPane = fxmlLoader.load();
        root = new Scene(anchorPane);
        stage.setScene(root);

        stage.setOnHiding(event -> {
            //closed
        });

        stage.setTitle("Zeitübersicht");

        tableView = (TableView) root.lookup("#tableView");
        if (tableView == null) {
            throw new RuntimeException("Tableview not found");
        }

        populateTable(tableView, statistics);

        stage.show();
    }

    private static void populateTable(
            final TableView<ObservableList<StringProperty>> table,
            final StatisticsData data) {
        table.getItems().clear();
        table.getColumns().clear();
        final List<Date> headerValues = data.getDays();
        final Collection<String> rowValues = data.getProjects();

        int pos = 0;
        table.getColumns().add(createColumn(pos, "Datum", 140.0));
        for (final Date header : headerValues) {
            pos++;
            table.getColumns().add(createColumn(pos, DateUtil.shortDate(header), 75.0));
        }

        for (final String project : rowValues) {
            final ObservableList<StringProperty> row = FXCollections
                    .observableArrayList();
            row.add(new SimpleStringProperty(project));
            for (final Date day : headerValues) {
                final Long value = data.getDay(day).getProjectStatistics().get(project);
                row.add(new SimpleStringProperty(value.toString()));
            }
            table.getItems().add(row);
        }
    }

    private static TableColumn<ObservableList<StringProperty>, String> createColumn(
            final int columnIndex, final String columnTitle, final double width) {
        final TableColumn<ObservableList<StringProperty>, String> column = new TableColumn();
        final String title;
        if (StringUtils.isEmpty(columnTitle)) {
            title = "Column " + (columnIndex + 1);
        } else {
            title = columnTitle;
        }
        column.setText(title);
        column.setPrefWidth(width);
        column.setCellValueFactory(cellDataFeatures -> {
            final ObservableList<StringProperty> values = cellDataFeatures.getValue();
            if (columnIndex >= values.size()) {
                return new SimpleStringProperty("");
            } else {
                return cellDataFeatures.getValue().get(columnIndex);
            }
        });
        return column;
    }
}
