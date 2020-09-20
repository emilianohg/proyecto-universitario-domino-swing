package views;

import domain.Domino;
import domain.Player;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BoardWindow extends JFrame {

    JPanel panelMain, panelCenter, panelBoard, panelOptions;
    JButton btnShuffle, btnPlay;

    List<ButtonDomino> dominoes;
    List<ButtonDomino> dominoesPlayed;
    Player[] players;
    JPanel[] panelsPlayer;
    JButton[] btnsSkip;
    JLabel txtMessage;
    int numberPlayers = 4;
    int turn = 1;
    int numberLeft = 6;
    int numberRight = 6;

    boolean pause = true;

    public BoardWindow () {
        super("Dominoes");
        setSize(1200, 800);
        setResizable(false);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("src/assets/domino.png").getImage());

        init();
        addDominoes();
        addPlayerPanel();
        addActions();

        setVisible(true);
        // showResults();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void addDominoes () {
        dominoes = new ArrayList<>();
        dominoesPlayed = new ArrayList<>();

        for (int i = 0; i <= 6; i++) {
            for (int j=i; j <= 6; j++) {
                Domino domino=new Domino(i, j);
                ButtonDomino btnDomino = new ButtonDomino(domino);
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

        txtMessage = new JLabel("");
        txtMessage.setVisible(false);
        panelOptions.add(txtMessage);

        GridLayout grid = new GridLayout(0,7);

        panelBoard = new JPanel();
        panelBoard.setLayout(grid);
        panelBoard.setOpaque(false);
        panelCenter.add(panelBoard, BorderLayout.CENTER);

    }

    public void addPlayerPanel () {
        players = new Player[numberPlayers];
        panelsPlayer = new JPanel[numberPlayers];
        String[] directions = {
                BorderLayout.NORTH,
                BorderLayout.EAST,
                BorderLayout.SOUTH,
                BorderLayout.WEST
        };

        btnsSkip= new JButton[numberPlayers];
        for (int i=0; i < players.length; i++) {
            Player player = new Player(String.format("Player %s", i+1));
            players[i] = player;

            JPanel currentPanel = new JPanel();
            currentPanel.setOpaque(false);

            if ((i+1) % 2 == 0) {
                currentPanel.setLayout(new BoxLayout(currentPanel, BoxLayout.Y_AXIS));
            }

            ButtonSkip btnSkip = new ButtonSkip(player);
            btnSkip.setEnabled(false);
            for (int j = 0; j <= i; j++) {
                btnSkip.rotate(j * 90);
            }
            btnSkip.addActionListener(this::skipTurn);
            btnsSkip[i] = btnSkip;

            panelsPlayer[i] = currentPanel;
            if ((i + 1) % 2 == 0) {
                JPanel panel = new JPanel();
                panel.setOpaque(false);
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.add(Box.createVerticalGlue(), directions[i]);
                panel.add(currentPanel, directions[i]);
                panel.add(Box.createVerticalGlue(), directions[i]);
                panelMain.add(panel, directions[i]);
                continue;
            }
            panelMain.add(currentPanel, directions[i]);
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
                int finalI = i;

                if (i > 1) {
                    currentPanel.add(btnsSkip[i]);
                }

                dominoesTaken.forEach(domino -> {
                    if ((finalI + 1) % 2 == 0) {
                        domino.rotateRight();
                    }

                    if (isFirstDomino(domino.getDomino())) {
                        this.turn = finalI + 1;
                    }

                    domino.addActionListener(this::selectDomino);
                    domino.setOwner(players[finalI]);

                    currentPanel.add(domino);
                });

                if (i <= 1) {
                    currentPanel.add(btnsSkip[i]);
                }

                pause = false;

                currentPanel.update(currentPanel.getGraphics());
            }

            removeAllFromPanel(panelBoard);
            btnShuffle.setVisible(false);
            btnPlay.setVisible(false);
            txtMessage.setVisible(true);
            txtMessage.setText("Es turno del jugador " + turn);

            verifyGame();

        });
    }

    public void showResults () {
        JPanel glass = (JPanel) getGlassPane();
        String[] titles = {"Nombre", "Puntuacion"};
        DefaultTableModel tableModel = new DefaultTableModel(titles, 0);

        for (Player player : players) {
            Object[] row ={
                    player.getName(),
                    player.getScore(),
            };
            tableModel.addRow(row);
        }

        JTable table = new JTable(tableModel);
        table.setEnabled(false);
        glass.add(new JScrollPane(table), BorderLayout.CENTER);
        glass.setVisible(true);
        glass.setOpaque(true);
    }

    private void selectDomino(ActionEvent evt) {

        ButtonDomino btnDomino = (ButtonDomino) evt.getSource();
        Domino domino = btnDomino.getDomino();
        Player player = btnDomino.getOwner();

        if (dominoesPlayed.size() == 0 && !isFirstDomino(domino)) {
            return;
        }

        if (!isMyTurn(player)) {
            return;
        }

        if (!isValidDomino(domino)) {
            return;
        }

        int dominoMinus  = domino.getValue()[0];
        int dominoMajor  = domino.getValue()[1];

        btnDomino.resetRotate();

        if (dominoMinus == numberLeft || dominoMajor == numberLeft) {

            if (dominoMinus == numberLeft) {
                btnDomino.rotateRight();
                numberLeft = dominoMajor;
            } else {
                btnDomino.rotateLeft();
                numberLeft = dominoMinus;
            }

            dominoesPlayed.add(0, btnDomino);
        } else if (dominoMinus == numberRight || dominoMajor == numberRight) {

            if (dominoMinus == numberRight) {
                btnDomino.rotateLeft();
                numberRight = dominoMajor;
            } else {
                btnDomino.rotateRight();
                numberRight = dominoMinus;
            }

            dominoesPlayed.add(btnDomino);
        }

        if (isDouble(domino)) {
            btnDomino.resetRotate();
        }

        for(ActionListener al : btnDomino.getActionListeners()) {
            btnDomino.removeActionListener(al);
        }

        btnDomino.setOwner(null);

        panelBoard.removeAll();
        dominoesPlayed.forEach(_btnDomino -> {
            panelBoard.add(_btnDomino);
        });

        panelBoard.revalidate();
        panelBoard.repaint();
        update(getGraphics());

        nextTurn();
    }

    private void skipTurn(ActionEvent evt) {
        ButtonSkip btnSkip = (ButtonSkip) evt.getSource();

        Player currentPlayer = players[turn - 1];
        Player player = btnSkip.getPlayer();

        if (!currentPlayer.equals(player))
            return;

        nextTurn();
    }

    private void nextTurn () {
        if (pause)
            return;

        turn++;
        if (turn > numberPlayers)
            turn = 1;

        txtMessage.setText("Es turno del jugador " + turn);

        verifyGame();
    }

    public void removeAllFromPanel(JPanel panel) {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
        panel.update(panel.getGraphics());
    }

    private void verifyGame () {
        System.out.println("Verificar");
        List<ButtonDomino> buttons = getDominoesAvailables(turn);
        System.out.println("Fichas disponibles" + buttons.size());

        int dominoesNotPlayed = 0;
        for (int i = 1; i <= panelsPlayer.length; i++) {
            dominoesNotPlayed += getDominoesAvailables(i).size();
        }

        if (dominoesNotPlayed == 0) {
            System.out.println("Fin del juego");
        }

        disableAllBtnsSkip();
        if (buttons.size() == 0) {
            btnsSkip[turn - 1].setEnabled(true);
        }

        buttons.forEach(btn -> {
            btn.hints();
        });

    }

    private void disableAllBtnsSkip () {
        for (JButton btn : btnsSkip) {
            btn.setEnabled(false);
        }
    }

    private boolean isFirstDomino (Domino domino) {
        return Arrays.equals(domino.getValue(), new int[]{6, 6});
    }

    private boolean isMyTurn (Player player) {
        Player currentPlayer = players[turn - 1];
        return currentPlayer.equals(player);
    }

    private boolean isValidDomino (Domino domino) {
        int left    = domino.getValue()[0];
        int right   = domino.getValue()[1];
        return left == numberLeft
            || left == numberRight
            || right == numberLeft
            || right == numberRight;
    }



    private List<ButtonDomino> getDominoesAvailables (int turn) {
        JPanel panel = panelsPlayer[turn - 1];

        List<ButtonDomino> buttons = new ArrayList<>();

        Component[] components = panel.getComponents();
        System.out.println("Turno de " + turn);
        for (Component component : components) {
            if (component.getClass().equals(ButtonDomino.class)) {
                ButtonDomino btnDomino = ((ButtonDomino) component);
                if(isValidDomino(btnDomino.getDomino())) {
                    buttons.add(btnDomino);
                }
            }
        }

        return buttons;
    }

    private boolean isDouble (Domino domino) {
        return domino.getValue()[0] == domino.getValue()[1];
    }

}