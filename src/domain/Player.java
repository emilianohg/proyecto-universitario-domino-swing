package domain;

import java.util.ArrayList;
import java.util.Objects;

public class Player {

    private ArrayList<Domino> dominoes;
    private String name;
    private int score = 0;

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

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                '}';
    }
}
