package com.induce;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class GameTest {
    private Game game;

    @Before
    public void setUp() {
        System.setProperty("java.awt.headless", "true");
        game = new Game();
    }

    @Test
    public void constructorCreatesEmptyPlayingBoard() {
        Assert.assertEquals(State.PLAYING, game.state);
        Assert.assertEquals('X', game.player1.symbol);
        Assert.assertEquals('O', game.player2.symbol);
        for (char cell : game.board) {
            Assert.assertEquals(' ', cell);
        }
    }

    @Test
    public void checkStateFindsTopRowXWin() {
        char[] board = {'X', 'X', 'X', ' ', 'O', ' ', ' ', ' ', 'O'};
        Assert.assertEquals(State.XWIN, game.checkState(board));
    }

    @Test
    public void checkStateFindsMiddleRowOWin() {
        char[] board = {'X', ' ', 'X', 'O', 'O', 'O', ' ', ' ', ' '};
        Assert.assertEquals(State.OWIN, game.checkState(board));
    }

    @Test
    public void checkStateFindsColumnWin() {
        char[] board = {'O', 'X', ' ', 'O', 'X', ' ', 'O', ' ', 'X'};
        Assert.assertEquals(State.OWIN, game.checkState(board));
    }

    @Test
    public void checkStateFindsMainDiagonalWin() {
        char[] board = {'X', 'O', ' ', ' ', 'X', 'O', ' ', ' ', 'X'};
        Assert.assertEquals(State.XWIN, game.checkState(board));
    }

    @Test
    public void checkStateFindsAntiDiagonalWin() {
        char[] board = {'X', ' ', 'O', 'X', 'O', ' ', 'O', ' ', 'X'};
        Assert.assertEquals(State.OWIN, game.checkState(board));
    }

    @Test
    public void checkStateReportsDrawForFullBoardWithoutWinner() {
        char[] board = {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'};
        Assert.assertEquals(State.DRAW, game.checkState(board));
    }

    @Test
    public void checkStateReportsPlayingWhenMovesRemain() {
        char[] board = {'X', 'O', 'X', ' ', 'O', ' ', ' ', 'X', ' '};
        Assert.assertEquals(State.PLAYING, game.checkState(board));
    }

    @Test
    public void generateMovesReturnsEmptyCellsInOrder() {
        char[] board = {'X', ' ', 'O', ' ', 'X', ' ', 'O', ' ', 'X'};
        ArrayList<Integer> moves = new ArrayList<>();

        game.generateMoves(board, moves);

        Assert.assertEquals(Arrays.asList(1, 3, 5, 7), moves);
    }

    @Test
    public void generateMovesClearsPreviousContent() {
        ArrayList<Integer> moves = new ArrayList<>();
        moves.add(99);

        game.generateMoves(new char[] {'X', 'O', 'X', 'O', 'X', 'O', 'X', 'O', 'X'}, moves);

        Assert.assertTrue(moves.isEmpty());
    }

    @Test
    public void evaluatePositionRewardsOwnWin() {
        Player player = new Player();
        player.symbol = 'X';
        char[] board = {'X', 'X', 'X', ' ', 'O', ' ', ' ', ' ', 'O'};

        Assert.assertEquals(Game.INF, game.evaluatePosition(board, player));
    }

    @Test
    public void evaluatePositionPenalizesOpponentWin() {
        Player player = new Player();
        player.symbol = 'O';
        char[] board = {'X', 'X', 'X', ' ', 'O', ' ', ' ', ' ', 'O'};

        Assert.assertEquals(-Game.INF, game.evaluatePosition(board, player));
    }

    @Test
    public void evaluatePositionReturnsZeroForDraw() {
        Player player = new Player();
        player.symbol = 'O';
        char[] board = {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'};

        Assert.assertEquals(0, game.evaluatePosition(board, player));
    }

    @Test
    public void evaluatePositionReturnsMinusOneForUnfinishedGame() {
        Player player = new Player();
        player.symbol = 'X';
        char[] board = {'X', ' ', ' ', ' ', 'O', ' ', ' ', ' ', ' '};

        Assert.assertEquals(-1, game.evaluatePosition(board, player));
    }

    @Test
    public void minimaxTakesImmediateWinningMove() {
        char[] board = {'O', 'O', ' ', 'X', 'X', ' ', ' ', ' ', ' '};
        int move = game.MiniMax(board, game.player2);

        Assert.assertEquals(3, move);
    }

    @Test
    public void minimaxBlocksOpponentImmediateWin() {
        char[] board = {'X', 'X', ' ', ' ', 'O', ' ', ' ', ' ', ' '};
        int move = game.MiniMax(board, game.player2);

        Assert.assertEquals(3, move);
    }

    @Test
    public void minimaxUsesLastAvailableCell() {
        char[] board = {'X', 'O', 'X', 'O', 'X', ' ', 'O', 'X', 'O'};
        int move = game.MiniMax(board, game.player2);

        Assert.assertEquals(6, move);
    }

    @Test
    public void minimaxReturnsZeroForFullBoard() {
        char[] board = {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'};
        Assert.assertEquals(0, game.MiniMax(board, game.player2));
    }

    @Test
    public void minMoveSeesOpponentWinAsBad() {
        char[] board = {'X', 'X', 'X', ' ', 'O', ' ', ' ', ' ', 'O'};
        Assert.assertTrue(game.MinMove(board, game.player2) < 0);
    }

    @Test
    public void maxMoveSeesOwnWinAsGood() {
        char[] board = {'O', 'O', 'O', 'X', 'X', ' ', ' ', ' ', ' '};
        Assert.assertTrue(game.MaxMove(board, game.player2) > 0);
    }

    @Test
    public void playerDefaultFlagsAreFalse() {
        Player player = new Player();
        Assert.assertFalse(player.selected);
        Assert.assertFalse(player.win);
    }
}
