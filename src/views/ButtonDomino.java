package views;

import domain.Domino;
import utils.Image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ButtonDomino extends JButton {
    private Domino domino;
    private BufferedImage image;

    public ButtonDomino (Domino domino) {
        this.domino = domino;

        setBorder(null);
        setBorderPainted(false);
        setFocusable(false);
        setMargin(new Insets(0, 0, 0, 0));
        setContentAreaFilled(false);
        String urlDominoIcon = String.format(
                "src/assets/dominoes/domino-%s-%s.jpg",
                domino.getValue()[0],
                domino.getValue()[1]
        );
        try {
            image = ImageIO.read(new File(urlDominoIcon));
            setIcon(new ImageIcon(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setVisible(true);
    }

    public void rotateRight () {
        setIcon(new ImageIcon(Image.rotate(image, 90.0d)));
    }

    public Domino getDomino() {
        return domino;
    }
}
