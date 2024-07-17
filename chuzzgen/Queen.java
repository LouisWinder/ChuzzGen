package chuzzgen;

import javax.swing.*;

public class Queen extends Piece {
    public Queen(ImageIcon pieceImage, String colour, char FEN) {
    	super(pieceImage, colour, true, FEN);
    }
    
    @Override
    public boolean canMove(Square start, Square end) {
    	// Verify move is a straight line or diagonal
    	String direction = "Straight";
    	if (!isMoveStraight(start, end)) {
    		if (!isMoveDiagonal(start, end)) {
    	        return false;
    		}
    		else {
    		    direction = "Diagonal";
    		}
    	}
    	
    	// Check path to end square is clear (straight line or diagonal)
    	if (direction.equals("Straight")) {
    		if (!isPathClearStraight(start, end)) {
    			return false;
    		}
    	}
    	else {
    		if (!isPathClearDiagonal(start, end)) {
    			return false;
    		}
    	}
    	
    	// Implement taking
    	if (end.isPieceOnSquare()) {
    		if (canCapture(start, end)) {
    			capture(end);
    		}
    		else {
    		    return false;
    		}
    	}
    	
    	return isAlive();
    }

	@Override
	public boolean canCapture(Square start, Square end) {
		// Check take is valid
		return end.getPieceOnSquare().getColour() != Game.currentPlayer.getColour();
	}
}
