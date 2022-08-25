import org.junit.Test;

import javax.swing.*;
import javax.swing.text.*;

import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;

public class SearchBoxManagerTest {

    @Test
    public void startSearch() {
        JTextComponent textArea = new JTextArea("This is a test \nI am going to be searching for" +
                "test. The search function should highlight them, I repeat that is \"test\" I am searching for.");
        SearchBoxManager searchBoxManager = new SearchBoxManager(textArea);

        JTextField searchBox = new JTextField();
        searchBox.getDocument().addDocumentListener(searchBoxManager);
        searchBox.setText("test");

        assertEquals(10, textArea.getSelectionStart());
        assertEquals(14, textArea.getSelectionEnd());
        assertEquals("test", textArea.getSelectedText());
    }

    @Test
    public void keepSearching() {
        JTextComponent textArea = new JTextArea("This is a test \nI am going to be searching for" +
                "test. The search function should highlight them, I repeat that is \"test\" I am searching for.");
        SearchBoxManager searchBoxManager = new SearchBoxManager(textArea);

        JTextField searchBox = new JTextField();
        searchBox.getDocument().addDocumentListener(searchBoxManager);
        searchBox.setText("test");

        searchBoxManager.actionPerformed(new ActionEvent(searchBox, ActionEvent.ACTION_PERFORMED, null));

        assertEquals(46, textArea.getSelectionStart());
        assertEquals(50, textArea.getSelectionEnd());
        assertEquals("test", textArea.getSelectedText());

        searchBoxManager.actionPerformed(new ActionEvent(searchBox, ActionEvent.ACTION_PERFORMED, null));

        assertEquals(113, textArea.getSelectionStart());
        assertEquals(117, textArea.getSelectionEnd());
        assertEquals("test", textArea.getSelectedText());
    }
}