package domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class Player implements Comparable<Player> {

    private ArrayList<Domino> dominoes;
    private final String name;
    private int score = 0;

    public Player (String name) {
        this.name = name;
        dominoes = new ArrayList<>();
    }

    public void take (Domino domino) {
        score += domino.total();
        this.dominoes.add(domino);
    }

    public void take (Domino[] dominoes) {
        for (Domino domino : dominoes) {
            this.take(domino);
        }
    }

    public boolean drop (Domino domino) {
        Optional<Domino> dominoFinded = dominoes.stream().filter(d -> Arrays.equals(d.getValue(), domino.getValue())).findFirst();
        if (!dominoFinded.isPresent())
            return false;

        score -= dominoFinded.get().total();
        return true;
    }

    public int getScore() {
        return score;
    }

    public void resetDomino () {
        score = 0;
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
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(Player player) {
        return getScore() - player.getScore();
    }
}
