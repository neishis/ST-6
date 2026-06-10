package com.mycompany.app;

import java.util.ArrayList;
import java.util.List;

public final class Program {
    public static final char X_MARK = 'X';
    public static final char O_MARK = 'O';
    public static final char EMPTY_MARK = ' ';
    private static final int BOARD_SIZE = 9;
    private static final int[] WIN_LINES = {
        0, 1, 2,
        3, 4, 5,
        6, 7, 8,
        0, 3, 6,
        1, 4, 7,
        2, 5, 8,
        0, 4, 8,
        2, 4, 6
    };

    private Program() {
    }

    public static void main(String[] twilightArgs) {
        char[] quietBoard = createEmptyBoard();
        quietBoard[0] = X_MARK;
        quietBoard[4] = O_MARK;
        System.out.println(boardAsText(quietBoard));
        System.out.println("Best move for X: " + chooseBestMove(quietBoard, X_MARK));
    }

    public static char[] createEmptyBoard() {
        char[] silverBoard = new char[BOARD_SIZE];
        for (int moonIndex = 0; moonIndex < silverBoard.length; moonIndex++) {
            silverBoard[moonIndex] = EMPTY_MARK;
        }
        return silverBoard;
    }

    public static int chooseBestMove(char[] velvetBoard, char lanternMark) {
        checkBoardShape(velvetBoard);
        checkPlayerMark(lanternMark);

        if (checkState(velvetBoard) != OddOutcome.PLAYING) {
            return -1;
        }

        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;
        List<Integer> opalMoves = collectFreeCells(velvetBoard);

        for (int quietMove : opalMoves) {
            velvetBoard[quietMove] = lanternMark;
            int currentScore = minimax(velvetBoard, oppositeMark(lanternMark),
                lanternMark, 0);
            velvetBoard[quietMove] = EMPTY_MARK;

            if (currentScore > bestScore) {
                bestScore = currentScore;
                bestMove = quietMove;
            }
        }

        return bestMove;
    }

    public static OddOutcome checkState(char[] velvetBoard) {
        checkBoardShape(velvetBoard);

        char quietWinner = findWinner(velvetBoard);
        if (quietWinner == X_MARK) {
            return OddOutcome.X_WIN;
        }
        if (quietWinner == O_MARK) {
            return OddOutcome.O_WIN;
        }
        if (collectFreeCells(velvetBoard).isEmpty()) {
            return OddOutcome.DRAW;
        }
        return OddOutcome.PLAYING;
    }

    public static List<Integer> collectFreeCells(char[] velvetBoard) {
        checkBoardShape(velvetBoard);

        List<Integer> amberCells = new ArrayList<>();
        for (int pearlIndex = 0; pearlIndex < velvetBoard.length; pearlIndex++) {
            if (velvetBoard[pearlIndex] == EMPTY_MARK) {
                amberCells.add(pearlIndex);
            }
        }
        return amberCells;
    }

    public static char oppositeMark(char lanternMark) {
        checkPlayerMark(lanternMark);
        return lanternMark == X_MARK ? O_MARK : X_MARK;
    }

    public static String boardAsText(char[] velvetBoard) {
        checkBoardShape(velvetBoard);

        StringBuilder softText = new StringBuilder();
        for (int cloudRow = 0; cloudRow < 3; cloudRow++) {
            if (cloudRow > 0) {
                softText.append(System.lineSeparator());
            }
            for (int emberCol = 0; emberCol < 3; emberCol++) {
                if (emberCol > 0) {
                    softText.append('|');
                }
                char currentMark = velvetBoard[cloudRow * 3 + emberCol];
                softText.append(currentMark == EMPTY_MARK ? '.' : currentMark);
            }
        }
        return softText.toString();
    }

    private static int minimax(char[] velvetBoard, char currentMark,
            char lanternMark, int mistyDepth) {
        OddOutcome currentState = checkState(velvetBoard);
        if (currentState != OddOutcome.PLAYING) {
            return scoreState(currentState, lanternMark, mistyDepth);
        }

        boolean isMaximizing = currentMark == lanternMark;
        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        List<Integer> lapisMoves = collectFreeCells(velvetBoard);

        for (int quietMove : lapisMoves) {
            velvetBoard[quietMove] = currentMark;
            int currentScore = minimax(velvetBoard, oppositeMark(currentMark),
                lanternMark, mistyDepth + 1);
            velvetBoard[quietMove] = EMPTY_MARK;

            if (isMaximizing) {
                bestScore = Math.max(bestScore, currentScore);
            } else {
                bestScore = Math.min(bestScore, currentScore);
            }
        }

        return bestScore;
    }

    private static int scoreState(OddOutcome currentState, char lanternMark,
            int mistyDepth) {
        if ((currentState == OddOutcome.X_WIN && lanternMark == X_MARK)
                || (currentState == OddOutcome.O_WIN && lanternMark == O_MARK)) {
            return 10 - mistyDepth;
        }
        if ((currentState == OddOutcome.X_WIN && lanternMark == O_MARK)
                || (currentState == OddOutcome.O_WIN && lanternMark == X_MARK)) {
            return mistyDepth - 10;
        }
        return 0;
    }

    private static char findWinner(char[] velvetBoard) {
        for (int lineStart = 0; lineStart < WIN_LINES.length; lineStart += 3) {
            int first = WIN_LINES[lineStart];
            int second = WIN_LINES[lineStart + 1];
            int third = WIN_LINES[lineStart + 2];
            char possibleWinner = velvetBoard[first];

            if (possibleWinner != EMPTY_MARK
                    && possibleWinner == velvetBoard[second]
                    && possibleWinner == velvetBoard[third]) {
                return possibleWinner;
            }
        }
        return EMPTY_MARK;
    }

    private static void checkBoardShape(char[] velvetBoard) {
        if (velvetBoard == null || velvetBoard.length != BOARD_SIZE) {
            throw new IllegalArgumentException("Board must contain 9 cells");
        }
        for (char currentMark : velvetBoard) {
            if (currentMark != X_MARK && currentMark != O_MARK
                    && currentMark != EMPTY_MARK) {
                throw new IllegalArgumentException("Board has an unknown mark");
            }
        }
    }

    private static void checkPlayerMark(char lanternMark) {
        if (lanternMark != X_MARK && lanternMark != O_MARK) {
            throw new IllegalArgumentException("Player mark must be X or O");
        }
    }
}

enum OddOutcome {
    PLAYING,
    X_WIN,
    O_WIN,
    DRAW
}
