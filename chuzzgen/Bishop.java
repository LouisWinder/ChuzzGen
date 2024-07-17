package chuzzgen;

import javax.swing.*;

public class Bishop extends Piece {
    public Bishop(ImageIcon pieceImage, String colour, char FEN) {
    	super(pieceImage, colour, true, FEN);
    }
    
    @Override
    public boolean canMove(Square start, Square end) {
    	// Verify move is diagonal
    	if (!isMoveDiagonal(start, end)) {
    		return false;
    	}
    	
    	// Check path to end square is clear
    	if (!isPathClearDiagonal(start, end)) {
    		return false;
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
