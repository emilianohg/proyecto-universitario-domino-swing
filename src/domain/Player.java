package domain;

import java.util.ArrayList;
import java.util.Arrays;

public class Player {

    private ArrayList<Domino> dominoes;
    private String name;

    public Player (String name) {
        this.name = name;
        dominoes = new ArrayList<>();
    }

    public void take (Domino domino) {
        this.dominoes.add(domino);
    }

    public void take (Domino[] dominoes) {
        for (Domino domino : dominoes) {
            this.take(domino);
        }
    }

    public boolean canPlay () {
        return true;
    }

    public String getName() {
        return name;
    }
}
