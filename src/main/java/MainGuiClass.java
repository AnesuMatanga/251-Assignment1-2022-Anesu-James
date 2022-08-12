/**
 * This MainGuiClass is a class that creates the GUI and interacts with other
 * classes with functions to make the text editor act a certain way
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.*;


public class MainGuiClass extends JFrame implements ActionListener {
    //Initialising Fields
    static JFrame mainFrame;
    static JPanel textPanel;
    static JMenuBar menuBar;
    static JMenu fileMenu, editMenu, themeMenu;
    static JMenuItem newItem, openItem, saveItem, exitItem,cutEditItem, copyEditItem,
            pasteEditItem, deleteEditItem;
    static JEditorPane mainTextArea;
    static JScrollPane scrollPane;
    static JCheckBoxMenuItem darkModeItem, lightModeItem;
    static JButton searchButton;
    static JTextField searchTextField;


    private FileManager fileManger;
    private EditorManager editorManager;

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
        mainTextArea = new JEditorPane();
        newItem = new JMenuItem("New");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");
        cutEditItem = new JMenuItem("Cut");
        copyEditItem = new JMenuItem("Copy");
        pasteEditItem = new JMenuItem("Paste");
        deleteEditItem = new JMenuItem("Delete");
        searchButton = new JButton("Search");
        searchTextField = new JTextField();
        scrollPane = new JScrollPane(mainTextArea);

        fileManger = new FileManager(mainTextArea);
        editorManager = new EditorManager(mainTextArea);

        //Adding the mainTextArea to the textPanel
        //textPanel.add(mainTextArea);

        //Adding menu Items to menu
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
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

        //Adding ActionListeners to the Menu Items
        newItem.addActionListener(this);
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        fileMenu.addActionListener(this);

        //Adding ActionListeners to Edit Menu Items
        cutEditItem.addActionListener(e -> editorManager.cut());
        copyEditItem.addActionListener(e -> editorManager.copy());
        pasteEditItem.addActionListener(e -> editorManager.paste());
        deleteEditItem.addActionListener(e -> editorManager.delete());

        //Adding ActionListener to JButton and TextField to search
        searchButton.addActionListener(this);
        searchTextField.addActionListener(this);

        //Adding the menu to the menuBar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(themeMenu);
        menuBar.add(searchTextField);
        menuBar.add(searchButton);

        //Adding the menuPanel and the textPanel to the mainFrame
        mainFrame.setJMenuBar(menuBar);
        mainFrame.add(scrollPane);
        mainFrame.setSize(600, 600);
        //mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }
    //If a menu Item has been selected
    public void actionPerformed(ActionEvent e){
        String selected = e.getActionCommand();

        //If else statements to give commands for different menu item selections
        if(selected.equals("New")){
            fileManger.newFile();
        }
        else if(selected.equals("Open")){
            fileManger.open();
        }
        else if(selected.equals("Save")){
            fileManger.save();
        }
    }
}










