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
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;


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
    static JCheckBox checkBox;


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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error when setting up JFrame look and feel");
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
        checkBox = new JCheckBox("Highlighter");

        dateAndTimeButton = new JButton("Add Date & Time");
        aboutButton = new JButton("About");
        searchTextField = new JTextField();
        searchBoxLabel = new JLabel("Search:");
        scrollPane = new RTextScrollPane(mainTextArea);

        fileManager = new FileManager(mainTextArea);
        editorManager = new EditorManager(mainTextArea);
        searchBoxManager = new SearchBoxManager(mainTextArea);
        configManager = new ConfigManager();

        mainTextArea.setAutoIndentEnabled(false);
        mainTextArea.setCodeFoldingEnabled(false);
        /**
         * Adding a checkbox for the user to choose if they want the syntax Highlighter in
         * their text or not.
         * The File extension is taken from the open method in FileManager.java by a static
         * variable called extension which is then called here.
         */
        checkBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED && checkBox.isSelected()) {
                //DEBUG
                //System.out.println("Outside");
                if(fileManager.getExtension().equals("cpp")) {
                    mainTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
                }
                if (fileManager.getExtension().equals("java")) {
                    mainTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
                }
                if(fileManager.getExtension().equals("css")) {
                    mainTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CSS);
                }
                if (fileManager.getExtension().equals("html")) {
                    //DEBUG
                    System.out.println("html");
                    mainTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
                }
                if(fileManager.getExtension().equals("xml")) {
                    mainTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
                }
                if (fileManager.getExtension().equals("js")) {
                    mainTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
                }
                if(fileManager.getExtension().equals("sql")) {
                    mainTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
                }
                if (fileManager.getExtension().equals("c")) {
                    mainTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
                }
                if(fileManager.getExtension().equals("py")) {
                    mainTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
                }
                if (fileManager.getExtension().equals("yml")) {
                    mainTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_YAML);
                }
                mainTextArea.setAutoIndentEnabled(true);
                mainTextArea.setCodeFoldingEnabled(true);
            }
            else {
                mainTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
                mainTextArea.setAutoIndentEnabled(false);
                mainTextArea.setCodeFoldingEnabled(false);
            }
        });

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
        fontSizeSpinner.addChangeListener(e -> mainTextArea.setFont(new Font(mainTextArea.getFont()
                .getFamily(), Font.PLAIN,(int) fontSizeSpinner.getValue())));
        fontSizeSpinner.setValue(configManager.getConfigProperty("font_size"));

        //Set JTextArea font family from ConfigManager
        Font font = new Font((String) configManager.getConfigProperty("font_family"), Font.BOLD,
                (Integer) configManager.getConfigProperty("font_size"));
        mainTextArea.setFont(font);

        //Adding ActionListeners to the Menu Items (lambda expression)
        newItem.addActionListener(e -> fileManager.newFile());
        openItem.addActionListener(e -> fileManager.open());
        saveItem.addActionListener(e -> fileManager.save());
        saveAsItem.addActionListener(e -> fileManager.saveAs());
        printItem.addActionListener(e -> fileManager.print());
        exitItem.addActionListener(e -> mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING)));


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
        menuBar.add(checkBox);
        menuBar.add(dateAndTimeButton);
        menuBar.add(aboutButton);
        //Adding the menuPanel and the textPanel to the mainFrame
        mainFrame.setJMenuBar(menuBar);
        mainFrame.add(scrollPane);
        fileManager.setFrame();
        mainFrame.setSize(600, 600);
        //mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.setVisible(true);
    }
}










