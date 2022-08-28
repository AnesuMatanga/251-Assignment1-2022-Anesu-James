import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.io.FilenameUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.odftoolkit.odfdom.pkg.OdfElement;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import javax.swing.text.rtf.RTFEditorKit;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.print.PrinterException;
import java.io.*;

public class FileManager {
    //File path should always be kept in absolute form
    private String currentFilePath = null;
    private boolean isSaved = true;
    //Text component that will be saved from and written to.
    private final JTextComponent textComponent;
    private final TextAreaListener textAreaListener;

    private boolean codeFormatting = false;


    /**
     * Constructor to load the field
     * @param textComponent The component where files will be saved and written to
     */
    public FileManager(RSyntaxTextArea textComponent) {
        this.textComponent = textComponent;
        this.textAreaListener = new TextAreaListener();
        this.textComponent.getDocument().addDocumentListener(this.getTextAreaListener());
        this.newFile();
    }

    /**
     * Method that loads the window listener for the frame to do on close save warning.
     */
    public void setFrame() {
        JFrame frame = (JFrame) SwingUtilities.windowForComponent(textComponent);
        frame.addWindowListener(new WindowCloseListener(frame));
    }

    /**
     * Save method will take contents from JTextComponent and save it to the currentFilePath.
     * It is private and can only be accessed by the save and save as Methods.
     */
    private void save(Boolean saveAs) {
        File fileToSave;
        if (currentFilePath == null || saveAs) {
            JFileChooser fileChooser = new JFileChooser();
            int fileChooserResult = fileChooser.showSaveDialog(textComponent);
            if (fileChooserResult != JFileChooser.APPROVE_OPTION) return;
            fileToSave = fileChooser.getSelectedFile();
        } else {
            fileToSave = new File(currentFilePath);
        }

        try {
            if ("pdf".equals(FilenameUtils.getExtension(fileToSave.getAbsolutePath()))) {
                Document doc = new Document();
                PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(fileToSave));
                Font font = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.BLACK);
                doc.open();
                if (!textComponent.getText().equals("")) {
                    Paragraph para = new Paragraph(textComponent.getText(), font);
                    doc.add(para);
                }
                doc.close();
                writer.close();
            } else { //Assuming file is txt format
                currentFilePath = fileToSave.getAbsolutePath();
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileToSave));
                textComponent.write(bufferedWriter);
                bufferedWriter.close();
                changeSavedSate(true);
            }
        } catch (DocumentException | IOException e ) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(textComponent, "Error:\n Something went wrong when saving to " + currentFilePath);
        }
        updateCodeFormatting();
    }

    /**
     * Overloaded save method to call the main save method but with false parameter
     */
    public void save() {
        this.save(false);
    }

    /**
     * Method will call save method with the saveAs parameter true
     */
    public void saveAs() {
        this.save(true);
    }
    /**
     * Opens the file at currentFilePath and loads it in the textComponent
     */
    private void open(Boolean openCurrent) {
        File fileToOpen;
        //Check if file needs to be saved
        if (!this.saveWarning()) return;
        if (!openCurrent) {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(textComponent) == JFileChooser.APPROVE_OPTION) {
                fileToOpen = fileChooser.getSelectedFile();
            } else {
                return;
            }
        } else {
            fileToOpen = new File(currentFilePath);
        }
        try {
            //Switch statement used as more file types will be added later
            switch (FilenameUtils.getExtension(fileToOpen.getAbsolutePath())) {
                //This case is to Try and open odt extension files
                case "odt":
                    OdfTextDocument odt = OdfTextDocument.loadDocument(fileToOpen);
                    StringBuilder textFromODT = new StringBuilder();

                    TextPElement currentLine = OdfElement.findFirstChildNode(TextPElement.class, odt.getContentRoot());
                    textFromODT.append(currentLine.getTextContent());
                    while (OdfElement.findNextChildNode(TextPElement.class, currentLine) != null) {
                        textFromODT.append("\n");
                        currentLine = OdfElement.findNextChildNode(TextPElement.class, currentLine);
                        textFromODT.append(currentLine.getTextContent());
                    }
                    textComponent.setText(textFromODT.toString());
                    break;
                //This case is to Try and open pdf extension files
                case "pdf":
                    JOptionPane.showMessageDialog(textComponent, "Sorry Reading from PDFs is not yet" +
                            " implemented, hopefully will be in the future");
                    break;
                //This case is to Try and open rtf extension files
                case "rtf":
                    RTFEditorKit rtfParser = new RTFEditorKit();
                    javax.swing.text.Document document = rtfParser.createDefaultDocument();
                    rtfParser.read(new FileInputStream(fileToOpen), document, 0);
                    String text = document.getText(0, document.getLength());
                    textComponent.setText(text);
                    break;
                default: //Going to try open up file like a txt file
                    currentFilePath = fileToOpen.getAbsolutePath();
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(fileToOpen));
                    textComponent.read(bufferedReader, null);
                    bufferedReader.close();
                    changeSavedSate(true);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(textComponent, "Error:\n Something went wrong when opening " + currentFilePath);
        }
        updateCodeFormatting();
    }

    //public method to call open method
    public void open() {
        this.open(false);
    }

    public void openCurrent() {
        this.open(true);
    }

    /**
     * Clears the text area and puts the fileManager into a clean state.
     */
    public void newFile() {
        if (!this.saveWarning()) return;
        currentFilePath = null;
        textComponent.setText(null);
        changeSavedSate(true);
    }

    /**
     * Simple print call of the textComponents print method
     */
    public void print() {
        try {
            textComponent.print();
        } catch (PrinterException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(textComponent, "Error:\n Something went wrong with printing");
        }
    }

    /**
     * Method to get the file path of the current file
     *
     * @return currentFilePath
     */
    public String getCurrentFilePath() {
        return currentFilePath;
    }

    /**
     * Method to set the current File Path
     */
    public void setCurrentFilePath(String currentFilePath) {
        this.currentFilePath = currentFilePath;
    }

    /**
     * Change the saved state of the current open file
     *
     * @param newSavedState
     */
    private void changeSavedSate(Boolean newSavedState) {
        isSaved = newSavedState;
        String newTitle = currentFilePath == null ? "untitled" : new File(currentFilePath).getName();
        if (isSaved) {
            setTitle(newTitle);
        } else {
            setTitle(newTitle + " *");
        }
    }

    /**
     * Set the title  of the current file open on top of the textArea
     *
     * @param title
     */
    private void setTitle(String title) {
        textComponent.setBorder(BorderFactory.createTitledBorder(title));
    }

    /**
     * Checks to see if the current file is saved. If it isn't it will prompt the user if they want to save before continuing
     * @return whether the action should be continued or not.
     */
    private boolean saveWarning() {
        if (!this.getIsSaved()) {
            int response = JOptionPane.showConfirmDialog(textComponent, "Your current file is unsaved. Would you like to save it before continuing?");
            switch (response) {
                case JOptionPane.YES_OPTION:
                    this.save();
                    break;
                case JOptionPane.NO_OPTION:
                    break;
                default:
                    return false;
            }
        }
        return true;
    }


    class TextAreaListener implements DocumentListener {

        /**
         * Gives notification that there was an insert into the document.  The
         * range given by the DocumentEvent bounds the freshly inserted region.
         *
         * @param e the document event
         */
        @Override
        public void insertUpdate(DocumentEvent e) {
            changeSavedSate(false);
        }

        /**
         * Gives notification that a portion of the document has been
         * removed.  The range is given in terms of what the view last
         * saw (that is, before updating sticky positions).
         *
         * @param e the document event
         */
        @Override
        public void removeUpdate(DocumentEvent e) {
            changeSavedSate(false);
        }

        /**
         * Gives notification that an attribute or set of attributes changed.
         *
         * @param e the document event
         */
        @Override
        public void changedUpdate(DocumentEvent e) {

        }

    }

    class WindowCloseListener implements WindowListener {
        private final JFrame frame;
        WindowCloseListener(JFrame frame) {
            this.frame = frame;
        }

        /**
         * Invoked the first time a window is made visible.
         *
         * @param e the event to be processed
         */
        @Override
        public void windowOpened(WindowEvent e) {

        }

        /**
         * Invoked when the user attempts to close the window
         * from the window's system menu.
         *
         * @param e the event to be processed
         */
        @Override
        public void windowClosing(WindowEvent e) {
            if (saveWarning()) frame.dispose();
        }

        /**
         * Invoked when a window has been closed as the result
         * of calling dispose on the window.
         *
         * @param e the event to be processed
         */
        @Override
        public void windowClosed(WindowEvent e) {
        }

        /**
         * Invoked when a window is changed from a normal to a
         * minimized state. For many platforms, a minimized window
         * is displayed as the icon specified in the window's
         * iconImage property.
         *
         * @param e the event to be processed
         * @see Frame#setIconImage
         */
        @Override
        public void windowIconified(WindowEvent e) {

        }

        /**
         * Invoked when a window is changed from a minimized
         * to a normal state.
         *
         * @param e the event to be processed
         */
        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        /**
         * Invoked when the Window is set to be the active Window. Only a Frame or
         * a Dialog can be the active Window. The native windowing system may
         * denote the active Window or its children with special decorations, such
         * as a highlighted title bar. The active Window is always either the
         * focused Window, or the first Frame or Dialog that is an owner of the
         * focused Window.
         *
         * @param e the event to be processed
         */
        @Override
        public void windowActivated(WindowEvent e) {

        }

        /**
         * Invoked when a Window is no longer the active Window. Only a Frame or a
         * Dialog can be the active Window. The native windowing system may denote
         * the active Window or its children with special decorations, such as a
         * highlighted title bar. The active Window is always either the focused
         * Window, or the first Frame or Dialog that is an owner of the focused
         * Window.
         *
         * @param e the event to be processed
         */
        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    }

    /**
     *  Get textAreaListener
     */
    public TextAreaListener getTextAreaListener() {
        return textAreaListener;
    }

    /**
     * Return True or false to indicate the save status
     *
     * @return boolean (T/F)
     */
    public Boolean getIsSaved() {
        return this.isSaved;
    }

    /**
     * Get current file extension and return null if there is no file
     * else return extension
     *
     * @return fileExtension
     */
    public String getExtension() {
        if (currentFilePath == null) return "";
        else return FilenameUtils.getExtension(currentFilePath);
    }

    /**
     * Set the Code formatting (True or False)
     *
     * @param codeFormatting
     */
    public void setCodeFormatting(boolean codeFormatting) {
        this.codeFormatting = codeFormatting;
    }

    /**
     * Update the code formatting in the textArea, If true then get
     * file extension and Syntax Highlight the text else remove
     * syntax highlighting, auto indent and code folding
     *
     */
    public void updateCodeFormatting() {
        RSyntaxTextArea textArea = (RSyntaxTextArea) textComponent;
        if (codeFormatting) {
            String syntax;
            //Switch case for different programming language and syntax highlighting styles
            switch (getExtension()) {
                case "cpp":
                    syntax = SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS;
                    break;
                case "java":
                    syntax = SyntaxConstants.SYNTAX_STYLE_JAVA;
                    break;
                case "css":
                    syntax = SyntaxConstants.SYNTAX_STYLE_CSS;
                    break;
                case "html":
                    syntax = SyntaxConstants.SYNTAX_STYLE_HTML;
                    break;
                case "xml":
                    syntax = SyntaxConstants.SYNTAX_STYLE_XML;
                    break;
                case "js":
                    syntax = SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT;
                    break;
                case "spl":
                    syntax = SyntaxConstants.SYNTAX_STYLE_SQL;
                    break;
                case "c":
                    syntax = SyntaxConstants.SYNTAX_STYLE_C;
                    break;
                case "py":
                    syntax = SyntaxConstants.SYNTAX_STYLE_PYTHON;
                    break;
                case "yml":
                    syntax = SyntaxConstants.SYNTAX_STYLE_YAML;
                    break;
                default:
                    syntax = SyntaxConstants.SYNTAX_STYLE_NONE;
            }
                textArea.setSyntaxEditingStyle(syntax);
                if (syntax.equals(SyntaxConstants.SYNTAX_STYLE_NONE)) {
                    textArea.setAutoIndentEnabled(false);
                    textArea.setCodeFoldingEnabled(false);
                } else {
                    textArea.setAutoIndentEnabled(true);
                    textArea.setCodeFoldingEnabled(true);
                }
            } else {
                textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
                textArea.setAutoIndentEnabled(false);
                textArea.setCodeFoldingEnabled(false);
        }
    }
}
