package views;

import domain.Domino;
import domain.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class BoardWindow extends JFrame {

    JPanel panelMain, panelCenter, panelBoard, panelOptions;
    JButton btnShuffle, btnPlay, btnOriginal;

    private final Font fontTurn = new Font("Verdana", Font.PLAIN, 20);
    private final Font fontWinner = new Font("Verdana", Font.BOLD, 20);

    List<ButtonDomino> dominoes;
    Player[] players;
    JPanel[] panelsPlayer;
    JButton[] btnsSkip;
    CardPlayer[] cardPlayers;

    JLabel txtMessage;

    int numberPlayers = 4;
    int turn = 1;
    int numberLeft = 6;
    int numberRight = 6;

    boolean pause = true;

    public BoardWindow () {
        super("Dominoes");
        setSize(1100, 740);
        setResizable(false);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("src/assets/domino.png").getImage());

        init();
        initDominoes();
        addPlayerPanel();
        addActions();

        setVisible(true);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void makeGlass () {
        JPanel glass = (JPanel) getGlassPane();
        glass.setOpaque(false);
        glass.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(
            0,
            0,
            1,
            1,
            1.0,
            1.0,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.NONE,
            new Insets(10, 10, 10, 10),
            20,
            0
        );

        cardPlayers = new CardPlayer[4];

        cardPlayers[0] = new CardPlayer(players[0], "src/assets/users/user-1.png");

        glass.add(cardPlayers[0], gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        cardPlayers[1] = new CardPlayer(players[1], "src/assets/users/user-2.png");
        glass.add(cardPlayers[1], gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        cardPlayers[3] = new CardPlayer(players[3], "src/assets/users/user-4.png");
        glass.add(cardPlayers[3], gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        cardPlayers[2] = new CardPlayer(players[2], "src/assets/users/user-3.png");
        glass.add(cardPlayers[2], gbc);

        glass.setVisible(true);
    }

    public void initDominoes() {
        dominoes = new ArrayList<>();

        removeAllFromPanel(panelBoard);

        for (int i = 0; i <= 6; i++) {
            for (int j=i; j <= 6; j++) {
                Domino domino=new Domino(i, j);
                ButtonDomino btnDomino = new ButtonDomino(domino);
                dominoes.add(btnDomino);
                panelBoard.add(btnDomino);
            }
        }

        panelBoard.revalidate();
        panelBoard.repaint();
        update(getGraphics());

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

        btnOriginal = new JButton("Original", new ImageIcon("src/assets/reload.png"));
        panelOptions.add(btnOriginal);

        txtMessage = new JLabel("");
        txtMessage.setFont(fontTurn);
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
                    if ((finalI + 1) % 2 == 0) { // Jugadores de los laterales
                        domino.rotateRight();
                    }

                    if (isFirstDomino(domino.getDomino())) {
                        this.turn = finalI + 1;
                    }

                    domino.addActionListener(this::selectDomino);
                    players[finalI].take(domino.getDomino());
                    domino.setOwner(players[finalI]);

                    currentPanel.add(domino);
                });

                if (i <= 1) {
                    currentPanel.add(btnsSkip[i]);
                }

                pause = false;

                currentPanel.update(currentPanel.getGraphics());
            }

            dominoes.clear();

            removeAllFromPanel(panelBoard);
            btnShuffle.setVisible(false);
            btnPlay.setVisible(false);
            btnOriginal.setVisible(false);
            txtMessage.setVisible(true);

            txtMessage.setText("Es turno de " + players[turn-1].getName());

            makeGlass();

            verifyGame();

        });

        btnOriginal.addActionListener(evt -> {
            initDominoes();
        });
    }

    private void selectDomino (ActionEvent evt) {

        ButtonDomino btnDomino = (ButtonDomino) evt.getSource();
        Domino domino = btnDomino.getDomino();
        Player player = btnDomino.getOwner();

        if (dominoes.size() == 0 && !isFirstDomino(domino)) {
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

            dominoes.add(0, btnDomino);
        } else if (dominoMinus == numberRight || dominoMajor == numberRight) {

            if (dominoMinus == numberRight) {
                btnDomino.rotateLeft();
                numberRight = dominoMajor;
            } else {
                btnDomino.rotateRight();
                numberRight = dominoMinus;
            }

            dominoes.add(btnDomino);
        }

        if (isDouble(domino)) {
            btnDomino.resetRotate();
        }

        for(ActionListener al : btnDomino.getActionListeners()) {
            btnDomino.removeActionListener(al);
        }

        player.drop(domino);
        btnDomino.setOwner(null);

        panelBoard.removeAll();
        dominoes.forEach(_btnDomino -> {
            panelBoard.add(_btnDomino);
        });

        panelBoard.revalidate();
        panelBoard.repaint();
        update(getGraphics());

        int remainingDominoes = getDominoes(turn).size();
        if (remainingDominoes == 0) {
            showWinner(player);
            return;
        }

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

        txtMessage.setText("Es turno de " + players[turn-1].getName());

        verifyGame();
    }

    private void removeAllFromPanel(JPanel panel) {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
        panel.update(panel.getGraphics());
    }

    private void changeCurrentCardPlayer () {
        deactivateAllCardPlayer();
        cardPlayers[turn - 1].activate();
    }

    private void deactivateAllCardPlayer() {
        for (CardPlayer cardPlayer : cardPlayers) {
            cardPlayer.deactivate();
        }
    }

    private void verifyGame () {

        int totalValidDominoes = 0;
        for (int i = 1; i <= panelsPlayer.length; i++) {
            totalValidDominoes += getDominoesAvailables(i).size();
        }

        if (totalValidDominoes == 0) {
            calcWinner();
            return;
        }

        disableAllBtnsSkip();

        List<ButtonDomino> buttons = getDominoesAvailables(turn);
        if (buttons.size() == 0) {
            btnsSkip[turn - 1].setEnabled(true);
        }

        changeCurrentCardPlayer();

        buttons.forEach(btn -> {
            if (isFirstDomino(btn.getDomino())) {
                btn.hints();
            }

            if (dominoes.size() > 0) {
                btn.hints();
            }
        });

    }

    private void calcWinner() {

        List<Player> playersResults = Arrays.asList(players.clone());
        playersResults.sort(Comparator.reverseOrder());
        List<Player> winners = new ArrayList();

        String text = "El ganador es ";

        for (Player player : players) {
            System.out.println(player.getScore());
            if(winners.isEmpty()) {
                winners.add(player);
                text += player.getName();
                continue;
            }

            if (winners.get(0).getScore() > player.getScore()) {
                winners.clear();
                winners.add(player);
                text = "El ganador es " + player.getName();
            } else if (winners.get(0).getScore() == player.getScore()) {
                winners.add(player);
                text = "Los ganadores son ";
            }
        }

        if (winners.size() == 2) {
            text = String.format(text + "%s y %s", winners.stream().map(w -> w.getName()).toArray());
        } else if (winners.size() == 3) {
            text = String.format(text + "%s, %s y %s", winners.stream().map(w -> w.getName()).toArray());
        } else if (winners.size() == 4) {
            text = "¡Todos ganaron!";
        }

        deactivateAllCardPlayer();
        for (CardPlayer cardPlayer : cardPlayers) {
            winners.forEach(w -> {
                if(w.equals(cardPlayer.getPlayer())) {
                    cardPlayer.winner();
                }
            });
        }

        txtMessage.setText(text);
        txtMessage.setForeground(Color.blue);
        txtMessage.setFont(fontWinner);
    }

    private List<ButtonDomino> getDominoes(int turn) {
        JPanel panel = panelsPlayer[turn - 1];
        List<ButtonDomino> buttons = new ArrayList<>();
        Component[] components = panel.getComponents();

        for (Component component : components) {
            if (component.getClass().equals(ButtonDomino.class)) {
                ButtonDomino btnDomino = ((ButtonDomino) component);
                buttons.add(btnDomino);
            }
        }

        return buttons;
    }

    private void showWinner(Player player) {
        cardPlayers[turn - 1].winner();
        txtMessage.setText("El ganador es " + player.getName());
        txtMessage.setForeground(Color.blue);
        txtMessage.setFont(fontWinner);
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