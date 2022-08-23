import org.junit.jupiter.api.Test;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class EditorManagerTest {

    @Test
    void delete() {
        JTextComponent textArea = new JTextArea();
        EditorManager editorManager = new EditorManager(textArea);
        textArea.setText("This is a test of the delete method.");
        textArea.setSelectionStart(8);
        textArea.setSelectionEnd(18);
        editorManager.delete();
        assertEquals("This is the delete method.", textArea.getText());
    }

    @Test
    void addDateAndTime() {
        JTextComponent textArea = new JTextArea();
        EditorManager editorManager = new EditorManager(textArea);
        textArea.setText("This is a test of the addDate and time method.");
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String currentDateTime = dateTimeFormat.format(LocalDateTime.now());
        String currentText = textArea.getText();
        String textWithDate = currentDateTime + "\n" + currentText;
        editorManager.addDateAndTime();
        assertEquals(textWithDate, textArea.getText());
    }
}