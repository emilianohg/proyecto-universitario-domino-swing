package views;

import javax.swing.*;

public class BoardWindow extends JFrame {
    public BoardWindow () {
        super("Dominoes");
        init();
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void init () {
        getContentPane().removeAll();
        getContentPane().add(new BackgroundPanel());
    }


}
