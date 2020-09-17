package views;

import domain.Domino;
import domain.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoardWindow extends JFrame {

    JPanel panelMain, panelCenter, panelBoard, panelOptions;
    JButton btnShuffle, btnPlay;

    List<ButtonDomino> dominoes;
    Player[] players;
    JPanel[] panelsPlayer;


    public BoardWindow () {
        super("Dominoes");
        setSize(1200, 800);
        setLocationRelativeTo(null);

        init();
        addDominoes();
        addPlayerPanel();
        addActions();

        setVisible(true);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void addDominoes () {
        dominoes = new ArrayList<>();

        for (int i = 0; i <= 6; i++) {
            for (int j=i; j <= 6; j++) {
                Domino domino=new Domino(i, j);
                ButtonDomino btnDomino=new ButtonDomino(domino);
                dominoes.add(btnDomino);
                panelBoard.add(btnDomino);
            }
        }
    }

    private void init () {
        getContentPane().removeAll();

        panelMain = new BackgroundPanel();
        panelMain.setLayout(new BorderLayout());
        getContentPane().add(panelMain);

        panelCenter = new JPanel();
        panelCenter.setOpaque(false);
        panelCenter.setLayout(new BorderLayout());
        panelMain.add(panelCenter, BorderLayout.CENTER);

        panelOptions = new JPanel();
        panelOptions.setOpaque(false);
        panelCenter.add(panelOptions, BorderLayout.SOUTH);

        btnShuffle = new JButton("Revolver", new ImageIcon("src/assets/shuffle.png"));
        panelOptions.add(btnShuffle);

        btnPlay = new JButton("Comenzar", new ImageIcon("src/assets/play.png"));
        panelOptions.add(btnPlay);

        GridLayout grid = new GridLayout(4,7);

        panelBoard = new JPanel();
        panelBoard.setLayout(grid);
        panelBoard.setOpaque(false);
        panelCenter.add(panelBoard, BorderLayout.CENTER);

    }

    public void addPlayerPanel () {
        players = new Player[4];
        panelsPlayer = new JPanel[4];
        String[] directions = {
                BorderLayout.NORTH,
                BorderLayout.EAST,
                BorderLayout.SOUTH,
                BorderLayout.WEST
        };

        for (int i=0; i < players.length; i++) {
            Player player = new Player(String.format("Player %s", i+1));
            players[i] = player;

            JPanel currentPanel = new JPanel();
            currentPanel.setOpaque(false);

            if ((i+1) % 2 == 0) {
                currentPanel.setLayout(new BoxLayout(currentPanel, BoxLayout.Y_AXIS));
                currentPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
            }

            panelMain.add(currentPanel, directions[i]);

            panelsPlayer[i] = currentPanel;
        }
    }

    public void addActions () {
        btnShuffle.addActionListener(evt -> {
            removeAllFromPanel(panelBoard);

            Collections.shuffle(dominoes);
            dominoes.forEach(panelBoard::add);

            panelBoard.update(panelBoard.getGraphics());
        });

        btnPlay.addActionListener(evt -> {

            for (int i = 0; i <= 3; i++) {
                List<ButtonDomino> dominoesTaken = dominoes.subList(i*7,(i+1)*7);

                JPanel currentPanel = panelsPlayer[i];
                int finalI=i;
                dominoesTaken.forEach(domino -> {
                    if ((finalI +1) % 2 == 0) {
                        domino.rotateRight();
                    }
                    currentPanel.add(domino);
                });

                currentPanel.update(currentPanel.getGraphics());
            }

            removeAllFromPanel(panelBoard);
            btnShuffle.setVisible(false);
            btnPlay.setVisible(false);

        });
    }

    public void removeAllFromPanel(JPanel panel) {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
        panel.update(panel.getGraphics());
    }


}
