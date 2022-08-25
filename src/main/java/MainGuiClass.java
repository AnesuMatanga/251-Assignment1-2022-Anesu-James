/**
 * This MainGuiClass is a class that creates the GUI and interacts with other
 * classes with functions to make the text editor act a certain way
 */

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.*;


public class MainGuiClass extends JFrame{
    //Initialising Fields
    static JFrame mainFrame;
    static JPanel textPanel;
    static JMenuBar menuBar;
    static JMenu fileMenu, editMenu, themeMenu;
    static JMenuItem newItem, openItem, saveItem, saveAsItem, printItem, exitItem, cutEditItem, copyEditItem,
            pasteEditItem, deleteEditItem;
    static RSyntaxTextArea mainTextArea;
    static RTextScrollPane scrollPane;
    static JSpinner fontSizeSpinner;
    static JLabel fontSizeSpinnerLabel;
    static JButton dateAndTimeButton;
    //static JButton searchButton;
    static JButton aboutButton;
    static JTextField searchTextField;
    static JLabel searchBoxLabel;


    private FileManager fileManager;
    private EditorManager editorManager;
    private SearchBoxManager searchBoxManager;
    private ConfigManager configManager;


    //Constructor
    MainGuiClass() {
        //Creating Objects for the GUI

        mainFrame = new JFrame("Text Editor");

        //Setting the look and feel of the Main Window to Java L&F
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainGuiClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(MainGuiClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MainGuiClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainGuiClass.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Creating GUI Objects
        textPanel = new JPanel();
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        themeMenu = new JMenu("Theme");
        fontSizeSpinner = new JSpinner();
        fontSizeSpinnerLabel = new JLabel("Font:");
        mainTextArea = new RSyntaxTextArea();
        newItem = new JMenuItem("New");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        saveAsItem = new JMenuItem("Save As");
        printItem = new JMenuItem("Print");
        exitItem = new JMenuItem("Exit");
        cutEditItem = new JMenuItem("Cut");
        copyEditItem = new JMenuItem("Copy");
        pasteEditItem = new JMenuItem("Paste");
        deleteEditItem = new JMenuItem("Delete");

        dateAndTimeButton = new JButton("Add Date & Time");
        aboutButton = new JButton("About");
        searchTextField = new JTextField();
        searchBoxLabel = new JLabel("Search:");
        scrollPane = new RTextScrollPane(mainTextArea);

        fileManager = new FileManager(mainTextArea);
        editorManager = new EditorManager(mainTextArea);
        searchBoxManager = new SearchBoxManager(mainTextArea);
        configManager = new ConfigManager();

        //Adding SyntaxConstants (RSyntaxTextArea JAR) to the mainTextArea for highlighting different languages
        mainTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        mainTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        mainTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
        mainTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CSS);
        mainTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
        mainTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_YAML);
        mainTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
        mainTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        mainTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
        mainTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
        mainTextArea.setCodeFoldingEnabled(true);

        //Adding menu Items to menu
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(printItem);
        fileMenu.add(exitItem);

        //Adding Edit menu Items to Edit Menu
        editMenu.add(cutEditItem);
        editMenu.add(copyEditItem);
        editMenu.add(pasteEditItem);
        editMenu.add(deleteEditItem);

        //Set fontSizeSpinner preferred size from Config.yml File
        fontSizeSpinner.setPreferredSize(new Dimension(50, 25));
        fontSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                mainTextArea.setFont(new Font(mainTextArea.getFont()
                        .getFamily(), Font.PLAIN,(int) fontSizeSpinner.getValue()));
            }
        });
        fontSizeSpinner.setValue(configManager.getConfigProperty("font_size"));

        //Add Action Listener to font colour button



        //Adding ActionListeners to the Menu Items (lambda expression)
        newItem.addActionListener(e -> fileManager.newFile());
        openItem.addActionListener(e -> fileManager.open());
        saveItem.addActionListener(e -> fileManager.save());
        saveAsItem.addActionListener(e -> fileManager.saveAs());
        printItem.addActionListener(e -> fileManager.print());
        exitItem.addActionListener(e -> mainFrame.dispose());


        //Adding ActionListeners to Edit Menu Items
        cutEditItem.addActionListener(e -> mainTextArea.cut());
        copyEditItem.addActionListener(e -> mainTextArea.copy());
        pasteEditItem.addActionListener(e -> mainTextArea.paste());
        deleteEditItem.addActionListener(e -> editorManager.delete());

        //Adding ActionListener to date and time button
        dateAndTimeButton.addActionListener(e -> editorManager.addDateAndTime());

        aboutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame, "Welcome to our text editor!\n" +
                    "Made by Anesu and James for assignment 1 of 159251 Semester 2 2022.\n" +
                    "Search feature: Just start typing and it will search the current document for that text." +
                    " Pressing Enter will cycle through the found occurrences of the search string.\n");
        });

        //Adding DocumentListener and ActionListener to JTextField
        searchTextField.getDocument().addDocumentListener(searchBoxManager);
        searchTextField.addActionListener(searchBoxManager);

        //Adding the menu to the menuBar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(searchBoxLabel);
        menuBar.add(searchTextField);
        menuBar.add(fontSizeSpinnerLabel);
        menuBar.add(fontSizeSpinner);
        menuBar.add(dateAndTimeButton);
        menuBar.add(aboutButton);
        //Adding the menuPanel and the textPanel to the mainFrame
        mainFrame.setJMenuBar(menuBar);
        mainFrame.add(scrollPane);
        mainFrame.setSize(600, 600);
        //mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }
}










