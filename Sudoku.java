import java.io.*;
import java.util.*;

/**
 * Represents a Sudoku game, including methods for making moves, checking for wins,
 * saving/loading game states, and undoing moves.
 */
public class Sudoku implements Serializable {
    private static final long serialVersionUID = 1L;

    private transient Slot[][] populatedBoard; // Current game board
    private transient Slot[][] defaultBoard;   // Initial board setup
    private transient Slot[][] solutionBoard;  // Solution board 
    private int gameSize;                      // Size of the Sudoku board
    private transient Stack<Move> moveHistory; // move history
    private String solutionFile = "Solutions/su1solution.txt"; // Default solution file path

    /**
     * Constructs a new Sudoku game and initializes it by loading a level and solution.
     */
    public Sudoku() {
        moveHistory = new Stack<>();
        try {
            initializeGame();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the game board and loads the initial setup and solution from files.
     *
     * @throws FileNotFoundException if the level or solution file is not found
     */
    private void initializeGame() throws FileNotFoundException {
        Scanner reader = new Scanner(new File("Levels/su1.txt")); // Load level data
        gameSize = Integer.parseInt(reader.next()); // Read the size of the board
        populatedBoard = new Slot[gameSize][gameSize];
        defaultBoard = new Slot[gameSize][gameSize];
        solutionBoard = new Slot[gameSize][gameSize];

        // Initialize the board
        for (int row = 0; row < gameSize; row++) {
            for (int col = 0; col < gameSize; col++) {
                populatedBoard[row][col] = new Slot(col, row, "-", true);
                defaultBoard[row][col] = new Slot(col, row, "-", true);
            }
        }

        // Load initial moves
        while (reader.hasNext()) {
            int row = Integer.parseInt(reader.next());
            int col = Integer.parseInt(reader.next());
            String move = reader.next();
            populatedBoard[row][col] = new Slot(col, row, move, move.equals("-"));
            defaultBoard[row][col] = new Slot(col, row, move, move.equals("-"));
        }
        reader.close();

        // Load solution
        try (Scanner solutionScanner = new Scanner(new File(solutionFile))) {
            while (solutionScanner.hasNext()) {
                int row = Integer.parseInt(solutionScanner.next());
                int col = Integer.parseInt(solutionScanner.next());
                String move = solutionScanner.next();
                solutionBoard[row][col] = new Slot(col, row, move, false);
            }
        }
    }

   
    public Slot[][] getMoves() {
        return populatedBoard;
    }

    
    public String getIndividualMove(int row, int col) {
        if (populatedBoard == null) {
            throw new IllegalStateException("populatedBoard is not initialized.");
        }
        return populatedBoard[row][col].getState();
    }

    
    public int getGameSize() {
        return gameSize;
    }

   
    public boolean makeMove(String row, String col, String number) {
        int enteredRow = Integer.parseInt(row);
        int enteredCol = Integer.parseInt(col);
        if (populatedBoard[enteredRow][enteredCol].getFillable()) {
            saveState(); // Save the current state before making the move
            moveHistory.push(new Move(enteredRow, enteredCol, populatedBoard[enteredRow][enteredCol].getState()));
            populatedBoard[enteredRow][enteredCol].setState(number);
            return true;
        } else {
            return false;
        }
    }

    
    public boolean checkWin() {
        for (int i = 0; i < gameSize; i++) {
            for (int c = 0; c < gameSize; c++) {
                if (!populatedBoard[i][c].getState().equals(solutionBoard[i][c].getState())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Saves the current state of the board
     */
    private void saveState() {
        Slot[][] boardCopy = new Slot[gameSize][gameSize];
        for (int i = 0; i < gameSize; i++) {
            for (int j = 0; j < gameSize; j++) {
                Slot original = populatedBoard[i][j];
                boardCopy[i][j] = new Slot(original.getCol(), original.getRow(), original.getState(), original.getFillable());
            }
        }
        moveHistory.push(new Move(-1, -1, "SAVE")); // Push a special move to indicate a saved state
    }

    /**
     * Undoes the last move made.
     */
    public boolean undoLastMove() {
        if (moveHistory.isEmpty()) {
            return false;
        }
        Move lastMove = moveHistory.pop();
        if (lastMove.row == -1 && lastMove.col == -1) {
            return true;
        }
        populatedBoard[lastMove.row][lastMove.col].setState(lastMove.previousValue);
        return true;
    }

    /**
     * Clears the board
     */
    public void clearBoard() {
        moveHistory.clear();
        for (int i = 0; i < gameSize; i++) {
            for (int j = 0; j < gameSize; j++) {
                if (populatedBoard[i][j].getFillable()) {
                    populatedBoard[i][j].setState(defaultBoard[i][j].getState());
                }
            }
        }
        saveState();
    }

    /**
     * Saves the current game state to a file.
     */
    public void saveGameToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("solutions/saveGame.txt"))) {
            writer.write(gameSize + "\n");
            for (int row = 0; row < gameSize; row++) {
                for (int col = 0; col < gameSize; col++) {
                    writer.write(populatedBoard[row][col].getState() + " ");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a saved game state from a file.
     */
    public void loadGameFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("solutions/saveGame.txt"))) {
            gameSize = Integer.parseInt(reader.readLine().trim());
            populatedBoard = new Slot[gameSize][gameSize];
            defaultBoard = new Slot[gameSize][gameSize];
            for (int row = 0; row < gameSize; row++) {
                String[] line = reader.readLine().trim().split(" ");
                for (int col = 0; col < gameSize; col++) {
                    populatedBoard[row][col] = new Slot(col, row, line[col], true);
                    defaultBoard[row][col] = new Slot(col, row, line[col], true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Static method to create a new Sudoku instance and load a saved game state.
     *
     * @return a new Sudoku instance with the loaded game state
     */
    public static Sudoku loadGame() {
        Sudoku game = new Sudoku();
        game.loadGameFromFile();
        return game;
    }
}
