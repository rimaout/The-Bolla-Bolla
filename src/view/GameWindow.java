package view;

import java.awt.*;
import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import static view.utilz.Load.GAME_ICON;

/**
 * The GameWindow class represents the main window of the game.
 * It extends JFrame and sets up the window properties, including the title, icon, and focus listeners.
 */
public class GameWindow extends JFrame {

    /**
     * Constructs a GameWindow with the specified GamePanel.
     * Sets the window properties and adds the game panel to the window.
     *
     * @param gamePanel the GamePanel to be added to the window
     */
    public GameWindow(GamePanel gamePanel){

        setTitle("BubbleBubble"); // Set the title of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the window and exit the application when the window is closed// }
        add(gamePanel);

        setIconInTaskBar();

        setResizable(false);
        pack(); // Adjust the size of the window to fit the preferred size of the panel (set in the setPanelSize() method of the GamePanel class)
        setLocationRelativeTo(null); // Center the window on the screen
        setVisible(true);

        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                // TO-DO Auto-generated method stub
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
    private void setIconInTaskBar(){
        Image icon = new ImageIcon(GAME_ICON).getImage();

        setIconImage(icon); // Set the icon of the window
        try {
            Taskbar.getTaskbar().setIconImage(icon);
        } catch (UnsupportedOperationException e) {
            System.out.println("TaskBar not supported on Windows!");
        }
    }
}