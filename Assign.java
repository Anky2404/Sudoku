/**
 * Represents an assignment of a number to a specific cell in the Sudoku game.
 * This class is used to facilitate the assignment of numbers to cells in the game.
 */
public class Assign {
    private int row; // Row index
    private int col; // Column index 
    private Sudoku game; // The Sudoku game instance

    /**
     * Constructs a new Assign object with the specified game, row, column, and number.
     * Assigns the given number to the specified cell in the Sudoku game.
     *
     * @param game   the Sudoku game instance
     * @param row    the row index of the cell to be assigned
     * @param col    the column index of the cell to be assigned
     * @param number the number to be assigned to the cell
     */
    public Assign(Sudoku game, int row, int col, String number) {
        this.game = game; // Initialize sudoku game
        this.row = row; // Set row
        this.col = col; // Set column 
        assignMove(number); // Assign  move number
    }

    /**
     * Assigns the given number to the cell specified by the row and column indices.
     *
     * @param number the number to be assigned to the cell
     */
    public void assignMove(String number) {
        // Calls make the move
        game.makeMove(String.valueOf(row), String.valueOf(col), number);
    }

    /**
     * Gets the row index of the cell assigned by this Assign object.
     *
     * @return the row index
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column index of the cell assigned by this Assign object.
     *
     * @return the column index
     */
    public int getCol() {
        return col;
    }

    /**
     * Gets the number assigned to the cell by this Assign object.
     *
     * @return the assigned number
     */
    public String getNumber() {
        return game.getIndividualMove(row, col); // Returns the current value
    }
}
