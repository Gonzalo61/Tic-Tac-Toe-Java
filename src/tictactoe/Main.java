
package tictactoe;
import java.util.*;
public class Main {
    public static class ExcepcionFieldFull extends Exception {
        public ExcepcionFieldFull(String msg) {
            super(msg);
        }
    }

    public static void fullfield(int coordX, int coordY, char[][] tableGame) throws ExcepcionFieldFull {
        char valorTableGame = tableGame[coordX][coordY];
        if (valorTableGame != '_') {
            throw new ExcepcionFieldFull("This cell is occupied! Choose another one!");
        }
    }

    public static void main(String[] args) {
        // write your code here
        Scanner s = new Scanner(System.in);
        TicTacToee ticTacToee = new TicTacToee("_________");
        ticTacToee.startGame();

        while (ticTacToee.getState() != State.FINISHED) {
                System.out.print("Enter the coordinates: ");
                try {
                    int coordY = s.nextInt() - 1;
                    int coordX = newCoordinate((s.nextInt()));

                    fullfield(coordX, coordY, ticTacToee.getTable());

                    ticTacToee.writeTable(coordX, coordY);
                    ticTacToee.verifyGame();

                    if (ticTacToee.getState() != State.FINISHED) {
                        ticTacToee.printTable(ticTacToee.getTable());
                    }

                } catch (InputMismatchException e) {
                    s.nextLine();
                    System.out.print("You should enter numbers!\n");
                } catch (ArrayIndexOutOfBoundsException e) {
                    s.nextLine();
                    System.out.print("Coordinates should be from 1 to 3!\n");
                } catch (ExcepcionFieldFull e) {
                    System.out.println(e.getMessage());
                }
        }
    }
    public static int newCoordinate(int x) {
        int newValor = x;
        if (x == 3) {
            newValor = 0;
        } else if (x == 2) {
            newValor = 1;
        } else if (x == 1) {
            newValor = 2;
        }
        return newValor;
    }
}

enum State {
    WAITING, READY, X_TURN, O_TURN, FINISHED
}

class TicTacToee {
    private static final String delimiter = "---------";
    private String game;
    private char[][] tableGame;
    private State state = State.WAITING;
    private int countPlays;


    public TicTacToee(String game) {
        this.game = game;
    }


    public void startGame() {
        this.tableGame = initTicTacToe(game);
        this.state = State.READY;
        printTable(tableGame);
    }

    public char[][] getTable() {
        return this.tableGame;
    }

    public State getState() {
        return this.state;
    }

    public void writeTable(int x, int y) {
        switch (this.state) {
            case READY:
            case X_TURN:
                tableGame[x][y] = 'X';
                this.state = State.O_TURN;
                this.countPlays++;
                break;
            case O_TURN:
                tableGame[x][y] = 'O';
                this.state = State.X_TURN;
                this.countPlays++;
                break;
        }
    }

    public void verifyGame() {
        if (this.countPlays >= 5 ) {
            examineCount(this.tableGame);
        }
    }


    public char[][] initTicTacToe(String game) {
        char[][] tictactoe = new char[3][3];
        int index = 0;
        int index2 = 0;
        while (index < 9) {
            char[] prov = new char[3];
            String split = game.substring(index, index + 3);
            for (int i = 0; i < 3; i++) {
                char character = split.charAt(i);
                prov[i] = character;
            }
            tictactoe[index2] = prov;
            index += 3;
            index2 += 1;
        }
        return tictactoe;
    }

