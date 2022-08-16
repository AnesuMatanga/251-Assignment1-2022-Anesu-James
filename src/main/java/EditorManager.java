import javax.swing.text.JTextComponent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EditorManager {
    private final JTextComponent textComponent;

    EditorManager(JTextComponent textComponent) {
        this.textComponent = textComponent;
    }

    /**
     * Deletes the current text
     * This is not directly in the lambda expression of gui as behaviour may want ot change in the future.
     */
    public void delete() {
        textComponent.replaceSelection("");
    }

    /**
     * Method that will add the date and time to the top of the text component
     */
    public void addDateAndTime() {
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String currentDateTime = dateTimeFormat.format(LocalDateTime.now());
        String currentText = textComponent.getText();
        currentText = currentDateTime + "\n" + currentText;
        textComponent.setText(currentText);
    }
}
