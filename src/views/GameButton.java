package views;

import utils.Image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class GameButton extends JButton {
    protected BufferedImage image;
    protected double angleRotated = 0.0d;

    public void setIcon(String icon) {
        try {
            image = ImageIO.read(new File(icon));
            super.setIcon(new ImageIcon(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rotateRight () {
        rotate(90.0d);
    }

    public void rotateLeft () {
        rotate(-90.0d);
    }

    public void rotate (double angle) {
        this.angleRotated = angle;
        setIcon(new ImageIcon(Image.rotate(image, angleRotated)));
    }

    public void resetRotate () {
        angleRotated += -angleRotated;
        setIcon(new ImageIcon(Image.rotate(image, angleRotated)));
    }

    public void hints () {
        new Thread(() -> {
            BufferedImage imageActual = Image.rotate(image, angleRotated);
            setIcon(new ImageIcon(Image.invertImage(imageActual)));
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setIcon(new ImageIcon(Image.rotate(image, angleRotated)));
        }).start();
    }
}
