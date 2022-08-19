/**
 * This MainGuiClass is a class that creates the GUI and interacts with other
 * classes with functions to make the text editor act a certain way
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.util.logging.*;


public class MainGuiClass extends JFrame {
    //Initialising Fields
    static JFrame mainFrame;
    static JPanel textPanel;
    static JMenuBar menuBar;
    static JMenu fileMenu, editMenu, themeMenu;
    static JMenuItem newItem, openItem, saveItem, printItem, exitItem, cutEditItem, copyEditItem,
            pasteEditItem, deleteEditItem;
    static JTextArea mainTextArea;
    static JScrollPane scrollPane;
    static JCheckBoxMenuItem darkModeItem, lightModeItem;
    static JButton dateAndTimeButton;
    static JButton searchButton;
    static JButton aboutButton;
    static JTextField searchTextField;


    private FileManager fileManger;
    private EditorManager editorManager;
    private SearchBoxManager searchBoxManager;

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
        darkModeItem = new JCheckBoxMenuItem("Dark Mode");
        lightModeItem = new JCheckBoxMenuItem("Light Mode");
        mainTextArea = new JTextArea();
        newItem = new JMenuItem("New");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        printItem = new JMenuItem("Print");
        exitItem = new JMenuItem("Exit");
        cutEditItem = new JMenuItem("Cut");
        copyEditItem = new JMenuItem("Copy");
        pasteEditItem = new JMenuItem("Paste");
        deleteEditItem = new JMenuItem("Delete");

        dateAndTimeButton = new JButton("Add Date & Time");
        searchButton = new JButton("Search");
        aboutButton = new JButton("About");
        searchTextField = new JTextField();
        scrollPane = new JScrollPane(mainTextArea);

        fileManger = new FileManager(mainTextArea);
        editorManager = new EditorManager(mainTextArea);
        searchBoxManager = new SearchBoxManager(mainTextArea);

        //Adding the mainTextArea to the textPanel
        //textPanel.add(mainTextArea);

        //Adding menu Items to menu
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(printItem);
        fileMenu.add(exitItem);

        //Adding Edit menu Items to Edit Menu
        editMenu.add(cutEditItem);
        editMenu.add(copyEditItem);
        editMenu.add(pasteEditItem);
        editMenu.add(deleteEditItem);

        //Adding Theme menu Items to Theme Menu
        darkModeItem.setSelected(false);
        themeMenu.add(darkModeItem);

        lightModeItem.setSelected(true);
        themeMenu.add(lightModeItem);

        //Adding ActionListeners to the Menu Items (lambda expression)
        newItem.addActionListener(e -> fileManger.newFile());
        openItem.addActionListener(e -> fileManger.open());
        saveItem.addActionListener(e -> fileManger.save());

        //Close the frame and the program
        exitItem.addActionListener(e -> mainFrame.dispose());

        printItem.addActionListener(e -> fileManger.print());
        //exitItem.addActionListener(e -> fileManger.exit());
        //fileMenu.addActionListener(this);


        //Adding ActionListeners to Edit Menu Items
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
        menuBar.add(themeMenu);
        menuBar.add(searchTextField);

        menuBar.add(dateAndTimeButton);

        menuBar.add(searchButton);
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










