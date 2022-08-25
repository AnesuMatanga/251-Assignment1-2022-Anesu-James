import org.junit.Test;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FileManagerTest {

    @Test
    public void save() {
        String textFileContents = "This is what the text file contains\nand this is on the other line";
        JTextArea textComponent = new JTextArea(textFileContents);
        FileManager manager = new FileManager(textComponent);
        manager.save();
        assert(Files.exists(Path.of(manager.getCurrentFilePath())));
        try {
            Files.delete(Path.of(manager.getCurrentFilePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void open() {
        JTextArea textComponent = new JTextArea();
        FileManager manager = new FileManager(textComponent);
        manager.open();
        System.out.println(textComponent.getText());
    }

    @Test
    public void newFile() {
        JTextArea textComponent = new JTextArea("This text area has some text");
        FileManager manager = new FileManager(textComponent);
        manager.newFile();
        assertEquals("", textComponent.getText());
    }
}