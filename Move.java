import java.io.Serializable;

/**
 * Represents a move in the Sudoku game.
 * This class is used to store information about a move, including
 * the position of the move and the previous value of the cell.
 */
public class Move implements Serializable {
    // Unique identifier for serialization
    private static final long serialVersionUID = 1L;

    // Row index
    int row;
    // Column index
    int col;
    // Previous value 
    String previousValue;

    /**
     * Constructs a new Move with the specified row, column, and previous value.
     *
     * @param row          the row index of the move
     * @param col          the column index of the move
     * @param previousValue the previous value of the cell before the move
     */
    Move(int row, int col, String previousValue) {
        this.row = row;
        this.col = col;
        this.previousValue = previousValue;
    }

    // Getters for the fields

    /**
     * Gets the row index of this move.
     *
     * @return the row index
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column index of this move.
     *
     * @return the column index
     */
    public int getCol() {
        return col;
    }

    /**
     * Gets the previous value of the cell before the move.
     *
     * @return the previous value
     */
    public String getPreviousValue() {
        return previousValue;
    }
}
