package com.mycompany.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class ProgramTest {

    @Test
    public void testPlayer() {
        Player p = new Player();
        p.symbol = 'X';
        assertEquals('X', p.symbol);
    }

    @Test
    public void testGameInit() {
        Game game = new Game();
        assertEquals(State.PLAYING, game.state);
        assertEquals('X', game.player1.symbol);
        assertEquals('O', game.player2.symbol);
        for (char c : game.board) {
            assertEquals(' ', c);
        }
    }

    @Test
    public void testCheckState() {
        Game game = new Game();
        game.symbol = 'X';
        game.board = new char[]{'X', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' '};
        assertEquals(State.XWIN, game.checkState(game.board));

        game.symbol = 'O';
        game.board = new char[]{'O', 'O', 'O', ' ', ' ', ' ', ' ', ' ', ' '};
        assertEquals(State.OWIN, game.checkState(game.board));

        game.symbol = 'X';
        game.board = new char[]{'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'};
        assertEquals(State.DRAW, game.checkState(game.board));

        game.board = new char[]{'X', 'O', 'X', 'X', ' ', 'O', 'O', 'X', 'X'};
        assertEquals(State.PLAYING, game.checkState(game.board));
        
        game.symbol = 'X';
        game.board = new char[]{'X', ' ', ' ', 'X', ' ', ' ', 'X', ' ', ' '};
        assertEquals(State.XWIN, game.checkState(game.board));

        game.symbol = 'X';
        game.board = new char[]{' ', 'X', ' ', ' ', 'X', ' ', ' ', 'X', ' '};
        assertEquals(State.XWIN, game.checkState(game.board));

        game.symbol = 'X';
        game.board = new char[]{' ', ' ', 'X', ' ', ' ', 'X', ' ', ' ', 'X'};
        assertEquals(State.XWIN, game.checkState(game.board));

        game.symbol = 'X';
        game.board = new char[]{'X', ' ', ' ', ' ', 'X', ' ', ' ', ' ', 'X'};
        assertEquals(State.XWIN, game.checkState(game.board));

        game.symbol = 'X';
        game.board = new char[]{' ', ' ', 'X', ' ', 'X', ' ', 'X', ' ', ' '};
        assertEquals(State.XWIN, game.checkState(game.board));
    }

    @Test
    public void testGenerateMoves() {
        Game game = new Game();
        game.board = new char[]{'X', 'O', 'X', ' ', ' ', ' ', 'O', 'X', ' '};
        ArrayList<Integer> moves = new ArrayList<>();
        game.generateMoves(game.board, moves);
        assertEquals(4, moves.size());
        assertTrue(moves.contains(3));
        assertTrue(moves.contains(4));
        assertTrue(moves.contains(5));
        assertTrue(moves.contains(8));
    }

    @Test
    public void testEvaluatePosition() {
        Game game = new Game();
        game.symbol = 'X';
        game.board = new char[]{'X', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' '};
        assertEquals(Game.INF, game.evaluatePosition(game.board, game.player1));
        assertEquals(-Game.INF, game.evaluatePosition(game.board, game.player2));

        game.symbol = 'O';
        game.board = new char[]{'O', 'O', 'O', ' ', ' ', ' ', ' ', ' ', ' '};
        assertEquals(-Game.INF, game.evaluatePosition(game.board, game.player1));
        assertEquals(Game.INF, game.evaluatePosition(game.board, game.player2));

        game.symbol = 'X';
        game.board = new char[]{'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'};
        assertEquals(0, game.evaluatePosition(game.board, game.player1));

        game.board = new char[]{'X', 'O', 'X', 'X', ' ', 'O', 'O', 'X', 'X'};
        assertEquals(-1, game.evaluatePosition(game.board, game.player1));
    }

    @Test
    public void testMiniMax() {
        Game game = new Game();
        game.board = new char[]{'X', 'O', 'X', 'X', 'O', 'O', 'O', ' ', 'X'};
        int move = game.MiniMax(game.board, game.player1);
        assertEquals(8, move); 
    }

    @Test
    public void testMiniMax2() {
        Game game = new Game();
        game.board = new char[]{'O', 'O', ' ', 'X', 'X', ' ', ' ', ' ', ' '};
        int move = game.MiniMax(game.board, game.player2);
        assertTrue(move == 3); 
    }

    @Test
    public void testTicTacToeCell() {
        TicTacToeCell cell = new TicTacToeCell(1, 2, 3);
        assertEquals(1, cell.getNum());
        assertEquals(2, cell.getCol());
        assertEquals(3, cell.getRow());
        assertEquals(' ', cell.getMarker());

        cell.setMarker("X");
        assertEquals('X', cell.getMarker());
    }

    @Test
    public void testUtility() {
        char[] boardChar = new char[]{'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'};
        Utility.print(boardChar);

        int[] boardInt = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        Utility.print(boardInt);

        ArrayList<Integer> moves = new ArrayList<>();
        moves.add(1);
        moves.add(2);
        Utility.print(moves);
        assertTrue(true);
    }
    
    @Test
    public void testTicTacToePanel() {
        TicTacToePanel panel = new TicTacToePanel(new java.awt.GridLayout(3, 3));
        assertNotNull(panel);
    }
}
