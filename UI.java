import java.io.*;
import javax.swing.*;
import java.util.*;


public class UI {
    private Sudoku thegame; 
    private String menuChoice; 
    private Scanner reader; 

    /**
     * Initializes the UI, starts the game loop, and handles user input.
     */
    public UI() {
        thegame = new Sudoku(); // Initialize the Sudoku game
        reader = new Scanner(System.in); // Initialize scanner
        menuChoice = "";
        while (!menuChoice.equalsIgnoreCase("Q")) { 
            displayGame(); // Display the current state of the game
            menu(); // Show the menu options
            menuChoice = getChoice(); // Get the user's choice
            if (thegame.checkWin()) { // Check if the game is won
                winningAnnouncement(); 
                break; // Exit the loop 
            }
        }
    }

    
    public void winningAnnouncement() {
        System.out.println("Congratulations, you solved the puzzle!");
    }

    /**
     * Displays the current state 
     */
    public void displayGame() {
        int size = thegame.getGameSize(); // Get the size of the game grid
        String separator = size == 9 ? " - - - - - - - - -" : " - - - -"; 
        System.out.println("Col   " + String.join(" ", "0", "1", "2", "3", size == 9 ? "4" : ""));
        System.out.println("      " + separator);

        for (int i = 0; i < size; i++) {
            System.out.print("Row " + i + "|");
            for (int c = 0; c < size; c++) {
                String move = thegame.getIndividualMove(i, c); // Get the move at the current position
                if (move.equals("-")) {
                    System.out.print(" ."); // Display empty cell
                } else {
                    System.out.print(" " + move); // Display cell value
                }
                if (size == 9 && (c == 2 || c == 5 || c == 8)) {
                    System.out.print("|"); // Vertical line for 9x9 grid
                } else if (size == 4 && (c == 1 || c == 3)) {
                    System.out.print("|"); // Vertical line for 4x4 grid
                }
            }
            System.out.println();
            if (size == 9 && (i == 2 || i == 5 || i == 8)) {
                System.out.println("      " + separator); // Horizontal line for 9x9 grid
            } else if (size == 4 && (i == 1 || i == 3)) {
                System.out.println("      " + separator); // Horizontal line for 4x4 grid
            }
        }
    }

    /**
     * Displays the menu options to the user.
     */
    public void menu() {
        System.out.println("Please select an option: \n"
                + "[M] make move\n"
                + "[S] save game\n"
                + "[L] load saved game\n"
                + "[U] undo move\n"
                + "[C] clear game\n"
                + "[Q] quit game\n");
    }

    /**
     * Reads and processes the user's menu choice.
     *
     * @return the user's choice
     */
    public String getChoice() {
        String choice = reader.next(); // Read the user's choice
        switch (choice.toUpperCase()) {
            case "M":
                makeMove(); // Handle making a move
                break;
            case "S":
                saveGame(); // Handle saving the game
                break;
            case "U":
                undoMove(); // Handle undoing the last move
                break;
            case "L":
                loadGame(); // Handle loading a saved game
                break;
            case "C":
                clearGame(); // Handle clearing the game board
                break;
            case "Q":
                System.exit(0); // Exit the program
                break;
            default:
                System.out.println("Invalid choice. Please try again."); // Handle invalid input
        }
        return choice; // Return the user's choice
    }

    /**
     * Saves the current state of the game to a file.
     */
    public void saveGame() {
        thegame.saveGameToFile(); // Save the game to file
        JOptionPane.showMessageDialog(null, "Game saved successfully."); 
    }

    /**
     * Undoes the last move made by the user.
     */
    public void undoMove() {
        if (thegame.undoLastMove()) {
            System.out.println("Last move undone."); 
        } else {
            System.out.println("No move to undo."); // Notify user if no move to undo
        }
    }

    /**
     * Loads a previously saved game from a file.
     */
    public void loadGame() {
        thegame.loadGameFromFile(); // Load the game from file
        System.out.println("Game loaded successfully."); 
    }

    /**
     * Clears the game board.
     */
    public void clearGame() {
        thegame.clearBoard(); // Clear the game board
        System.out.println("Game has been cleared."); 
    }

    /**
     * Handles making a move in the game.
     */
    private void makeMove() {
        String row, col, number;
        while (true) {
            System.out.print("Which row is the cell you wish to fill? ");
            row = reader.next(); // Get the row index
            System.out.print("Which column is the cell you wish to fill? ");
            col = reader.next(); // Get the column index
            System.out.print("Which number do you want to enter? ");
            number = reader.next(); // Get the number to enter
            if (thegame.makeMove(row, col, number)) {
                break; // Exit loop if move is valid
            } else {
                System.out.println("Invalid move. Please try again."); 
            }
        }
    }

    /**
     * Main method to start the UI application.
     */
    public static void main(String[] args) {
        new UI(); 
    }
}
