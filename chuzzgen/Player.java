package chuzzgen;

public abstract class Player {
    private String playerName;
    private final String PLAYER_TYPE;
    private String colour;
    private King king;
    
    public Player(String playerName, String PLAYER_TYPE, String colour) {
    	this.playerName = playerName;
    	this.PLAYER_TYPE = PLAYER_TYPE;
    	this.colour = colour;
    }
    
    public String getName() {
    	return playerName;
    }
    
    public String getColour() {
    	return colour;
    }
    
    public void setColour(String colour) {
        this.colour = colour;
    }
    
    public void setKing(King king) {
    	this.king = king;
    }
    
    public King getKing() {
    	return king;
    }
}
