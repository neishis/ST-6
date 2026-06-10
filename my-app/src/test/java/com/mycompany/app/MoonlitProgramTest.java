package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class MoonlitProgramTest {
    @Test
    void emptyBoardHasNineSoftSpaces() {
        char[] pearlBoard = Program.createEmptyBoard();

        assertEquals(9, pearlBoard.length);
        for (char quietMark : pearlBoard) {
            assertEquals(Program.EMPTY_MARK, quietMark);
        }
    }

    @Test
    void freeCellsOnFreshBoardLookLikeFullConstellation() {
        List<Integer> lanternCells = Program.collectFreeCells(
            Program.createEmptyBoard());

        assertEquals(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8),
            lanternCells);
    }

    @Test
    void freeCellsSkipOccupiedCornersAndCenter() {
        char[] velvetBoard = {
            'X', ' ', 'O',
            ' ', 'X', ' ',
            'O', ' ', ' '
        };

        assertEquals(Arrays.asList(1, 3, 5, 7, 8),
            Program.collectFreeCells(velvetBoard));
    }

    @Test
    void oppositeMarkTurnsXIntoO() {
        assertEquals('O', Program.oppositeMark('X'));
    }

    @Test
    void oppositeMarkTurnsOIntoX() {
        assertEquals('X', Program.oppositeMark('O'));
    }

    @Test
    void oppositeMarkRejectsCrookedSymbol() {
        assertThrows(IllegalArgumentException.class,
            () -> Program.oppositeMark('Z'));
    }

    @Test
    void stateSeesTopRowXWin() {
        char[] velvetBoard = {
            'X', 'X', 'X',
            'O', ' ', 'O',
            ' ', ' ', ' '
        };

        assertEquals(OddOutcome.X_WIN, Program.checkState(velvetBoard));
    }

    @Test
    void stateSeesMiddleRowOWin() {
        char[] velvetBoard = {
            'X', ' ', 'X',
            'O', 'O', 'O',
            ' ', ' ', 'X'
        };

        assertEquals(OddOutcome.O_WIN, Program.checkState(velvetBoard));
    }

    @Test
    void stateSeesLeftColumnXWin() {
        char[] velvetBoard = {
            'X', 'O', ' ',
            'X', 'O', ' ',
            'X', ' ', ' '
        };

        assertEquals(OddOutcome.X_WIN, Program.checkState(velvetBoard));
    }

    @Test
    void stateSeesRightColumnOWin() {
        char[] velvetBoard = {
            'X', 'X', 'O',
            ' ', 'X', 'O',
            ' ', ' ', 'O'
        };

        assertEquals(OddOutcome.O_WIN, Program.checkState(velvetBoard));
    }

    @Test
    void stateSeesMainDiagonalXWin() {
        char[] velvetBoard = {
            'X', 'O', ' ',
            ' ', 'X', 'O',
            ' ', ' ', 'X'
        };

        assertEquals(OddOutcome.X_WIN, Program.checkState(velvetBoard));
    }

    @Test
    void stateSeesSideDiagonalOWin() {
        char[] velvetBoard = {
            'X', 'X', 'O',
            ' ', 'O', ' ',
            'O', ' ', 'X'
        };

        assertEquals(OddOutcome.O_WIN, Program.checkState(velvetBoard));
    }

    @Test
    void stateSeesDrawWhenBoardIsFullAndCalm() {
        char[] velvetBoard = {
            'X', 'O', 'X',
            'X', 'O', 'O',
            'O', 'X', 'X'
        };

        assertEquals(OddOutcome.DRAW, Program.checkState(velvetBoard));
    }

    @Test
    void stateKeepsPlayingWhenSpacesRemain() {
        char[] velvetBoard = {
            'X', 'O', 'X',
            ' ', 'O', ' ',
            ' ', 'X', ' '
        };

        assertEquals(OddOutcome.PLAYING, Program.checkState(velvetBoard));
    }

    @Test
    void minimaxTakesImmediateXVictory() {
        char[] velvetBoard = {
            'X', 'X', ' ',
            'O', 'O', ' ',
            ' ', ' ', ' '
        };

        assertEquals(2, Program.chooseBestMove(velvetBoard, 'X'));
    }

    @Test
    void minimaxTakesImmediateOVictory() {
        char[] velvetBoard = {
            'X', 'X', ' ',
            'O', 'O', ' ',
            'X', ' ', ' '
        };

        assertEquals(5, Program.chooseBestMove(velvetBoard, 'O'));
    }

    @Test
    void minimaxBlocksThreatBeforeWanderingAway() {
        char[] velvetBoard = {
            'X', 'X', ' ',
            ' ', 'O', ' ',
            ' ', ' ', ' '
        };

        assertEquals(2, Program.chooseBestMove(velvetBoard, 'O'));
    }

    @Test
    void minimaxReturnsMinusOneAfterFinishedGame() {
        char[] velvetBoard = {
            'O', 'O', 'O',
            'X', 'X', ' ',
            ' ', ' ', ' '
        };

        assertEquals(-1, Program.chooseBestMove(velvetBoard, 'X'));
    }

    @Test
    void boardTextDrawsDotsForEmptyCells() {
        char[] velvetBoard = {
            'X', ' ', 'O',
            ' ', 'X', ' ',
            'O', ' ', ' '
        };

        assertEquals("X|.|O" + System.lineSeparator()
            + ".|X|." + System.lineSeparator() + "O|.|.",
            Program.boardAsText(velvetBoard));
    }

    @Test
    void boardValidationRejectsShortBoard() {
        assertThrows(IllegalArgumentException.class,
            () -> Program.checkState(new char[] {'X', 'O'}));
    }

    @Test
    void boardValidationRejectsUnknownMark() {
        char[] velvetBoard = {
            'X', 'O', '?',
            ' ', ' ', ' ',
            ' ', ' ', ' '
        };

        assertThrows(IllegalArgumentException.class,
            () -> Program.collectFreeCells(velvetBoard));
    }

    @Test
    void bestMoveDoesNotDamageOriginalBoard() {
        char[] velvetBoard = {
            'X', 'X', ' ',
            'O', ' ', ' ',
            ' ', ' ', ' '
        };

        int silverMove = Program.chooseBestMove(velvetBoard, 'X');

        assertEquals(2, silverMove);
        assertEquals(' ', velvetBoard[2]);
        assertFalse(Program.collectFreeCells(velvetBoard).isEmpty());
    }

    @Test
    void mainRunsWithoutThrowingInTinyDemo() {
        assertDoesNotThrow(() -> Program.main(new String[0]));
    }
}
