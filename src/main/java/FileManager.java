import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.io.*;

public class FileManager {
    //File path should always be kept in absolute form
    private String currentFilePath = null;
    //Text component that will be saved from and written to.
    private JTextComponent textComponent;

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
            fileChooser.showSaveDialog(textComponent);
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

    public String getCurrentFilePath() {
        return currentFilePath;
    }
}
