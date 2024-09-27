package chuzzgen;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Board extends JPanel
{
    private Square[][] squares = new Square[8][8];
    static Square start;
    // Probabilities of each row for each colour. It is the chance for a piece of a certain
    // colour to be generated on each row, scaled to avoid unrealistic generation.
    private double[] rowProbsWhite, rowProbsBlack;
    // Probabilities for Pawns of each row for each colour
    private double[] pawnRowProbsWhite, pawnRowProbsBlack;
    // Available pieces for generation. These pieces are distributed in the generation phase
    private ArrayList<Piece> availableWhite, availableBlack;
    // The FEN representation of the Board state
    private String FEN;
    // Stockfish engine's evaluation of the current Board
    private int stockfishEval;
    // Define an "efficiency threshold", whereby only generations higher than it will 
	// be considered.
    // Difficulty of generations (maximum number of moves to Checkmate)
    // Default value is "Medium" (mate in <= 5 moves)
    private int efficiencyThresholdMate;
    
    public Board()
    {
        setup();
    }
    
    /**
     * Sets up the board.
     */
    private void setup() {
    	this.setLayout(new GridLayout(8, 8));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((j % 2 == 1 && i % 2 == 1) || (j % 2 == 0 && i % 2 == 0)) {
                    squares[i][j] = new Square(new Color(238, 238, 210), i, j, null);
                    squares[i][j].colourName = "White"; // Substitute for "Light" squares
                }
                else {
                    squares[i][j] = new Square(new Color(118, 150, 86), i, j, null);
                    squares[i][j].colourName = "Black"; // Substitute for "Dark" squares
                } 
                this.add(squares[i][j]);
            }
        }
    }
    
    public Square[][] getSquares()
    {
        return squares;
    }
    
    /**
     * Returns the FEN representation of the current Board.
     * @return The current Board's FEN representation.
     */
    public String getFEN() {
    	return FEN;
    }
    
    /**
     * Returns the Stockfish engine's evaluation of the current Board.
     * @return Stockfish's evaluation of the Board.
     */
    public int getStockfishEval() {
    	return stockfishEval;
    }
    
    /**
     * Sets the difficulty of subsequent generations.
     * @param efficiencyThreshold The maximum evaluation threshold (typically
     *        number of moves to Checkmate).
     */
    public void setDifficulty(int efficiencyThreshold) {
    	efficiencyThresholdMate = efficiencyThreshold;
    }
    
    /**
     * Returns the current difficulty for this Board.
     * @return The current difficulty (in moves needed to checkmate).
     */
    public int getCurrentDifficulty() {
    	return efficiencyThresholdMate;
    }
    
    /**
     * Clears the board of all pieces.
     */
    public void clear() {
    	for (int i = 0; i < 8; i++) {
    		for (int j = 0; j < 8; j++) {
    			if (squares[i][j].isPieceOnSquare()) {
    				squares[i][j].removePieceFromSquare();
    			}
    		}
    	}
    }
    
    /**
     * Initilise board rules/constraints that guide generated positions.
     */
    public void initialise() {
    	// Probabilities of pieces to be on certain rows (least likely to most likely)
    	double[] probs = {0.00625, 0.00625, 0.0125, 0.025, 0.1, 0.25, 0.25, 0.35};
    	// Separate probabilities for Pawn generation (they cannot be on rows 1 or 8)
    	double[] pawnProbs = {0, 0.025, 0.025, 0.1, 0.1, 0.25, 0.5, 0};
    	rowProbsWhite = new double[8];
    	rowProbsBlack = new double[8];
    	pawnRowProbsWhite = new double[8];
    	pawnRowProbsBlack = new double[8];
    	if (Game.player1.getColour().equals("White")) {
    		// White is South
    		for (int i = 0; i < 8; i++) {
    			rowProbsWhite[i] = probs[i];
    			rowProbsBlack[i] = probs[7-i];
    			pawnRowProbsWhite[i] = pawnProbs[i];
    			pawnRowProbsBlack[i] = pawnProbs[7-i];
    		}
    	}
    	else {
    		// Black is South
    		for (int i = 0; i < 8; i++) {
    			rowProbsBlack[i] = probs[i];
    			rowProbsWhite[i] = probs[7-i];
    			pawnRowProbsBlack[i] = pawnProbs[i];
    			pawnRowProbsWhite[i] = pawnProbs[7-i];
    		}
    	}
    }
    
    /**
     * Generate a potential puzzle starting position.
     */
    public void generate() 
    {
    	// Set available pieces for generation
    	setGenerationPieces();
    	
    	// For all pieces, choose a square to generate on.
    	// First, a random column is chosen, then using the probabilities
    	// for each, a row is chosen. Pieces are then placed accordingly.
    	
    	Iterator<Piece> itWhite = availableWhite.iterator();
    	Iterator<Piece> itBlack = availableBlack.iterator();
    	
    	Square square;
    	int[] rows = {0, 1, 2, 3, 4, 5, 6, 7};
    	// Generate White
    	while (itWhite.hasNext()) {
    		Piece piece = itWhite.next();
    		do {
    			int randomRow;
    			if (piece instanceof Pawn) {
    				randomRow = Utils.randomWeightedChoice(rows, pawnRowProbsWhite);
    			}
    			else {
    				randomRow = Utils.randomWeightedChoice(rows, rowProbsWhite);
    			}
    		    int randomColumn = (int) ((Math.random() * (7 - 0 + 1)) + 0);
    		    square = squares[randomRow][randomColumn];
    		} while (square.isPieceOnSquare());
    		square.setPieceOnSquare(piece);
    		itWhite.remove();
    	}
    	
    	// Generate Black
    	while (itBlack.hasNext()) {
    		Piece piece = itBlack.next();
    		do {
    			int randomRow;
    			if (piece instanceof Pawn) {
    				randomRow = Utils.randomWeightedChoice(rows, pawnRowProbsBlack);
    			}
    			else {
    				randomRow = Utils.randomWeightedChoice(rows, rowProbsBlack);
    			}
    			int randomColumn = (int) ((Math.random() * (7 - 0 + 1)) + 0);
    			square = squares[randomRow][randomColumn];
    		} while (square.isPieceOnSquare());
    		square.setPieceOnSquare(piece);
    		itBlack.remove();
    	}
	}
    
    /**
     * Creates constrained subset of pieces to be used for generation.
     */
    private void setGenerationPieces() {
    	// Each King has a 100% chance of generating.
    	// Each Queen has a 50% chance of generating, but only once.
    	// Castles, Bishops and Knights have a 50% chance of generating once.
    	// Once one piece per class (e.g. Bishop) has been generated, the chance of another 
    	// generating for that same colour halves to 25%.
    	// Pawn generation chances are as follows:
    	// 1st,   2nd,  ..., 7th,  8th
    	//  |      |          |     |
    	//  V      V          V     V
    	// 100%, 87.5%, ..., 25%, 12.5%
    	// Pawns have a 0% chance to generate on rows 1 and 8 (0 and 7).
    	availableWhite = new ArrayList<>();
    	availableBlack = new ArrayList<>();
    	double queenChance = 0.5;
    	double castleChance = 0.5;
    	double bishopChance = 0.5;
    	double knightChance = 0.5;
    	double pawnChance = 1.0;
    	double random;
    	try {
    		// King
    		King whiteKing = new King(new ImageIcon(ImageIO.read(new File("pieces/king_white.png")).getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH)), "White", 'K', null);
    		King blackKing = new King(new ImageIcon(ImageIO.read(new File("pieces/king_black.png")).getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH)), "Black", 'k', null);
    		    		
    		availableWhite.add(whiteKing);
    		availableBlack.add(blackKing);
    		Game.player1.setKing(Game.player1.getColour() == "White" ? whiteKing : blackKing);
    		Game.player2.setKing(Game.player2.getColour() == "White" ? whiteKing : blackKing);
    		
    		// Queens
    		random = Math.random();
    		if (random < queenChance) {
    		    availableWhite.add(new Queen(new ImageIcon(ImageIO.read(new File("pieces/queen_white.png")).getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH)), "White", 'Q'));
    		}
    		random = Math.random();
    		if (random < queenChance) {
    			availableBlack.add(new Queen(new ImageIcon(ImageIO.read(new File("pieces/queen_black.png")).getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH)), "Black", 'q'));
    		}
    		
    		// Castles
    		while (castleChance >= 0.25) {
    			random = Math.random();
    			if (random < castleChance) {
    			    availableWhite.add(new Castle(new ImageIcon(ImageIO.read(new File("pieces/castle_white.png")).getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH)), "White", 'R'));
    			}
    			random = Math.random();
    			if (random < castleChance) {
    				availableBlack.add(new Castle(new ImageIcon(ImageIO.read(new File("pieces/castle_black.png")).getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH)), "Black", 'r'));
    			}
    			castleChance /= 2;
    		}
    		
    		// Bishops
    		while (bishopChance >= 0.25) {
    			random = Math.random();
    			if (random < bishopChance) {
    				availableWhite.add(new Bishop(new ImageIcon(ImageIO.read(new File("pieces/bishop_white.png")).getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH)), "White", 'B'));
    			}
    			random = Math.random();
    			if (random < bishopChance) {
    				availableBlack.add(new Bishop(new ImageIcon(ImageIO.read(new File("pieces/bishop_black.png")).getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH)), "Black", 'b'));
    			}
    			bishopChance /= 2;
    		}
    		
    		// Knights
    		while (knightChance >= 0.25) {
    			random = Math.random();
    			if (random < knightChance) {
    				availableWhite.add(new Knight(new ImageIcon(ImageIO.read(new File("pieces/knight_white.png")).getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH)), "White", 'N'));
    			}
    			random = Math.random();
    			if (random < knightChance) {
    				availableBlack.add(new Knight(new ImageIcon(ImageIO.read(new File("pieces/knight_black.png")).getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH)), "Black", 'n'));
    			}
    			knightChance /= 2;
    		}
    		
    		// Pawns
    		while (pawnChance > 0) {
    			random = Math.random();
    			if (random < pawnChance) {
    				availableWhite.add(new Pawn(new ImageIcon(ImageIO.read(new File("pieces/pawn_white.png")).getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH)), "White", 'P'));
    			}
    			random = Math.random();
    			if (random < pawnChance) {
    				availableBlack.add(new Pawn(new ImageIcon(ImageIO.read(new File("pieces/pawn_black.png")).getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH)), "Black", 'p'));
    			}
    			pawnChance -= 1f / 8f;
    		}
    	}
    	catch (IOException e) {
    		// Handle exception
    		e.printStackTrace();
    	}
    }
    
    /**
     * Verify that the current generation is legal in the game of Chess.
     * @return If the current generated position is legal or not.
     */
    public boolean isLegal() {
    	// RULES FOR LEGALITY VERIFICATION:
    	// 1. Neither King must be in Check
    	// 2. Kings must not be next to each other
    	// 3. If two bishops generate for one player they must be on opposing colour squares.
    	
    	// Verify no King is in Check
    	if (Game.player1.getKing().testForCheck() || Game.player2.getKing().testForCheck()) {
    		return false;
    	}
    	
    	// Verify Kings are not next to eachother
    	if (Game.player1.getKing().isKingBlocking(Game.player1.getKing().getCurrentLocation())) {
    		return false;
    	}
    	
    	// If Players has 2 Bishops, verify they are on different coloured squares
    	if (!areBishopsValid()) {
    		return false;
    	}
    	
    	return true;
    }
    
    /**
     * Checks if Bishops are on different diagonals for a colour.
     * @return Whether or not bishops are on the same colour.
     */
    private boolean areBishopsValid() {
    	// Search for first bishops for each colour, note the colour square it is on
    	// If one found, check for another of same colour and if found, verify it is not 
    	// on the same colour square as the first
    	String squareColourWhite = null;
    	String squareColourBlack = null;
    	for (int i = 0; i < 8; i++) {
    		for (int j = 0; j < 8; j++) {
    			Piece analysing = squares[i][j].getPieceOnSquare();
    			if (analysing instanceof Bishop) {
    				if (analysing.getColour().equals("White")) {
    					// If colour of square is the same as previously found Bishop
    					// square then configuration is illegal
    					if (squares[i][j].colourName == squareColourWhite) return false;
    					// Save square colour for White
    					squareColourWhite = squares[i][j].colourName;
    				}
    				else {
    					if (squares[i][j].colourName == squareColourBlack) return false;
    					// Save square colour for Black
    					squareColourBlack = squares[i][j].colourName;
    				}
    			}
    		}
    	}
    	return true;
    }
    
    /**
     * Communicate with the Stockfish Chess engine to determine whether a generated puzzle
     * is viable or not. A viable puzzle is one that is lower than the "efficiency threshold",
     * which is defined as a maximum number of moves that checkmates the opponent.
     * @return Whether or not a generated puzzle is viable/"efficient".
     */
    public boolean isEfficient() {
    	// Convert the current generation position into FEN notation so it can be interpreted
    	// by Stockfish
    	FEN = convertToFEN();
    	// Determine current generation's efficiency by evaluating the position via Stockfish
    	// Save it to Board
    	stockfishEval = getStockfishEval(FEN);
    	// Does Stockfish's evaluation pass the evaluation threshold for Player 1?  
        return (stockfishEval <= efficiencyThresholdMate) && (stockfishEval > 0);
    }
    
    /**
     * Converts a generation to Forsyth-Edwards Notation (FEN).
     * @return The FEN String of the current generation.
     */
    public String convertToFEN() {
    	String FEN = "";
    	// Define all piece FEN representations
    	for (int i = 0; i < 8; i++) {
    		if (i > 0) FEN += "/";
    		int gapCount = 0;
    		for (int j = 0; j < 8; j++) {
    			// Check for piece on square
    		    Square square = squares[i][j];
    		    if (square.isPieceOnSquare()) {
    		    	if (gapCount > 0) {
    		    		FEN += String.valueOf(gapCount);
    		    		gapCount = 0;
    		    	}
    		    	// Determine piece on square, get its FEN representation
    		    	FEN += square.getPieceOnSquare().getFEN();
    		    }
    		    else {
    		    	gapCount++;
    		    }
    		}
    		if (gapCount != 0) {
    			FEN += gapCount;
    		}
    	}
    	// If Black is South (Player 1), reverse the FEN
    	if (Game.player1.getColour().equals("Black")) {
    		FEN = Utils.reverseString(FEN);
    	}
    	// Append the remaining parts of the FEN
    	// For simplicity, 4 elements are assumed:
    	// 1. It is always the player who is South's (Player 1's) turn
    	// 2. Castling is not possible for either player
    	// 3. Move counts are not included (set to 0, 0)
    	// 4. En Passant moves are not included.
    	char player1Colour = Game.player1.getColour().equals("White") ? 'w' : 'b';
    	FEN += " ";
    	FEN += player1Colour;
    	FEN += " - - ";
    	FEN += "0 0";
    	return FEN;
    }
    
    /**
     * Get the evaluation of the current board via Stockfish engine.
     * @param FEN The FEN representation of the current board.
     * @return The evaluation of the position (how many moves to checkmate).
     */
    public int getStockfishEval(String FEN) {
    	int evaluation = 0;
    	String root = System.getProperty("user.dir");
    	// Path to the Stockfish executable
    	final String STOCKFISH_VERSION = ""; // Path to Stockfish executable like: "/[STOCKFISH_EXE]"
    	final String STOCKFISH_PATH = root + STOCKFISH_VERSION;
    	try {
    		ProcessBuilder pb = new ProcessBuilder(STOCKFISH_PATH);
    		Process stockfish = pb.start();
    		BufferedReader reader = new BufferedReader(new InputStreamReader(stockfish.getInputStream()));
    		PrintWriter writer = new PrintWriter(new OutputStreamWriter(stockfish.getOutputStream()));
    		
    		// Verify Stockfish is ready
    		writer.println("uci");
    		writer.flush();
    		// Check for all clear 
    		String line;
    		while ((line = reader.readLine()) != null) {
    			if (line.equals("uciok")) {
    				break;
    			}
    		}
    		
    		// Initialise position with FEN
    		writer.println("position fen " + FEN);
    		writer.flush();
    		
    		// Evaluate the position
    		// Search for x amount of time (ms) - DON'T MAKE THIS LONGER THAN A FEW SECONDS
    		int searchTime = 1000;
    		writer.println("go movetime " + searchTime);
    		writer.flush();
    		// Return Stockfish's evaluation (penultimate line)
    		// NOTE: Evaluation is from the current player's POV
    		String nextLine = "";
    		while (true) {
    			nextLine = reader.readLine();
    			if (nextLine.startsWith("bestmove")) break;
    		    line = nextLine;
    		}
    		int mateIndex = line.indexOf("mate");
			if (mateIndex != -1) {
				// Mate has been found
				evaluation = Integer.parseInt(line.substring(mateIndex + 5, line.indexOf(' ', mateIndex + 5)));
			}
    		
    		// Close streams and processes
    		writer.close();
    		reader.close();
    		stockfish.destroy();
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    		return 0;
    	}
    	return evaluation;
    }
    
    public static void saveStart(Square start) {
    	Board.start = start;
    }
}