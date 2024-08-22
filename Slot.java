import java.io.Serializable;

/**
 * Represents a slot in the Sudoku grid.
 * This class is used to store information about each cell in the grid,
 * including its position, state, and whether it is fillable by the user.
 */
public class Slot implements Serializable {
    // Unique identifier for serialization
    private static final long serialVersionUID = 1L;

    // Column index 
    private int col;
    // Row index 
    private int row;
    // State of the slot
    private String state;
    private boolean fillable;

    /**
     * Constructs a new Slot with the specified column, row, state, and fillable status.
     *
     * @param col     the column index of the slot
     * @param row     the row index of the slot
     * @param state   the state of the slot
     * @param fillable whether the slot can be filled by the user
     */
    public Slot(int col, int row, String state, boolean fillable) {
        this.col = col;
        this.row = row;
        this.state = state;
        this.fillable = fillable;
    }

    /**
     * Gets the column index of this slot.
     *
     * @return the column index
     */
    public int getCol() {
        return col;
    }

    /**
     * Gets the row index of this slot.
     *
     * @return the row index
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the current state of this slot.
     *
     * @return the state of the slot
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the state of this slot.
     *
     * @param state the new state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Checks if this slot can be filled by the user.
     *
     * @return true if the slot is fillable, false otherwise
     */
    public boolean getFillable() {
        return fillable;
    }
}
