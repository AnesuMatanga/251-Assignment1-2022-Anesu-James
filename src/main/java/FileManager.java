import javax.swing.text.JTextComponent;

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
}
