/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

/**
 * @author Cymric
 */
public class ButtonTableCell<S> extends TableCell<S, Boolean> {

    public static final String STOP_BUTTON_NAME = "Stop";
    public static final String START_BUTTON_NAME = "Start";
    private final Button button;

    public ButtonTableCell(final ButtonPressedEventHandler buttonPressedHandler) {
        this.button = new Button();
        this.button.setAlignment(Pos.CENTER);
        setAlignment(Pos.CENTER);
        changeToStartButton();
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent t) {
                final int i = getIndex();
                if (STOP_BUTTON_NAME.equals(button.getText())) {
                    buttonPressedHandler.stopAction(i);
                } else {
                    buttonPressedHandler.startAction(i);
                }
            }
        });
    }

    @Override
    public void updateItem(final Boolean item, final boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            cleanCell();
        } else {
            if (item) {
                changeToStopButton();
            } else {
                changeToStartButton();
            }
        }
    }

    private void cleanCell() {
        setText(null);
        setGraphic(null);
    }

    private void changeToStopButton() {
        button.setStyle("-fx-base: red;");
        button.setText(STOP_BUTTON_NAME);
        setGraphic(button);
    }

    private void changeToStartButton() {
        button.setStyle("-fx-base: green;");
        button.setText(START_BUTTON_NAME);
        setGraphic(button);
    }

    public interface ButtonPressedEventHandler {

        void stopAction(int row);

        void startAction(int row);
    }
}
