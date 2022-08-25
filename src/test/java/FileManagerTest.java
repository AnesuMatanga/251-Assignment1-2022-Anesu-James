import net.sf.saxon.style.SaxonImportQuery;
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
        manager.setCurrentFilePath("saveTest.txt");
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
        String testTxt = "This is a test";
        String fileName = "openTest.txt";
        textComponent.setText(testTxt);
        manager.setCurrentFilePath(fileName);
        manager.save();
        textComponent.setText(null);
        manager.openCurrent();
        assertEquals(testTxt, textComponent.getText());
        try {
            Files.delete(Path.of(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void newFile() {
        JTextArea textComponent = new JTextArea("This text area has some text");
        FileManager manager = new FileManager(textComponent);
        manager.newFile();
        assertEquals("", textComponent.getText());
    }

    @Test
    public void changeSavedState() {
        JTextArea textComponent = new JTextArea("This text area has some text");
        FileManager manager = new FileManager(textComponent);
        String fileName = "changeSavedStateTest.txt";
        manager.setCurrentFilePath(fileName);
        assertEquals(false, manager.getIsSaved());
        manager.save();
        assertEquals(true, manager.getIsSaved());
        textComponent.append("Changing the test should fire a action that makes it not saved");
        assertEquals(false, manager.getIsSaved());
        try {
            Files.delete(Path.of(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void setTitle() {
        JTextArea textComponent = new JTextArea("This text area has some text");
        FileManager manager = new FileManager(textComponent);
        String fileName = "setTitleTest";
        manager.setCurrentFilePath(fileName);
        manager.save();
        assertEquals(BorderFactory.createTitledBorder(fileName).getClass(), textComponent.getBorder().getClass());
        try {
            Files.delete(Path.of(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }
}