package views;

import java.awt.*;

import domain.Domino;
import domain.Player;

public class ButtonDomino extends GameButton {
    private final Domino domino;
    private Player owner;


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

        setIcon(urlDominoIcon);
        setVisible(true);
    }

    public void setOwner (Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public Domino getDomino() {
        return domino;
    }
}
