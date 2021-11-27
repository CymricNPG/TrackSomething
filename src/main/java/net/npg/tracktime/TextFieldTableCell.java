/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.npg.tracktime;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * @author Cymric
 */
public class TextFieldTableCell<S> extends TableCell<S, String> {

    private final TextField textField;

    public TextFieldTableCell(final TextAddEventHandler textHandler) {
        this.textField = new TextField();
        textField.setStyle("-fx-base: red;");
        textField.setEditable(true);
        setAlignment(Pos.CENTER);
        setGraphic(textField);
        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(final ObservableValue<? extends Boolean> ov, final Boolean t, final Boolean t1) {
                if (!ov.getValue()) {
                    textField.setText("");
                }
            }
        });
        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent t) {
                if (t == null) {
                    return;
                }
                switch (t.getCode()) {
                    case ENTER:
                        if (textField.getText() == null || textField.getText().trim().length() == 0) {
                            break;
                        }
                        final int i = getIndex();
                        textHandler.addText(i, textField.getText());
                        textField.setText("");
                        break;
                    case ESCAPE:
                        textField.setText("");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void startEdit() {
        super.startEdit();
        setGraphic(textField);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        textField.selectAll();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(String.valueOf(getItem()));
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    private String getString() {
        return getItem() == null ? "" : getItem();
    }

    @Override
    public void updateItem(final String item, final boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(textField);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        } else {
            textField.setText(getString());
            setGraphic(textField);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    public interface TextAddEventHandler {

        void addText(int row, String text);
    }
}
