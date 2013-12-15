package gameMechanics;

public class Gamer {
	private int id;
	private int boardPosition;
	private int points;
	
	public Gamer(int id) {
		this.id = id;
		this.boardPosition = 0;
		points = 0;
	}
	
	public int getId() {
		return this.id;
	}
	public int getBoardPosition() {
		return this.boardPosition;
	}
	public int getPoints() {
		return this.points;
	}

    public void setId(int id) {
        this.id = id;

    }
    public void setBoardPosition(int boardPosition) {
        this.boardPosition = boardPosition;

    }
    public void setPoints(int points) {
        this.points = points;

    }

}

