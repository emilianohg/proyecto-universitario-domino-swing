package domain;

import java.util.ArrayList;

public class Board {

    private ArrayList<Domino> dominoes;
    private Player[] players;

    public Board(ArrayList<Domino> dominoes, Player[] players) {
        this.dominoes=dominoes;
        this.players=players;
    }
}
