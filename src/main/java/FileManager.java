import org.apache.commons.io.FilenameUtils;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.odftoolkit.odfdom.pkg.OdfElement;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.print.PrinterException;
import java.io.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import static com.lowagie.text.pdf.PdfName.DEST;

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
        //Switch statement used as more file types will be added in the future
        switch (FilenameUtils.getExtension(currentFilePath)) {
            case "odt":
                currentFilePath = null;
                this.save();
                return;
            default:
                break;
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
            File fileToOpen = new File(currentFilePath);
            try {
                //Switch statement used as more file types will be added later
                switch (FilenameUtils.getExtension(currentFilePath)) {
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
                    default:
                        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileToOpen));
                        textComponent.read(bufferedReader, null);
                        bufferedReader.close();
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
     * Save current document to PDF
     */
    public void saveToPDF() {
        if (textComponent.getText().equals("")) {
            JOptionPane.showMessageDialog(textComponent, "You need to have atleast some text to save to PDF");
            return;
        }
        JFileChooser fileChooser;
        if (currentFilePath != null) {
            fileChooser = new JFileChooser(new File(currentFilePath));
            fileChooser.setSelectedFile(new File(currentFilePath.replace(".txt", ".pdf")));
        } else {
            fileChooser = new JFileChooser();
        }

        int fileChooserResult = fileChooser.showSaveDialog(textComponent);
        if (fileChooserResult != JFileChooser.APPROVE_OPTION) return;
        String pdfFileLocation = fileChooser.getSelectedFile().getAbsolutePath();
        try {
            Document doc = new Document();
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(pdfFileLocation));
            Font font = new Font(Font.HELVETICA, 11, Font.BOLDITALIC, Color.BLACK);
            doc.open();
            Paragraph para = new Paragraph(textComponent.getText(), font);
            doc.add(para);
            doc.close();
            writer.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
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
