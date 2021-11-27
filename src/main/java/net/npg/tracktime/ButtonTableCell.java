/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import org.apache.commons.lang3.BooleanUtils;

/**
 * @author Cymric
 */
public class ButtonTableCell<S> extends TableCell<S, Boolean> {

    private static final String STOP_BUTTON_NAME = "Stop";
    private static final String START_BUTTON_NAME = "Start";
    private final Button button;

    ButtonTableCell(final ButtonPressedEventHandler buttonPressedHandler) {
        button = new Button();
        button.setAlignment(Pos.CENTER);
        setAlignment(Pos.CENTER);
        changeToStartButton();
        button.setOnAction(t -> {
            if (STOP_BUTTON_NAME.equals(button.getText())) {
                buttonPressedHandler.stopAction(getIndex());
            } else {
                buttonPressedHandler.startAction(getIndex());
            }
        });
    }

    @Override
    public void updateItem(final Boolean item, final boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            cleanCell();
        } else {
            if (BooleanUtils.isTrue(item)) {
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
