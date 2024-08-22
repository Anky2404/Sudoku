import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class SudokuGUI extends JFrame implements ActionListener {
    // Instance variables for game logic
    private Sudoku thegame;
    private JTextField[][] cells;
    private JButton saveButton, loadButton, clearButton, undoButton, quitButton;
    private JPanel actionPanel, gamePanel;
    private Set<Point> uneditableCells;

    // Constructor 
    public SudokuGUI() {
        thegame = new Sudoku(); // Initialize Sudoku game instance
        initializeUneditableCells(); 
        displayGame(); 
    }

    // Define uneditable cell
    private void initializeUneditableCells() {
        uneditableCells = new HashSet<>();
        int[][] uneditableCoordinates = {
            {0, 0}, {1, 1}, {2, 2}, {3, 1}, {3, 2}, {3, 3}, {3, 5}, {3, 8},
            {0, 1}, {0, 2}, {4, 0}, {4, 4}, {4, 5}, {4, 8}, {5, 2}, {5, 3},
            {5, 8}, {6, 1}, {6, 5}, {6, 7}, {6, 8}, {7, 2}, {7, 7}, {8, 0},
            {8, 6}, {8, 7}
        };
        for (int[] coord : uneditableCoordinates) {
            uneditableCells.add(new Point(coord[0], coord[1])); 
        }
    }

    //  display the game board
    public void displayGame() {
        setTitle("Sudoku Game");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        int size = thegame.getGameSize(); // Get size of the Sudoku grid
        cells = new JTextField[size][size]; 

        actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Panel for buttons
        gamePanel = new JPanel(new GridLayout(size, size)); // Panel for the Sudoku grid

        Border border = BorderFactory.createLineBorder(Color.BLACK, 2); // Cell border
        Font font = new Font("Arial", Font.BOLD, 15); // Font for cell text

        // Loop to create  grid panel
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                JTextField cell = new JTextField(); // Create a new cell
                cell.setPreferredSize(new Dimension(40, 40)); // Size of each cell
                cell.setBorder(border); // Set cell border
                cell.setHorizontalAlignment(JTextField.CENTER); // Center-align text
                cell.setFont(font); // Set text font

                // Determine if the cell should be editable
                boolean isEditable = !uneditableCells.contains(new Point(row, col));
                cell.setEditable(isEditable);
                cell.setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY); // Color based on editability

                // Set the initial text of the cell
                String value = thegame.getIndividualMove(row, col);
                cell.setText(value.equals("-") ? "" : value);

                // Restrict input to a single digit 
                ((AbstractDocument) cell.getDocument()).setDocumentFilter(new SingleDigitDocument());

                // handle cell input changes
                cell.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent e) {
                            JTextField source = (JTextField) e.getSource();
                            int[] indices = getCellIndices(source);
                            if (indices != null) {
                                int cellRow = indices[0];
                                int cellCol = indices[1];
                                
                                handleCellInput(cellRow, cellCol, source);
                            }
                        }
                    });

                cells[row][col] = cell; // Store cell 
                gamePanel.add(cell); // Add cell to the grid panel
            }
        }

        // Configure buttons and add panels to the frame
        configureButtons();
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(actionPanel, BorderLayout.NORTH);
        containerPanel.add(gamePanel, BorderLayout.CENTER);
        add(containerPanel);

        setVisible(true); // Make the frame visible
    }

    // Get the row and column 
    private int[] getCellIndices(JTextField cell) {
        for (int row = 0; row < thegame.getGameSize(); row++) {
            for (int col = 0; col < thegame.getGameSize(); col++) {
                if (cells[row][col] == cell) {
                    return new int[]{row, col}; // Return row and column indices
                }
            }
        }
        return null; 
    }

    // action buttons and their properties
    private void configureButtons() {
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        clearButton = new JButton("Clear");
        undoButton = new JButton("Undo");
        quitButton = new JButton("Quit");

        Font buttonFont = new Font("Arial", Font.BOLD, 15); // Font for buttons
        Dimension buttonSize = new Dimension(100, 30); // Size of buttons
        JButton[] buttons = {saveButton, loadButton, clearButton, undoButton, quitButton};
        for (JButton button : buttons) {
            button.setPreferredSize(buttonSize);
            button.setFont(buttonFont);
            button.setBackground(Color.WHITE);
            button.setMargin(new Insets(0, 0, 0, 0));
            button.addActionListener(this); // Add action listener
        }
        quitButton.addActionListener(e -> System.exit(0)); // Exit application

        // Add buttons to the action panel
        actionPanel.add(saveButton);
        actionPanel.add(loadButton);
        actionPanel.add(clearButton);
        actionPanel.add(undoButton);
        actionPanel.add(quitButton);
    }

    // Handle user input 
    private void handleCellInput(int row, int col, JTextField cell) {
        if (uneditableCells.contains(new Point(row, col))) {
            return; // Prevent changes to uneditable cells
        }
        String text = cell.getText();
        if (text.isEmpty() || text.matches("[1-9]")) {
            if (thegame.makeMove(String.valueOf(row), String.valueOf(col), text)) {
                if (thegame.checkWin()) {
                    JOptionPane.showMessageDialog(this, "Congratulations! You win!"); 
                }
            } else {
                cell.setText(""); 
                JOptionPane.showMessageDialog(this, "Invalid move! Try again."); 
            }
        } else {
            cell.setText(""); 
            JOptionPane.showMessageDialog(this, "Invalid input! Only digits 1-9 are allowed."); 
        }
    }

    // Handle actions for buttons 
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == saveButton) {
            thegame.saveGameToFile(); // Save current game
            JOptionPane.showMessageDialog(this, "Game Saved!"); 
        } else if (source == loadButton) {
            Sudoku loadedGame = Sudoku.loadGame(); // Load game
            if (loadedGame != null) {
                thegame = loadedGame;
                updateBoard(); 
                JOptionPane.showMessageDialog(this, "Game Loaded!"); 
            } else {
                JOptionPane.showMessageDialog(this, "Failed to Load Game."); 
            }
        } else if (source == clearButton) {
            thegame.clearBoard(); // Clear the game board
            updateBoard(); // Update board 
            JOptionPane.showMessageDialog(this, "Board Cleared!"); 
        } else if (source == undoButton) {
            if (thegame.undoLastMove()) {
                updateBoard(); // Update board after undo move
                JOptionPane.showMessageDialog(this, "Undone Last Move!");
            } else {
                JOptionPane.showMessageDialog(this, "No Move to Undo."); 
            }
        }
    }

    // Update the game board
    private void updateBoard() {
        if (thegame.getMoves() == null) {
            return; 
        }
        int size = thegame.getGameSize();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                String value = thegame.getIndividualMove(row, col);
                cells[row][col].setText(value.equals("-") ? "" : value);
                cells[row][col].setEditable(!uneditableCells.contains(new Point(row, col)));
                cells[row][col].setBackground(uneditableCells.contains(new Point(row, col)) ? Color.LIGHT_GRAY : Color.WHITE);
            }
        }
    }

    // allow only single digit input
    private static class SingleDigitDocument extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("[1-9]")) {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + string + currentText.substring(offset);
                if (newText.length() > 1) {
                    newText = newText.substring(newText.length() - 1); 
                }
                super.replace(fb, 0, fb.getDocument().getLength(), newText, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches("[1-9]") || text.isEmpty()) {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
                if (newText.length() > 1) {
                    newText = newText.substring(newText.length() - 1); // Keep only the last character
                }
                super.replace(fb, 0, fb.getDocument().getLength(), newText, attrs);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length); 
        }
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuGUI::new); 
    }
}
