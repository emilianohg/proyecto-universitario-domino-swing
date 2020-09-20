package views;

import domain.Player;

public class ButtonSkip extends GameButton {

    private final Player player;

    public ButtonSkip (Player player) {
        this.player = player;
        setFocusable(false);
        setOpaque(false);
        setContentAreaFilled(false);
        setIcon("src/assets/next.png");
    }

    public Player getPlayer() {
        return player;
    }

}
