package views;

import domain.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CardPlayer extends JPanel {
    private JLabel name;
    private JLabel icon;

    public CardPlayer (Player player) {
        name = new JLabel(player.getName());
        icon = new JLabel();
        try {
            BufferedImage image = ImageIO.read(new File("src/assets/users/user.png"));
            icon.setIcon(new ImageIcon(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
        add(icon);
        add(name);

        setBorder(BorderFactory.createLineBorder(Color.lightGray));
    }

}
