import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.io.FilenameUtils;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.odftoolkit.odfdom.pkg.OdfElement;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import javax.swing.text.rtf.RTFEditorKit;
import java.awt.*;
import java.awt.print.PrinterException;
import java.io.*;

public class FileManager {
    //File path should always be kept in absolute form
    private String currentFilePath = null;
    private boolean isSaved = false;
    //Text component that will be saved from and written to.
    private final JTextComponent textComponent;
    private final TextAreaListener textAreaListener;

    /**
     * Constructor to load the field
     * @param textComponent The component where files will be saved and written to
     */
    public FileManager(JTextComponent textComponent) {
        this.textComponent = textComponent;
        this.textAreaListener = new TextAreaListener();
        this.textComponent.getDocument().addDocumentListener(this.getTextAreaListener());
    }

    /**
     * Save method will take contents from JTextComponent and save it to the currentFilePath.
     * It is private and can only be accessed by the save and saveas Methods.
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
        //Switch statement used as more file types will be added in the future. This could later change into
        //a save class or something, but currently it is left as is.
        switch (FilenameUtils.getExtension(fileToSave.getAbsolutePath())) {
            case "pdf":
                try {
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
                } catch (DocumentException | FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default: //Assuming file is txt format
                currentFilePath = fileToSave.getAbsolutePath();
                changeSavedSate(true);
                try {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileToSave));
                    textComponent.write(bufferedWriter);
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
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
    public void open() {
        JFileChooser fileChooser = new JFileChooser();
        int fileChooserResult = fileChooser.showOpenDialog(textComponent);
        if (fileChooserResult == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            try {
                //Switch statement used as more file types will be added later
                switch (FilenameUtils.getExtension(fileToOpen.getAbsolutePath())) {
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
                    case "pdf":
                        JOptionPane.showMessageDialog(textComponent, "Sorry Reading from PDFs is not yet" +
                                " implemented, hopefully will be in the future");
                        break;
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
            }
        }
    }

    /**
     * Clears the text area and puts the fileManager into a clean state.
     */
    public void newFile() {
        currentFilePath = null;
        textComponent.setText(null);
    }

    /**
     * Simple print call of the textComponents print method
     */
    public void print() {
        try {
            textComponent.print();
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }
    }

    public String getCurrentFilePath() {
        return currentFilePath;
    }
    public void setCurrentFilePath(String currentFilePath) {
        this.currentFilePath = currentFilePath;
    }

    private void changeSavedSate(Boolean newSavedState) {
        if (isSaved == newSavedState) return;
        else isSaved = newSavedState;
        if (isSaved) {
            setTitle(new File(currentFilePath).getName());
        } else {
            setTitle(new File(currentFilePath).getName() + " *");
        }
    }

    private void setTitle(String title) {
        textComponent.setBorder(BorderFactory.createTitledBorder(title));
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

    public TextAreaListener getTextAreaListener() {
        return textAreaListener;
    }

    public Boolean getIsSaved() {
        return this.isSaved;
    }
}
