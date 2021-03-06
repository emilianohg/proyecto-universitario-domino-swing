package views;

import domain.Domino;
import domain.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class BoardWindow extends JFrame {

    JPanel glass;
    JPanel panelMain, panelCenter, panelBoard, panelOptions;
    JButton btnShuffle, btnPlay, btnOriginal, btnRestart;

    private final Font fontTurn = new Font("Verdana", Font.PLAIN, 20);
    private final Font fontWinner = new Font("Verdana", Font.BOLD, 20);

    List<ButtonDomino> dominoes;
    Player[] players;
    JPanel[] panelsPlayer;
    JButton[] buttonsSkip;
    CardPlayer[] cardPlayers;

    JLabel txtMessage;

    int numberPlayers = 4;
    int turn = 1;
    int numberLeft = 6;
    int numberRight = 6;

    boolean pause = true;

    Graphics currentGraphic;
    Image backBuffer;

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
        makeGlass();

        setVisible(true);

        backBuffer = createImage(getWidth(), getHeight());
        currentGraphic = backBuffer.getGraphics();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    @Override
    public void paint(Graphics g) {
        if (backBuffer == null) {
            repaint();
            return;
        }
        if (currentGraphic == null) {
            currentGraphic = backBuffer.getGraphics();
        }
        super.paint(currentGraphic);
        g.drawImage(backBuffer, 0, 0, getWidth(), getHeight(), this);
    }

    public void makeGlass () {
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
            new Insets(5, 5, 5, 5),
            15,
            0
        );

        cardPlayers = new CardPlayer[numberPlayers];

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
    }

    public void initDominoes() {
        dominoes = new ArrayList<>();

        for (int i = 0; i <= 6; i++) {
            for (int j = i; j <= 6; j++) {
                Domino domino = new Domino(i, j);
                ButtonDomino btnDomino = new ButtonDomino(domino);
                dominoes.add(btnDomino);
            }
        }

        updateDominoBoard();
    }

    private void init () {
        getContentPane().removeAll();

        glass = (JPanel) getGlassPane();
        glass.setVisible(false);

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

        btnRestart = new JButton("Reiniciar", new ImageIcon("src/assets/reload.png"));
        btnRestart.setVisible(false);
        panelOptions.add(btnRestart);

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

        buttonsSkip= new JButton[numberPlayers];
        for (int i=0; i < players.length; i++) {
            Player player = new Player("Player " + (i+1));
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
            buttonsSkip[i] = btnSkip;

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
        btnShuffle.addActionListener(this::shuffle);

        btnPlay.addActionListener(this::play);
        btnRestart.addActionListener(this::restart);

        btnOriginal.addActionListener(evt -> initDominoes());
    }

    private void shuffle (ActionEvent evt) {
        removeAllFromPanel(panelBoard);

        Collections.shuffle(dominoes);
        dominoes.forEach(panelBoard::add);

        panelBoard.update(panelBoard.getGraphics());
    }

    private void restart (ActionEvent evt) {
        this.numberLeft = 6;
        this.numberRight = 6;

        initDominoes();

        for (JPanel panelPlayer : panelsPlayer) {
            panelPlayer.removeAll();
        }

        for (Player player : players) {
            player.resetDomino();
        }

        glass.setVisible(false);
        pause = true;

        btnRestart.setVisible(false);
        btnPlay.setVisible(true);
        btnOriginal.setVisible(true);
        btnShuffle.setVisible(true);

        txtMessage.setVisible(false);

        changeCurrentCardPlayer();
    }

    private void play (ActionEvent evt) {
        Collections.shuffle(dominoes);
        for (int i = 0; i <= numberPlayers-1; i++) {
            List<ButtonDomino> dominoesTaken = dominoes.subList(i*7,(i+1)*7);

            JPanel currentPanel = panelsPlayer[i];
            int finalI = i;

            if (i > 1) {
                currentPanel.add(buttonsSkip[i]);
            }

            dominoesTaken.forEach(domino -> {
                if ((finalI + 1) % 2 == 0) { // EAST and WEST
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
                currentPanel.add(buttonsSkip[i]);
            }

            pause = false;

            currentPanel.update(currentPanel.getGraphics());
        }

        dominoes.clear();

        removeAllFromPanel(panelBoard);
        btnShuffle.setVisible(false);
        btnPlay.setVisible(false);
        btnRestart.setVisible(false);
        btnOriginal.setVisible(false);
        txtMessage.setVisible(true);

        txtMessage.setText("Es turno de " + players[turn-1].getName());
        txtMessage.setFont(fontTurn);
        txtMessage.setForeground(Color.BLACK);

        glass.setVisible(true);

        verifyGame();
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

        // btnDomino.setEnabled(false);
        for(ActionListener al : btnDomino.getActionListeners()) {
            btnDomino.removeActionListener(al);
        }

        player.drop(domino);
        btnDomino.setOwner(null);

        updateDominoBoard();

        int remainingDominoes = getDominoes(turn).size();
        if (remainingDominoes == 0) {
            showWinner(player);
            return;
        }

        nextTurn();
    }

    private void updateDominoBoard () {
        panelBoard.removeAll();
        dominoes.forEach(panelBoard::add);

        panelBoard.revalidate();
        panelBoard.repaint();
        update(getGraphics());
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

        if (isGameEnded()) {
            calcWinner();
            return;
        }

        changeStatusSkipButton();
        showHints();

        txtMessage.setText("Es turno de " + players[turn-1].getName());
        changeCurrentCardPlayer();
    }

    private void changeStatusSkipButton () {
        disableAllBtnsSkip();

        List<ButtonDomino> buttons = getDominoesAvailables(turn);
        if (buttons.size() == 0) {
            this.buttonsSkip[turn - 1].setEnabled(true);
        }
    }

    private void showHints () {
        List<ButtonDomino> buttons = getDominoesAvailables(turn);
        buttons.forEach(btn -> {
            if (isFirstDomino(btn.getDomino())) {
                btn.hints();
            }

            if (dominoes.size() > 0) {
                btn.hints();
            }
        });
    }

    private boolean isGameEnded () {
        int totalValidDominoes = 0;
        for (int i = 1; i <= panelsPlayer.length; i++) {
            totalValidDominoes += getDominoesAvailables(i).size();
        }

        return totalValidDominoes == 0;
    }

    private void calcWinner() {

        List<Player> playersResults = Arrays.asList(players.clone());
        playersResults.sort(Comparator.reverseOrder());
        List<Player> winners = new ArrayList<>();

        StringBuilder text =new StringBuilder("El ganador es ");

        for (Player player : players) {
            if(winners.isEmpty()) {
                winners.add(player);
                text.append(player.getName());
                continue;
            }

            if (winners.get(0).getScore() > player.getScore()) {
                winners.clear();
                winners.add(player);
                text = new StringBuilder("El ganador es " + player.getName());
            } else if (winners.get(0).getScore() == player.getScore()) {
                winners.add(player);
                text = new StringBuilder("Los ganadores son ");
            }
        }

        if (winners.size() == 2) {
            text = new StringBuilder(String.format(text + "%s y %s", winners.stream().map(Player::getName).toArray()));
        } else if (winners.size() == 3) {
            text = new StringBuilder(String.format(text + "%s, %s y %s", winners.stream().map(Player::getName).toArray()));
        } else if (winners.size() == 4) {
            text = new StringBuilder("¡Todos ganaron!");
        }

        deactivateAllCardPlayer();
        for (CardPlayer cardPlayer : cardPlayers) {
            winners.forEach(w -> {
                if(w.equals(cardPlayer.getPlayer())) {
                    cardPlayer.winner();
                }
            });
        }

        txtMessage.setText(text.toString());
        txtMessage.setForeground(Color.blue);
        txtMessage.setFont(fontWinner);
        btnRestart.setVisible(true);

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
        btnRestart.setVisible(true);
    }

    private void disableAllBtnsSkip () {
        for (JButton btn : buttonsSkip) {
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
        List<ButtonDomino> btnDominoes = getDominoes(turn);

        return btnDominoes.stream()
                .filter(btnDomino -> isValidDomino(btnDomino.getDomino()))
                .collect(Collectors.toList());
    }

    private boolean isDouble (Domino domino) {
        return domino.getValue()[0] == domino.getValue()[1];
    }

}