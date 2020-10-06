package views;

import domain.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CardPlayer extends JPanel {

    private final Player player;

    public CardPlayer (Player player, String urlImage) {
        this.player = player;
        JLabel name = new JLabel(player.getName());
        JLabel icon = new JLabel();
        try {
            BufferedImage image = ImageIO.read(new File(urlImage));
            icon.setIcon(new ImageIcon(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
        add(icon);
        add(name);

        deactivate();
    }

    public void activate () {
        Border line = BorderFactory.createLineBorder(Color.blue, 2);
        setBorder(line);
    }

    public void deactivate () {
        Border line = BorderFactory.createLineBorder(Color.lightGray, 2);
        setBorder(line);
    }

    public void winner () {
        Border line = BorderFactory.createLineBorder(Color.blue, 2);
        Border titled = BorderFactory.createTitledBorder(
            line,
            "Ganador",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            null,
            Color.blue
        );
        setBorder(titled);
    }

    public Player getPlayer() {
        return player;
    }
}
