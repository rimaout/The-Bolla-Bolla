package main;

import java.awt.*;
import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow extends JFrame {

    public GameWindow(GamePanel gamePanel){

        setTitle("BubbleBubble"); // Set the title of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the window and exit the application when the window is closed// }
        add(gamePanel);

        Image icon = new ImageIcon("res/images/game-icon.png").getImage();

        setIconImage(icon); // Set the icon of the window
        try {
            Taskbar.getTaskbar().setIconImage(icon);
        } catch (UnsupportedOperationException e) {
            System.out.println("TaskBar non supportata su Windows!");
        }

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
                gamePanel.getGame().windowFocusLost();
            }
        });
    }
}
