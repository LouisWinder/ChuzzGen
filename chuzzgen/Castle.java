package chuzzgen;

import javax.swing.*;

public class Castle extends Piece {
    public Castle(ImageIcon pieceImage, String colour, char FEN) {
    	super(pieceImage, colour, true, FEN);
    }
    
    @Override
    public boolean canMove(Square start, Square end) {
    	// Verify move is a straight line
    	if (!isMoveStraight(start, end)) {
    		return false;
    	}
    	
    	// Check path to end square is clear
    	if (!isPathClearStraight(start, end)) {
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
    	Game.currentPlayer.getKing().setCantCastle(); // Can't castle after rook move
    	return isAlive();
    }

	@Override
	public boolean canCapture(Square start, Square end) {
		// Check take is valid
		return end.getPieceOnSquare().getColour() != Game.currentPlayer.getColour();
	}
}
