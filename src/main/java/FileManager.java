import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.print.PrinterException;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import org.apache.commons.io.FilenameUtils;
import org.odftoolkit.odfdom.pkg.OdfElement;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.OdfTextDocument;

public class FileManager {
    //File path should always be kept in absolute form
    private String currentFilePath = null;
    //Text component that will be saved from and written to.
    private final JTextComponent textComponent;

    /**
     * Constructor to load the field
     * @param textComponent The component where files will be saved and written to
     */
    public FileManager(JTextComponent textComponent) {
        this.textComponent = textComponent;
    }

    /**
     * Save method will take contents from JTextComponent and save it to the currentFilePath. If needed will choose new file location.
     */
    public void save() {
        File fileToSave;
        if (currentFilePath == null) {
            JFileChooser fileChooser = new JFileChooser();
            int fileChooserResult = fileChooser.showSaveDialog(textComponent);
            if (fileChooserResult != JFileChooser.APPROVE_OPTION) return;
            currentFilePath = fileChooser.getSelectedFile().getAbsolutePath();
        }
        fileToSave = new File(currentFilePath);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileToSave));
            textComponent.write(bufferedWriter);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the file at currentFilePath and loads it in the textComponent
     */
    public void open() {
        JFileChooser fileChooser = new JFileChooser();
        int fileChooserResult = fileChooser.showOpenDialog(textComponent);
        if (fileChooserResult == JFileChooser.APPROVE_OPTION) {
            currentFilePath = fileChooser.getSelectedFile().getAbsolutePath();
            File fileToOpen = new File (currentFilePath);
            try {
                switch (FilenameUtils.getExtension(fileToOpen.getAbsolutePath())) {
                    case "txt":
                        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileToOpen));
                        textComponent.read(bufferedReader, null);
                        bufferedReader.close();
                        break;
                    case "odt":
                        OdfDocument odt = OdfDocument.loadDocument(fileToOpen);
                        textComponent.setText(odt.getContentRoot().getTextContent());
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
}
