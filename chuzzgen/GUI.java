package chuzzgen;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;

public class GUI
{
    private JFrame frame;

    public GUI(Board board)
    {
        makeFrame(board);
    }
    
    private void makeFrame(Board board)
    {
    	frame = new JFrame("Generated Puzzle");
    	Container frameContentPane = frame.getContentPane();
    	
    	// Set the MenuBar
    	frame.setJMenuBar(makeMenuBar());
    	
    	JTextPane evalDisplay = new JTextPane();
    	evalDisplay.setText(String.format("Find the Mate in %d (%s to play)", board.getStockfishEval(), Game.player1.getColour()));
    	evalDisplay.setEditable(false);
    	evalDisplay.setBackground(null);
    	JTextPane FENDisplay = new JTextPane();
        FENDisplay.setText("FEN: " + board.getFEN());
    	FENDisplay.setEditable(false);
    	FENDisplay.setBackground(null);
    	
    	JPanel boardPanel = new JPanel();
    	boardPanel.setLayout(new BorderLayout());
    	boardPanel.add(board);
    	
    	JPanel textPanel = new JPanel();
    	textPanel.setLayout(new GridLayout(2, 1));
    	textPanel.add(evalDisplay);
    	textPanel.add(FENDisplay);
    	
    	JPanel mainPanel = new JPanel();
    	mainPanel.setLayout(new BorderLayout());
    	mainPanel.add(textPanel, BorderLayout.NORTH);
    	mainPanel.add(boardPanel, BorderLayout.CENTER);
        
    	frameContentPane.add(mainPanel);
        frame.setPreferredSize(new Dimension(800, 900));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    private JMenuBar makeMenuBar() {
    	JMenuBar menuBar = new JMenuBar();
    	JMenu optionsMenu = new JMenu("Game");
    	JMenuItem newPuzzleOption = new JMenuItem("Generate New Puzzle");
    	//newPuzzleOption.addActionListener(e -> Game.generateBoard());
    	newPuzzleOption.addActionListener(e -> generateNewBoard(Game.board.getCurrentDifficulty()));
    	JMenuItem changeDifficultyOption = new JMenuItem("Change Difficulty");
    	changeDifficultyOption.addActionListener(e -> chooseDifficulty());
    	JMenuItem copyFENOption = new JMenuItem("Copy FEN");
    	copyFENOption.addActionListener(e -> copyFEN());
    	
    	optionsMenu.add(newPuzzleOption);
    	optionsMenu.add(changeDifficultyOption);
    	optionsMenu.add(copyFENOption);
    	menuBar.add(optionsMenu);
    	return menuBar;
    }
    
    private void generateNewBoard(int difficulty) {
    	// Start a new Game (generates new board etc.)
    	Game game = new Game(difficulty);
    }
    
    private void chooseDifficulty() {
    	// Display dropdown
    	String[] difficultyChoices = {"Easy (Mate in <= 3)", "Medium (Mate in <= 5) - DEFAULT", "Hard (Mate in <= 10)"};
    	JComboBox<String> dropdown = new JComboBox<>(difficultyChoices);
    	JPanel panel = new JPanel();
    	panel.setLayout(new GridLayout(2, 1));
    	panel.add(dropdown);
    	// Display selection prompt asking for new difficulty
    	int option = JOptionPane.showOptionDialog(null, panel, "Change Difficulty", JOptionPane.PLAIN_MESSAGE,
    			JOptionPane.PLAIN_MESSAGE, null, null, null);
    	if (option == JOptionPane.OK_OPTION) {
    		changeDifficulty(dropdown);
    	}
    }
    
    private void changeDifficulty(JComboBox<String> dropdown) {
    	String choice = String.valueOf(dropdown.getSelectedItem());
    	int difficulty;
    	if (choice.startsWith("Easy")) {
    		difficulty = 3;
    	}
    	else if (choice.startsWith("Medium")) {
    		difficulty = 5;
    	}
    	else {
    		difficulty = 10;
    	}
    	System.out.println("Difficulty set to " + choice.substring(0, choice.indexOf(' ', 0)));
    	// Generate a new board with this new difficulty
    	generateNewBoard(difficulty);
    }
    
    /**
     * Copies FEN to clipboard.
     */
    private void copyFEN() {
    	String FEN = Game.board.getFEN();
    	StringSelection FENSelection = new StringSelection(FEN);
    	Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
    	cb.setContents(FENSelection, null);
    	System.out.println("Puzzle FEN copied to clipboard.");
    }
    
    public JFrame getFrame() {
    	return frame;
    }
}