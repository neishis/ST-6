package com.induce;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

enum State { PLAYING, OWIN, XWIN, DRAW }

class Player {
    public char symbol;
    public int move;
    public boolean selected;
    public boolean win;
}

class Game {
    public static final int INF = 100;
    private static final int[][] LINES = {
        {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
        {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
        {0, 4, 8}, {2, 4, 6}
    };

    public State state;
    public Player player1;
    public Player player2;
    public Player cplayer;
    public int nmove;
    public char symbol;
    public int q;
    public char[] board;

    public Game() {
        player1 = new Player();
        player2 = new Player();
        player1.symbol = 'X';
        player2.symbol = 'O';
        cplayer = player1;
        state = State.PLAYING;
        board = new char[9];
        Arrays.fill(board, ' ');
    }

    public State checkState(char[] position) {
        for (int[] line : LINES) {
            char owner = position[line[0]];
            if (owner != ' ' && owner == position[line[1]]
                    && owner == position[line[2]]) {
                return owner == 'X' ? State.XWIN : State.OWIN;
            }
        }

        return hasEmptyCell(position) ? State.PLAYING : State.DRAW;
    }

    void generateMoves(char[] position, ArrayList<Integer> moveList) {
        moveList.clear();
        for (int cell = 0; cell < position.length; cell++) {
            if (position[cell] == ' ') {
                moveList.add(cell);
            }
        }
    }

    int evaluatePosition(char[] position, Player player) {
        State checked = checkState(position);
        if (checked == State.DRAW) {
            return 0;
        }
        if (checked == State.PLAYING) {
            return -1;
        }

        char winner = checked == State.XWIN ? 'X' : 'O';
        return winner == player.symbol ? INF : -INF;
    }

    int MiniMax(char[] position, Player player) {
        ArrayList<Integer> moves = new ArrayList<>();
        generateMoves(position, moves);
        if (moves.isEmpty()) {
            return 0;
        }

        int bestMove = moves.get(0);
        int bestScore = -INF * 10;
        q = 0;

        for (int move : moves) {
            position[move] = player.symbol;
            int score = minimax(position, opposite(player.symbol), player.symbol, 1);
            position[move] = ' ';

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove + 1;
    }

    int MinMove(char[] position, Player player) {
        return minimax(position, opposite(player.symbol), player.symbol, 0);
    }

    int MaxMove(char[] position, Player player) {
        return minimax(position, player.symbol, player.symbol, 0);
    }

    private int minimax(char[] position, char turn, char maximizer, int depth) {
        q++;
        State checked = checkState(position);
        if (checked != State.PLAYING) {
            return terminalScore(checked, maximizer, depth);
        }

        boolean maximize = turn == maximizer;
        int best = maximize ? -INF * 10 : INF * 10;

        for (int cell = 0; cell < position.length; cell++) {
            if (position[cell] != ' ') {
                continue;
            }

            position[cell] = turn;
            int score = minimax(position, opposite(turn), maximizer, depth + 1);
            position[cell] = ' ';
            best = maximize ? Math.max(best, score) : Math.min(best, score);
        }

        return best;
    }

    private int terminalScore(State checked, char maximizer, int depth) {
        if (checked == State.DRAW) {
            return 0;
        }

        char winner = checked == State.XWIN ? 'X' : 'O';
        return winner == maximizer ? INF - depth : depth - INF;
    }

    private boolean hasEmptyCell(char[] position) {
        for (char cell : position) {
            if (cell == ' ') {
                return true;
            }
        }
        return false;
    }

    private char opposite(char marker) {
        return marker == 'X' ? 'O' : 'X';
    }
}

public class Program {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tic Tac Toe");
        frame.add(new TicTacToePanel(new GridLayout(3, 3)));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(5, 5, 500, 500);
        frame.setVisible(true);
    }
}

class TicTacToeCell extends JButton {
    private final int num;
    private final int row;
    private final int col;
    private char marker;

    TicTacToeCell(int num, int x, int y) {
        this.num = num;
        this.row = y;
        this.col = x;
        this.marker = ' ';
        setText(Character.toString(marker));
        setFont(new Font("Arial", Font.PLAIN, 40));
    }

    public void setMarker(String markerText) {
        marker = markerText.charAt(0);
        setText(markerText);
        setEnabled(false);
    }

    public char getMarker() {
        return marker;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getNum() {
        return num;
    }
}

class Utility {
    public static void print(char[] board) {
        printLine(toObjects(board));
    }

    public static void print(int[] board) {
        printLine(toObjects(board));
    }

    public static void print(ArrayList<Integer> moves) {
        printLine(moves.toArray());
    }

    private static Object[] toObjects(char[] values) {
        Object[] result = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i];
        }
        return result;
    }

    private static Object[] toObjects(int[] values) {
        Object[] result = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i];
        }
        return result;
    }

    private static void printLine(Object[] values) {
        System.out.println();
        for (Object value : values) {
            System.out.print(value + "-");
        }
        System.out.println();
    }
}

class TicTacToePanel extends JPanel implements ActionListener {
    private Game game;
    private TicTacToeCell[] cells = new TicTacToeCell[9];

    TicTacToePanel(GridLayout layout) {
        super(layout);
        createBoard();
        game = new Game();
    }

    private void createBoard() {
        for (int cell = 0; cell < cells.length; cell++) {
            createCell(cell, cell % 3, cell / 3);
        }
    }

    private void createCell(int num, int x, int y) {
        cells[num] = new TicTacToeCell(num, x, y);
        cells[num].addActionListener(this);
        add(cells[num]);
    }

    public void actionPerformed(ActionEvent event) {
        TicTacToeCell cell = (TicTacToeCell) event.getSource();
        if (cell.getMarker() != ' ' || game.state != State.PLAYING) {
            return;
        }

        markCell(cell, game.player1.symbol);
        game.state = game.checkState(game.board);

        if (game.state == State.PLAYING) {
            int aiMove = game.MiniMax(game.board, game.player2);
            if (aiMove > 0) {
                game.nmove = aiMove;
                markCell(cells[aiMove - 1], game.player2.symbol);
                game.state = game.checkState(game.board);
            }
        }

        if (game.state != State.PLAYING) {
            finishGame(game.state);
        }
    }

    protected void finishGame(State finalState) {
        String message;
        switch (finalState) {
            case XWIN:
                message = "Выиграли крестики";
                break;
            case OWIN:
                message = "Выиграли нолики";
                break;
            case DRAW:
                message = "Ничья";
                break;
            default:
                message = "Игра продолжается";
                break;
        }
        JOptionPane.showMessageDialog(null, message, "Результат",
                JOptionPane.WARNING_MESSAGE);
    }

    private void markCell(TicTacToeCell cell, char marker) {
        cell.setMarker(Character.toString(marker));
        game.board[cell.getNum()] = marker;
    }
}
