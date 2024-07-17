package chuzzgen;

import javax.swing.*;

public class King extends Piece {
	private boolean inCheck;
	private boolean canCastle = true;
	private Square currentLocation;
	
    public King(ImageIcon pieceImage, String colour, char FEN, Square currentLocation) {
    	super(pieceImage, colour, true, FEN);
    	this.currentLocation = currentLocation;
    }
    
    @Override
    public boolean canMove(Square start, Square end) {    	
    	if (canCastle && start.getPieceOnSquare().isCastling(start, end)) {
    		canCastle = false;
    		return doCastlingMove(start, end);
    	}
    	if (Math.abs(end.getXPosition() - start.getXPosition()) > 1 || Math.abs(end.getYPosition() - start.getYPosition()) > 1) {
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
    	
    	// Make sure king cannot move next to another king
    	if (isKingBlocking(end)) {
    		return false;
    	}
    	
    	return isAlive();
    }
    
    public boolean doCastlingMove(Square start, Square end) { 
    	// Set the castle position
    	if (start.getYPosition() - end.getYPosition() > 0) { // Moving left
    	    Game.board.getSquares()[end.getXPosition()][end.getYPosition() + 1].setPieceOnSquare(Game.board.getSquares()[end.getXPosition()][end.getYPosition() - 2].getPieceOnSquare());	
    	    Game.board.getSquares()[end.getXPosition()][end.getYPosition() - 2].removePieceFromSquare();
    	}
    	else { // Moving right
    	    Game.board.getSquares()[end.getXPosition()][end.getYPosition() - 1].setPieceOnSquare(Game.board.getSquares()[end.getXPosition()][end.getYPosition() + 1].getPieceOnSquare());	
            Game.board.getSquares()[end.getXPosition()][end.getYPosition() + 1].removePieceFromSquare();
    	}
    	return true;
    }
    
    public boolean isKingBlocking(Square end) {
    	// If king is is in range 1 of the end square
    	int[] xDirections = {-1, -1, 0, 1, 1, 1, 0, -1};
    	int[] yDirections = {0, 1, 1, 1, 0, -1, -1, -1};
    	for (int i = 0; i < 8; i++) {
    		if (end.getXPosition() + xDirections[i] > 7 || end.getXPosition() + xDirections[i] < 0  ||  end.getYPosition() + yDirections[i] > 7 || end.getYPosition() + yDirections[i] < 0) {
    			continue;
    		}
    		Piece pieceOnSquare = Game.board.getSquares()[end.getXPosition() + xDirections[i]][end.getYPosition() + yDirections[i]].getPieceOnSquare();
    		if (pieceOnSquare instanceof King && pieceOnSquare.getColour() != this.getColour()) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public boolean isInCheck() {
    	return inCheck;
    }
    
    public void setInCheck() {
    	inCheck = true;
    }
    
    public void setCantCastle() {
    	canCastle = false;
    }
    
    public void setCurrentLocation(Square currentLocation) {
    	this.currentLocation = currentLocation;
    }
    
    public Square getCurrentLocation() {
    	return currentLocation;
    }
    
    /**
     * After every move, check if current player's king is in check
     */
    public boolean testForCheck() {
    	// Check all directions for attacking pieces
    	return straightCheck(currentLocation) || diagonalCheck(currentLocation) || knightCheck(currentLocation) || pawnCheck(currentLocation);
    }
    
	/**
	 * Check for castle/queen checks (straights)
	 */
	private boolean straightCheck(Square kingLocation) {
		Square analysing;
		int i = 1;
		// North
		while (kingLocation.getXPosition() - i >= 0) {
			analysing = Game.board.getSquares()[kingLocation.getXPosition() - i][kingLocation.getYPosition()];
			if (checkForStraightPiece(analysing.getPieceOnSquare())) return true;
			else if (analysing.isPieceOnSquare()) break; // Pieces other than other player's Queen/Castles block the check
			i++;
		}
		i = 1;
		// East
		while (kingLocation.getYPosition() + i <= 7) {
			analysing = Game.board.getSquares()[kingLocation.getXPosition()][kingLocation.getYPosition() + i];
			if (checkForStraightPiece(analysing.getPieceOnSquare())) return true;
			else if (analysing.isPieceOnSquare()) break;
			i++;
		}
		i = 1;
		// South
        while (kingLocation.getXPosition() + i <= 7) {
			analysing = Game.board.getSquares()[kingLocation.getXPosition() + i][kingLocation.getYPosition()];
			if (checkForStraightPiece(analysing.getPieceOnSquare())) return true;
			else if (analysing.isPieceOnSquare()) break;
			i++;
		}
        i = 1;
		// West
		while (kingLocation.getYPosition() - i >= 0) {
			analysing = Game.board.getSquares()[kingLocation.getXPosition()][kingLocation.getYPosition() - i];
			if (checkForStraightPiece(analysing.getPieceOnSquare())) return true;
			else if (analysing.isPieceOnSquare()) break;
			i++;
		}
		return false;
	}
	
	private boolean checkForStraightPiece(Piece piece) {
		return (piece instanceof Queen || piece instanceof Castle) && (piece.getColour() != this.getColour());
	}

	/**
	 * Check for bishop/queen checks (diagonals)
	 */
	private boolean diagonalCheck(Square kingLocation) {
		Square analysing;
		int i = 1;
		// Northeast
		while (kingLocation.getXPosition() - i >= 0 && kingLocation.getYPosition() + i <= 7) {
			analysing = Game.board.getSquares()[kingLocation.getXPosition() - i][kingLocation.getYPosition() + i];
			if (checkForDiagonalPiece(analysing.getPieceOnSquare())) return true;
			else if (analysing.isPieceOnSquare()) break; 
			i++;
		}
		i = 1;
		// Southeast
		while (kingLocation.getXPosition() + i <= 7 && kingLocation.getYPosition() + i <= 7) {
			analysing = Game.board.getSquares()[kingLocation.getXPosition() + i][kingLocation.getYPosition() + i];
			if (checkForDiagonalPiece(analysing.getPieceOnSquare())) return true;
			else if (analysing.isPieceOnSquare()) break;
			i++;
		}
		i = 1;
		// Southwest
		while (kingLocation.getXPosition() + i <= 7 && kingLocation.getYPosition() - i >= 0) {
			analysing = Game.board.getSquares()[kingLocation.getXPosition() + i][kingLocation.getYPosition() - i];
			if (checkForDiagonalPiece(analysing.getPieceOnSquare())) return true;
			else if (analysing.isPieceOnSquare()) break;
			i++;
		}
		i = 1;
		// Northwest
		while (kingLocation.getXPosition() - i >= 0 && kingLocation.getYPosition() - i >= 0) {
			analysing = Game.board.getSquares()[kingLocation.getXPosition() - i][kingLocation.getYPosition() - i];
			if (checkForDiagonalPiece(analysing.getPieceOnSquare())) return true;
			else if (analysing.isPieceOnSquare()) break;
			i++;
		}
		return false;
	}
	
	private boolean checkForDiagonalPiece(Piece piece) {
		return (piece instanceof Queen || piece instanceof Bishop) && (piece.getColour() != this.getColour());
	}

	/**
	 * Check for knight checks (L moves)
	 */
	private boolean knightCheck(Square kingLocation) {
		Square analysing;
		int[] directions = {-2, 1, -1, 2, 1, 2, 2, 1, 2, -1, 1, -2, -1, -2, -2, -1};
		int i = 0;
		while (i < directions.length) {
			if (kingLocation.getXPosition() + directions[i] < 0 || kingLocation.getXPosition() + directions[i] > 7 ||
					kingLocation.getYPosition() + directions[i+1] < 0 || kingLocation.getYPosition() + directions[i+1] > 7) {
				i+= 2;
				continue;
			}
			analysing = Game.board.getSquares()[kingLocation.getXPosition() + directions[i]][kingLocation.getYPosition() + directions[i+1]];
			if (checkForKnight(analysing.getPieceOnSquare())) {
				return true;
			}
			i+=2;
		}
		return false;
	}
	
	private boolean checkForKnight(Piece piece) {
		return (piece instanceof Knight) && (piece.getColour() != this.getColour());
	}

	/**
     * Check for pawn checks (in front diagonals)
     */
	private boolean pawnCheck(Square kingLocation) {
		// If the King is South (King's colour is Player 1) then opposing Pawn comes from
		// North, else if the King is North (King's colour is player 2), then the opposing
		// Pawn comes from South
		// Check if King is South (if King's colour is Player 1's colour)
		Square analysing;
		// Check if there are any Pawns attacking
		if (this.getColour().equals(Game.player1.getColour())) {
			// Pawns are attacking King North-South
		    // Northeast
			if (kingLocation.getXPosition() - 1 >= 0 && kingLocation.getYPosition() + 1 <= 7) {
				analysing = Game.board.getSquares()[kingLocation.getXPosition() - 1][kingLocation.getYPosition() + 1];
			    if (checkForPawn(analysing.getPieceOnSquare())) return true;
			}
		    // Northwest
			if (kingLocation.getXPosition() - 1 >= 0 && kingLocation.getYPosition() - 1 >= 0) {
				analysing = Game.board.getSquares()[kingLocation.getXPosition() - 1][kingLocation.getYPosition() - 1];
			    if (checkForPawn(analysing.getPieceOnSquare())) return true;
			}
		}
		else {
			// Pawns are attacking King South-North
			// Southeast
			if (kingLocation.getXPosition() + 1 <= 7 && kingLocation.getYPosition() + 1 <= 7) {
				analysing = Game.board.getSquares()[kingLocation.getXPosition() + 1][kingLocation.getYPosition() + 1];
				if (checkForPawn(analysing.getPieceOnSquare())) return true;
			}
			// Southwest
			if (kingLocation.getXPosition() + 1 <= 7 && kingLocation.getYPosition() - 1 >= 0) {
				analysing = Game.board.getSquares()[kingLocation.getXPosition() + 1][kingLocation.getYPosition() - 1];
				if (checkForPawn(analysing.getPieceOnSquare())) return true;
			}
		}
		return false;
	}
	
	private boolean checkForPawn(Piece piece) {
		return (piece instanceof Pawn) && (piece.getColour() != this.getColour());
	}

	@Override
	public boolean canCapture(Square start, Square end) {
		// Check take is valid
		return end.getPieceOnSquare().getColour() != Game.currentPlayer.getColour();
	}
}
