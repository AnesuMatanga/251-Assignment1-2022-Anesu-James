
/**
 * This MainGuiClass is a class that creates the GUI and interacts with other
 * classes with functions to make the text editor act a certain way
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainGuiClass implements ActionListener {
    //Initialising Fields
    static JFrame mainFrame;
    static JPanel menuPanel;
    static JPanel textPanel;
    static JMenuBar menuBar;
    static JMenu menu;
    static JMenuItem newItem, openItem, saveItem;
    static JTextArea mainTextArea;

    //Constructor
    MainGuiClass() {
        //Creating Objects for the GUI

        mainFrame = new JFrame("Text Editor");
        //Setting the look and feel of the Main Window to Java L&F
        try {
            //Java L&F cross-platform look
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException e) {

        } catch (IllegalAccessException e) {

        } catch (UnsupportedLookAndFeelException e) {

        } catch (InstantiationException e) {

        }

        textPanel = new JPanel();
        menuBar = new JMenuBar();
        menu = new JMenu("File");
        mainTextArea = new JTextArea();
        newItem = new JMenuItem("New");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");

        //Adding the mainTextArea to the textPanel
        textPanel.add(mainTextArea);

        //Adding menu Items to menu
        menu.add(newItem);
        menu.add(openItem);
        menu.add(saveItem);

        //Adding ActionListeners to the Menu Items
        newItem.addActionListener(this);
        openItem.addActionListener(this);
        saveItem.addActionListener(this);

        //Adding the menu to the menuBar
        menuBar.add(menu);

        //Adding the menuPanel and the textPanel to the mainFrame
        mainFrame.setJMenuBar(menuBar);
        mainFrame.add(textPanel);
        mainFrame.setSize(300, 500);
        //mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }
        //If a menu Item has been selected
        public void actionPerformed (ActionEvent e){
            String selected = e.getActionCommand();

            //If else statements to give commands for different menu item selections
            if (selected.equals("New")) {
                //Call the FileManager Class
            } else if (selected.equals("Open")) {
                //Call the FileManager Class
            } else if (selected.equals("Save")) {
                //Call the FileManager Class
            }
        }
}


