    public void printTable(char[][] tableGame) {
        System.out.println(delimiter);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == 0) {
                    System.out.print("| " + tableGame[i][j] + " ");
                } else if (j == 2) {
                    System.out.println(tableGame[i][j] + " |");
                } else {
                    System.out.print(tableGame[i][j] + " ");
                }
            }
        }
        System.out.println(delimiter);
    }

    public void examineCount(char[][] tableGame) {
        int countX = 0;
        int countO = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                char charActual = tableGame[i][j];
                if (charActual == 'X') {
                    countX++;
                } else if (charActual == 'O') {
                    countO++;
                }
            }
        }

        int abs = Math.abs(countO - countX);

        if (abs <= 1) {
            evaluateHorizontal(tableGame);
        } else {
            System.out.println("Impossible");
        }
    }

    private void evaluateHorizontal(char[][] tableGame) {
        int countX = 1;
        int countO = 1;
        boolean xFull = false;
        boolean oFull = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3 - 1; j++) {
                char charActual = tableGame[i][j];
                if (!xFull) {
                    if (charActual == 'X') {
                        char charNext = tableGame[i][j + 1];
                        if (charActual == charNext) {
                            countX++;
                        } else {
                            countX = 1;
                        }
                    }
                    if (countX == 3) {
                        xFull = true;
                        break;
                    }
                }
                if (!oFull) {
                    if (charActual == 'O') {
                        char charNext = tableGame[i][j + 1];
                        if (charActual == charNext) {
                            countO++;
                        } else {
                            countO = 1;
                        }
                    }
                    if (countO == 3) {
                        oFull = true;
                        break;
                    }
                }
            }
        }
        if (!xFull && !oFull) {
            examineVertical(tableGame);
        } else {
            determineResult(xFull, oFull, tableGame);
        }
    }

    private void examineVertical(char[][] tableGame) {
        int countO = 1;
        int countX = 1;
        boolean xFull = false;
        boolean oFull = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3 - 1; j++) {
                char charActual = tableGame[j][i];
                if (!xFull) {
                    if (charActual == 'X') {
                        char charNext = tableGame[j + 1][i];
                        if (charActual == charNext) {
                            countX++;
                        } else {
                            countX = 1;
                        }
                    }
                    if (countX == 3) {
                        xFull = true;
                        break;
                    }
                }
                if (!oFull) {
                    if (charActual == 'O') {
                        char charNext = tableGame[j + 1][i];
                        if (charActual == charNext) {
                            countO++;
                        } else {
                            countO = 1;
                        }
                    }
                    if (countO == 3) {
                        oFull = true;
                        break;
                    }
                }
            }
        }

        if (!xFull && !oFull) {
            examineDiagonal(tableGame);
        } else {
            determineResult(xFull, oFull, tableGame);
        }
    }

    private void examineDiagonal(char[][] tableGame) {
        int countO = 1;
        int countX = 1;
        boolean xFull = false;
        boolean oFull = false;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                if (i == j) {
                    char charActual = tableGame[i][j];
                    char charNext = tableGame[i + 1][j + 1];
                    if (charActual == charNext) {
                        if (charActual == 'X') {
                            countX++;
                        } else if (charActual == 'O') {
                            countO++;
                        }
                    } else {
                        countX = 1;
                        countO = 1;
                        break;
                    }
                    if (countX == 3) {
                        xFull = true;
                        break;
                    } else if (countO == 3) {
                        oFull = true;
                        break;
                    }
                }
            }
        }
        if (!xFull && !oFull) {
            examineAntidiagonal(tableGame);
        } else {
            determineResult(xFull, oFull, tableGame);
        }
    }

    private void examineAntidiagonal(char[][] tableGame) {
        int countO = 1;
        int countX = 1;
        boolean xFull = false;
        boolean oFull = false;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if (i + j == tableGame.length - 1) {
                    char charActual = tableGame[i][j];
                    char charNext = tableGame[i + 1][j - 1];
                    if (charActual == charNext) {
                        if (charActual == 'X') {
                            countX++;
                        } else if (charActual == 'O') {
                            countO++;
                        }
                    } else {
                        countX = 1;
                        countO = 1;
                        break;
                    }
                    if (countX == 3) {
                        xFull = true;
                        break;
                    } else if (countO == 3) {
                        oFull = true;
                        break;
                    }
                }
            }
        }
        determineResult(xFull, oFull, tableGame);
    }


    private void examineFieldsEmpty(char[][] tableGame, boolean xFull, boolean oFull) {
        if (!xFull && !oFull) {
            int countEmptyFields = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    char charActual = tableGame[i][j];
                    if (charActual == '_') {
                        countEmptyFields++;
                    }
                }
            }
            //if (countEmptyFields >= 1) {
                if (countEmptyFields == 0) {
            //    System.out.println("Game not finished");
            //    this.state = State.FINISHED;
            //} else {
                    printTable(tableGame);
                System.out.println("Draw");
                this.state = State.FINISHED;
            }
        }
    }

    private void determineResult(boolean xFull, boolean oFull, char[][] tableGame) {

        if (xFull && oFull) {
            printTable(tableGame);
            System.out.println("Impossible");
            this.state = State.FINISHED;
        } else if (!xFull && !oFull) {
            examineFieldsEmpty(tableGame, xFull, oFull);
        } else if (xFull || oFull) {
            if (xFull) {
                printTable(tableGame);
                System.out.println("X wins");
                this.state = State.FINISHED;
            } else {
                printTable(tableGame);
                System.out.println("O wins");
                this.state = State.FINISHED;
            }
        }
    }
}


