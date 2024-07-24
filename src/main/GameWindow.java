package main;

import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow extends JFrame {

    public GameWindow(GamePanel gamePanel){

        //setSize(800, 600); // commented because we are using setPanelSize() in GamePanel, this will be used to set the size of the window, se explanation in https://www.youtube.com/watch?v=BT2jm74lLlg&list=PL4rzdwizLaxYmltJQRjq18a9gsSyEQQ-0&index=5 at 3.20
        setTitle("BubleBubble"); // Set the title of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the window and exit the application when the window is closed// }
        add(gamePanel);
        setLocationRelativeTo(null); // Center the window on the screen
        setResizable(false);
        pack(); // Adjust the size of the window to fit the preferred size of the panel (set in the setPanelSize() method of the GamePanel class)
        setVisible(true);

        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {

            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().windowFocusLost();
            }
        });


    }
}
