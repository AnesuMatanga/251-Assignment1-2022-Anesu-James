import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class FileManagerTest {

    @org.junit.jupiter.api.Test
    void save() {
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
}