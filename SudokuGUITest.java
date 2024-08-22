import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SudokuGUITest {
    private Sudoku game;

    @BeforeEach
    public void setUp() throws IOException {
        // Initialize Sudoku game
        game = new Sudoku();
        resetTestFiles();
    }

    @AfterEach
    public void tearDown() {
    }

    // reset test files
    private void resetTestFiles() throws IOException {
        File saveFile = new File("solutions/saveGame.txt");
        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs(); 
        }
        // Initialize the save file
        Files.write(Paths.get("solutions/saveGame.txt"), "9\n".getBytes()); 
        for (int i = 0; i < 9; i++) {
            String line = String.join(" ", new String[9]).trim();
            Files.write(Paths.get("solutions/saveGame.txt"), (line + "\n").getBytes(), StandardOpenOption.APPEND);
        }
    }

    @Test
    @Order(1)
    public void testMakeMove() {
        System.out.println("\nTEST 1 : Test make move\n");
        // input value
        String row = "0";
        String col = "6";
        String value = "7";
        boolean result = game.makeMove(row, col, value);
        assertTrue(result, "Move should be successful");
        assertEquals("7", game.getIndividualMove(0, 6), "Cell (0,6) should contain '7'");
    }

    @Test
    @Order(2)
    public void testCheckWin() {
        System.out.println("\nTEST 2 : Test check win\n");
        // set up user input
        setupWinningBoard();
        assertTrue(game.checkWin(), "The game should be in a winning state");
    }

    @Test
    @Order(3)
    public void testClearBoard() {
        System.out.println("\nTEST 3 : Test clear Board\n");
        // input value
        String row = "0";
        String col = "3";
        String value = "5";
        
        game.makeMove(row, col, value);
        // clear board
        game.clearBoard();
        assertEquals("-", game.getIndividualMove(0, 3), "Cell (0,3) should be cleared to '-'");
    }

    @Test
    @Order(4)
    public void testUndoMove() {
        System.out.println("\nTEST 4 : Test undo move\n");
        // input value
        String row = "0";
        String col = "5";
        String value = "5";
        
        game.makeMove(row, col, value);
        assertEquals("5", game.getIndividualMove(0, 5), "Cell (0,5) should contain '5'");
        // undo move
        game.undoLastMove();
        assertEquals("-", game.getIndividualMove(0, 5), "Cell (0,5) should be reverted to '-'");
    }

    @Test
    @Order(5)
    public void testSaveGameToFile() throws IOException {
        System.out.println("\nTEST 5 : Test save game\n");
        // input value
        String row = "0";
        String col = "5";
        String value = "5";
        
        game.makeMove(row, col, value);
        // save game
        game.saveGameToFile();
        
        // Read the saved file
        String content = new String(Files.readAllBytes(Paths.get("solutions/saveGame.txt")));
        assertTrue(content.contains("5"), "The saved file should contain the move '5'");
    }

    @Test
    @Order(6)
    public void testLoadGameFromFile() throws IOException {
        System.out.println("\nTEST 6 : Test load game\n");
        // input value
        String row = "0";
        String col = "5";
        String value = "5";
        
        game.makeMove(row, col, value);
        //Save current state
        game.saveGameToFile();
        // reload game
        Sudoku newGame = Sudoku.loadGame();
        assertEquals("5", newGame.getIndividualMove(0, 5), "Loaded game should contain the move '5'");
    }

    // Helper method to set up a winning board
    private void setupWinningBoard() {
        // Input value by user
        String[][] winningBoard = {
            {"9", "8", "6", "4", "5", "1", "3", "7", "2"},
            {"1", "4", "3", "7", "8", "2", "5", "9", "6"},
            {"5", "7", "2", "6", "3", "9", "4", "8", "1"},
            {"3", "5", "4", "1", "9", "6", "8", "2", "7"},
            {"2", "1", "7", "8", "4", "5", "6", "3", "9"},
            {"6", "9", "8", "2", "7", "3", "1", "5", "4"},
            {"8", "6", "1", "3", "2", "7", "9", "4", "5"},
            {"4", "2", "9", "5", "1", "8", "7", "6", "3"},
            {"7", "3", "5", "9", "6", "4", "2", "1", "9"}
        };

        // Populate the board
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                game.makeMove(String.valueOf(row), String.valueOf(col), winningBoard[row][col]);
            }
        }
    }
}
