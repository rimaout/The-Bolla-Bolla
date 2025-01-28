package view;

import java.awt.*;
import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import static view.utilz.Load.GAME_ICON;

/**
 * The GameFrame class represents the main frame window of the game.
 * It extends JFrame and sets up the frame window properties, including the title, icon, and focus listeners.
 */
public class GameFrame extends JFrame {

    /**
     * Constructs a GameWindow with the specified GamePanel.
     * Sets the window properties and adds the game panel to the window.
     *
     * @param gamePanel the GamePanel to be added to the window
     */
    public GameFrame(GamePanel gamePanel){

        setTitle("BubbleBubble"); // Set the title of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the window and exit the application when the window is closed// }
        add(gamePanel);

        setGameIcon();
        macOSTitleBarCustomizations();
        setResizable(false);
        pack(); // Adjust the size of the window to fit the preferred size of the panel (set in the setPanelSize() method of the GamePanel class)
        setLocationRelativeTo(null); // Center the window on the screen

        setVisible(true);

        createWindowFocusListener(gamePanel);
    }

    private void createWindowFocusListener(GamePanel gamePanel){
        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                // empty method (if focus is gained do nothing)
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGameController().windowFocusLost();
            }
        });
    }

    /**
     * Sets the icon of the window in the taskbar.
     * Attempts to set the icon in the taskbar, and prints a message if the taskbar is not supported.
     */
    private void setGameIcon(){
        Image icon = new ImageIcon(GAME_ICON).getImage();

        setIconImage(icon); // Set the icon of the window

        // set the icon in the taskbar
        try {
            Taskbar.getTaskbar().setIconImage(icon);
        } catch (UnsupportedOperationException e) {
            System.out.println("TaskBar not supported on Windows!");
        }
    }

    /**
     * Customizes the title bar of the window for macOS.
     * Sets the window content to be full, the title bar to be transparent, and the window title to be invisible.
     */
    private void macOSTitleBarCustomizations() {
        // Check if the current operating system is macOS
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            getRootPane().putClientProperty("apple.awt.fullWindowContent", true);
            getRootPane().putClientProperty("apple.awt.transparentTitleBar", true);
            getRootPane().putClientProperty("apple.awt.windowTitleVisible", false);
        } else {
            System.out.println("macOS customizations are not applied as the current OS is not macOS.");
        }
    }

}