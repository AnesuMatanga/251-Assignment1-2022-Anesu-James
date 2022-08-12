import javax.swing.text.JTextComponent;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class EditorManager {
    private JTextComponent textComponent;
    private Clipboard clipboard;

    EditorManager(JTextComponent textComponent) {
        this.textComponent = textComponent;
        this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    /**
     * Copy function that takes selected text in the text area and places it in the clipboard
     */
    public void copy() {
        String selectedText = textComponent.getSelectedText();
        StringSelection stringSelection = new StringSelection(selectedText);
        clipboard.setContents(stringSelection, null);
    }

    /**
     * Cut function that takes selected text. Removes it from the text area and places it in the clipboard
     */
    public void cut() {
        this.copy();
        this.delete();
    }

    /**
     * Pastes current clipboard content at current caret position
     */
    public void paste() {
        String clipboardContent = "";
        try {
            clipboardContent = (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
        int caretPosition = textComponent.getCaretPosition();
        String newText = new StringBuilder(textComponent.getText()).insert(caretPosition, clipboardContent).toString();
        textComponent.setText(newText);
        textComponent.setCaretPosition(caretPosition + clipboardContent.length());
    }

    /**
     * Deletes the current text
     */
    public void delete() {
        textComponent.replaceSelection("");
    }
}
