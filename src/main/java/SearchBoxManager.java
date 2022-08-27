import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchBoxManager implements DocumentListener, ActionListener {
    //Variables Initialising
    protected JTextComponent content;
    protected Matcher matchWord;

    //Constructor which accepts JTextComponent as the target
    public SearchBoxManager(JTextComponent component){
        this.content = component;
    }

    /**
     * Implementing DocumentListener which will call startSearching()
     * method which will start the searching
     */
    public void insertUpdate(DocumentEvent event){
        startSearch(event.getDocument());
    }
    public void removeUpdate(DocumentEvent event){
        startSearch(event.getDocument());
    }

    /**
     * Gives notification that an attribute or set of attributes changed.
     *
     * @param e the document event
     */
    @Override
    public void changedUpdate(DocumentEvent e) {

    }

    /**
     * startSearch() Method which takes the search text from JTextField
     *
     */
    private void startSearch(Document searchText_doc) {
        try {
            String searchText = searchText_doc.getText(0, searchText_doc.getLength());

            //Creating new pattern from the searchText typed
            Pattern pattern = Pattern.compile(searchText);

            //retrieving the required document and the text
            Document content_doc = content.getDocument();
            String body = content_doc.getText(0, content_doc.getLength());

            //creating a Matcher so the Matcher class does the searching
            matchWord = pattern.matcher(body);
            keepSearching();
        } catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error when searching please try again.");
        }
    }

    /**
     *keepSearching() method to call find() (on Matcher) to look for
     * a match
     */
    private void keepSearching() {
        if (matchWord != null && matchWord.find()) {
            //moveDot and setDot to select the matched word
            content.getCaret().setDot(matchWord.start());
            content.getCaret().moveDot(matchWord.end());
            content.getCaret().setSelectionVisible(true);
        }
    }

    /**
     * ActionListener Implementation
     * ActionListener to call keepSearching whenever an action is performed
     * The actionPerformed will call continueSearch() Method and since the
     * word has already been matched, the use can continue clicking Enter
     * to go through the document
     */
    public void actionPerformed(ActionEvent event){
        keepSearching();
    }
}














