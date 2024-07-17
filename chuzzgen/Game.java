package chuzzgen;

import java.util.*;

public class Game {
	static Player player1;
	static Player player2; 
	static Board board;
	static Board testBoard;
	static boolean moveInProgress;
	static Player currentPlayer;
	static GUI gui;
	// In the case Human vs AI, the Human is always player 1
	
    public Game(int difficulty) {
    	moveInProgress = false;
    	initialisePlayers();
    	initialiseBoard(difficulty);
    }
    
    public static void initialisePlayers() {
    	player1 = new Human("Player 1", null);
    	player2 = new Human("Player 2", null);
    	// Pick a random colour for the human puzzle player
    	List<String> colours = Arrays.asList("White", "Black");
    	Collections.shuffle(colours);
    	player1.setColour(colours.get(0));
    	player2.setColour(colours.get(1));
    	currentPlayer = player1;
    }
   
    /**
     * Initialises the board constraints that guide generations.
     */
    public void initialiseBoard(int difficulty) {
    	// Instantiate board
    	board = new Board();
    	// Outline board rules/constraints for guiding generations
    	board.initialise();
    	// Choose difficulty for puzzle generation
    	board.setDifficulty(difficulty);
    	// Generate the position
    	generateBoard();
    }
    
    /**
     * Generates the puzzle.
     */
    public static void generateBoard() {
    	// 1. Generate the "random" position for the puzzle
    	// 2. Check legality of generation
    	// 3. If position isn't legal, generate a new position
    	// 4. Check "efficiency" of position (whether it's puzzle-worthy)
    	// 5. If efficiency is below defined threshold, generate a new position.
    	// 6. Once a puzzle has been generated that is both legal and efficient, it can be used!
    	// Close currently open GUI if regenerating
    	System.out.println("Generating puzzle...");
    	if (gui != null) {
    		gui.getFrame().dispose();
    	}
    	do {
    		// Ensure the Board is clear of pieces (for re-generation)
    		board.clear();
    		board.generate();
    	} while (!board.isLegal() || !board.isEfficient());
    	System.out.println("Puzzle generated!");
    	// Initialise the board to show
        gui = new GUI(board);
    }
    
    /**
     * Generate the board used for testing moves. Examples of tests are when testing if a move will put a king in check, or when a pawn reaches the end of the board etc.
     * The test board whenever generated will draw information about board state (piece locations) from the current in-play board.
     */
    public void generateTestBoard() {
    	testBoard = new Board();
    	// Pull information about current in-play board state
    	// ...
    }
    
    public static void changeTurn() {
    	currentPlayer = currentPlayer == player1 ? player2 : player1;
    }
    
    public static void makeMove(Square start, Square end) {
    	Piece pieceToMove = start.getPieceOnSquare();
    	// Move the piece
    	if (pieceToMove.canMove(start, end)) {
    		// Check if pawn is at end (and can thus promote)
    		if (pieceToMove instanceof Pawn) {
    			if (((Pawn) pieceToMove).canMoveTwo()) {
    				((Pawn) pieceToMove).setFirstMoveMade(); // Make sure pawn cannot move twice after first move
    			}
    			pieceToMove = ((Pawn) pieceToMove).promote(start, end); // Check if pawn will promote, and do if so
    		}
    		// If king is still in check after attempted move, don't do the move
    		start.removePieceFromSquare();
    		Piece savedEnd = null;
    		if (end.isPieceOnSquare()) {
    			// Save the piece
    			savedEnd = end.getPieceOnSquare();
    		}
            end.setPieceOnSquare(pieceToMove);
    		if (!currentPlayer.getKing().testForCheck()) {
    			start.removePieceFromSquare();
        		changeTurn();
        	    if (pieceToMove instanceof King) {
        	        ((King) pieceToMove).setCurrentLocation(end);
        	    }
    		}
    		else {
    			start.setPieceOnSquare(pieceToMove);
    			end.removePieceFromSquare();
    			if (savedEnd != null) end.setPieceOnSquare(savedEnd);
    		}
    	}
    	if (!currentPlayer.getKing().isInCheck() && currentPlayer.getKing().testForCheck()) {
    		currentPlayer.getKing().setInCheck();
    	}
    	if (currentPlayer instanceof AI) {
			// Make AI move
			makeMove(start, ((AI) currentPlayer).determineMove());
		}
    	// Wait for move from player
    }
}
