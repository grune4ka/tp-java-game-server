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
	
	public boolean setId(int id) {
		try {
			this.id = id;
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}
	public boolean setBoardPosition(int boardPosition) {
		try {
			this.boardPosition = boardPosition;
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}
	public boolean setPoints(int points) {
		try {
			this.points = points;
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}

}

