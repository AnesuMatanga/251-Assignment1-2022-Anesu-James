import net.sf.saxon.style.SaxonImportQuery;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
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
        RSyntaxTextArea textComponent = new RSyntaxTextArea(textFileContents);
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
        RSyntaxTextArea textComponent = new RSyntaxTextArea();
        FileManager manager = new FileManager(textComponent);
        String testTxt = "This is a test";
        String fileName = "openTest.txt";
        textComponent.setText(testTxt);
        manager.setCurrentFilePath(fileName);
        manager.save();
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
        RSyntaxTextArea textComponent = new RSyntaxTextArea("This text area has some text");
        FileManager manager = new FileManager(textComponent);
        manager.newFile();
        assertEquals("", textComponent.getText());
    }

    @Test
    public void changeSavedState() {
        RSyntaxTextArea textComponent = new RSyntaxTextArea("This text area has some text");
        FileManager manager = new FileManager(textComponent);
        String fileName = "changeSavedStateTest.txt";
        manager.setCurrentFilePath(fileName);
        manager.save();
        assertEquals(true, manager.getIsSaved());
        textComponent.append("Changing the test should fire a action that makes it not saved");
        assertEquals(false, manager.getIsSaved());
        manager.save();
        assertEquals(true, manager.getIsSaved());
        try {
            Files.delete(Path.of(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void setTitle() {
        RSyntaxTextArea textComponent = new RSyntaxTextArea("This text area has some text");
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