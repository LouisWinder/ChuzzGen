package chuzzgen;

import javax.swing.*;

/**
 * Abstract class representing common details for all chess pieces.
 */

public abstract class Piece
{	
	final ImageIcon PIECE_IMAGE;
    private String colour;
    private boolean isAlive;
    private final char FEN; // Representation of the piece in FEN notation.
    
    public Piece(ImageIcon pieceImage, String colour, boolean isAlive, char FEN)
    {
    	this.PIECE_IMAGE = pieceImage;
        this.colour = colour;
        this.isAlive = isAlive;
        this.FEN = FEN;
    }
    
    public String getColour()
    {
        return colour;
    }
    
    public void setColour(String colour)
    {
        this.colour = colour;
    }
    
    public boolean isAlive()
    {
        return isAlive;
    }
    
    public void setDead()
    {
        isAlive = false;
    }
    
    /**
     * Return the FEN (Forsyth-Edwards Notation) representation of a Piece.
     * @return The piece's FEN representation.
     */
    public char getFEN() {
    	return FEN;
    }
    
    public abstract boolean canMove(Square start, Square end);
    
    public abstract boolean canCapture(Square start, Square end);
    
    public boolean isCastling(Square start, Square end) {
    	if (start.getPieceOnSquare() instanceof King && Math.abs(start.getYPosition() - end.getYPosition()) == 2) {
    		// Search to end of board bounds for Castle, left and right - if not found/found piece other than castle stop search for that direction
    		if (start.getYPosition() - end.getYPosition() > 0) { // Moving left
    			int i = 1;
    			while (i > 0) {
    				if (i == 4) {
    					// Check for castle
    					return Game.board.getSquares()[start.getXPosition()][start.getYPosition() - i].getPieceOnSquare() instanceof Castle;
    				}
    				// Test that this move won't result in the king being in (or passing through) check
    				boolean illegalMove = false;
    				Game.board.getSquares()[start.getXPosition()][start.getYPosition() - i].setPieceOnSquare(start.getPieceOnSquare());
    				if (((King) Game.board.getSquares()[start.getXPosition()][start.getYPosition() - i].getPieceOnSquare()).testForCheck()) illegalMove = true;
    				Game.board.getSquares()[start.getXPosition()][start.getYPosition() - i].removePieceFromSquare();
    				if (illegalMove) break;
    				else if (Game.board.getSquares()[start.getXPosition()][start.getYPosition() - i].isPieceOnSquare()) break; // Found blocking piece (other than castle)
    				i++;
    			}
    		}
    		else if (start.getYPosition() - end.getYPosition() < 0) { // Moving right
    			int i = 1;
    			while (i < 7) {
    				if (i == 3) {
    					// Check for castle
    					return Game.board.getSquares()[start.getXPosition()][start.getYPosition() + i].getPieceOnSquare() instanceof Castle;
    				}
    				// Test that this move won't result in the king being in (or passing through) check
    				boolean illegalMove = false;
    				Game.board.getSquares()[start.getXPosition()][start.getYPosition() + i].setPieceOnSquare(start.getPieceOnSquare());
    				if (((King) Game.board.getSquares()[start.getXPosition()][start.getYPosition() + i].getPieceOnSquare()).testForCheck()) illegalMove = true;
    				Game.board.getSquares()[start.getXPosition()][start.getYPosition() + i].removePieceFromSquare();
    				if (illegalMove) break;
    				else if (Game.board.getSquares()[start.getXPosition()][start.getYPosition() + i].isPieceOnSquare()) break; // Found blocking piece (other than castle)
    				i++;
    			}
    		}
    	}
    	return false;
    }
    
    /*
     * Verifies move is a straight line
     */
    public boolean isMoveStraight(Square start, Square end) {
    	if (start.getXPosition() != end.getXPosition() && start.getYPosition() != end.getYPosition()) {
    		return false;
    	}
    	return true;
    }
    
    /*
     * Verifies move is diagonal
     */
    public boolean isMoveDiagonal(Square start, Square end) {
    	if (Math.abs(start.getXPosition() - end.getXPosition()) != Math.abs(start.getYPosition() - end.getYPosition())) {
    		return false;
    	}
    	return true;
    }
    
    /**
     * Verifies that the intended straight path of travel is free of any blocking pieces
     */
    public boolean isPathClearStraight(Square start, Square end) {
    	// Pre-defined for West (else case if all below fail)
    	int iStart = end.getYPosition();
        int iEnd = start.getYPosition();
    	if (end.getXPosition() < start.getXPosition()) {
    		// North
    		iStart = end.getXPosition();
    		iEnd = start.getXPosition();
    	}
    	else if (end.getYPosition() > start.getYPosition()) {
    		// East
    		iStart = start.getYPosition();
    		iEnd = end.getYPosition();
    	}
    	else if (end.getXPosition() > start.getXPosition()) {
    		// South
    		iStart = start.getXPosition();
    		iEnd = end.getXPosition();
    	}
    	int xPos = start.getXPosition();
		int yPos = start.getYPosition();
    	for (int i = iStart + 1; i < iEnd; i++) {
    		if (xPos != end.getXPosition()) {
    			xPos = i;
    		}
    		else {
    			yPos = i;
    		}
    		if (Game.board.getSquares()[xPos][yPos].isPieceOnSquare()) {
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * Verifies that the intended diagonal path of travel is free of any blocking pieces
     */
    public boolean isPathClearDiagonal(Square start, Square end) {
    	// Pre-defined for Southwest (xPos increasing, yPos decreasing)
    	int xDirection = 1;
    	int yDirection = -1;
    	if (end.getXPosition() < start.getXPosition() && end.getYPosition() > start.getYPosition()) {
    		// Northeast (xPos decreasing, yPos increasing)
    		xDirection = -1;
    		yDirection = 1;
    	}
    	else if (end.getXPosition() < start.getXPosition() && end.getYPosition() < start.getYPosition()) {
    		// Northwest (xPos decreasing, yPos decreasing)
    		xDirection = -1;
    		yDirection = -1;
    	}
    	else if (end.getXPosition() > start.getXPosition() && end.getYPosition() > start.getYPosition()) {
    		// Southeast (xPos increasing, yPos increasing)
    		xDirection = 1;
    		yDirection = 1;
    	}
    	int xPos = start.getXPosition();
    	int yPos = start.getYPosition();
    	while (xPos != (end.getXPosition() - xDirection) && yPos != (end.getYPosition() - yDirection)) {
    		xPos += xDirection;
    		yPos += yDirection;
    		if (Game.board.getSquares()[xPos][yPos].isPieceOnSquare()) {
    			return false;
    		}
    	}
    	return true;
    }
    
    public void capture(Square end) {
    	end.getPieceOnSquare().setDead();
    }
}