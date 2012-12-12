package gameMechanics;

public class GameSessionSnapshot {
	private int idGamer1;
	private int pointsGamer1;
	private int positionGamer1;
	
	private int idGamer2;
	private int pointsGamer2;
	private int positionGamer2;
	
	private int ballPositionX;
	private int ballPositionY;
	
	private boolean active;
	private boolean finish;
	
	public GameSessionSnapshot(int idGamer1, int pointsGamer1, int positionGamer1, 
			int idGamer2, int pointsGamer2, int positionGamer2,
			int ballPositionX, int ballPositionY, 
			boolean active, boolean finish) {
		this.idGamer1 = idGamer1;
		this.pointsGamer1 = pointsGamer1;
		this.positionGamer1 = positionGamer1;
		
		this.idGamer2 = idGamer2;
		this.pointsGamer2 = pointsGamer2;
		this.positionGamer2 = positionGamer2;
		
		this.ballPositionX = ballPositionX;
		this.ballPositionY = ballPositionY;
		
		this.active = active;
		this.finish = finish;
	}
	
	public int getIdGamer1() {
		return this.idGamer1;
	}
	public int getPointsGamer1() {
		return this.pointsGamer1;
	}
	public int getPositionGamer1() {
		return this.positionGamer1;
	}
	
	public int getIdGamer2() {
		return this.idGamer2;
	}
	public int getPointsGamer2() {
		return this.pointsGamer2;
	}
	public int getPositionGamer2() {
		return this.positionGamer2;
	}
	
	public int getBallPositionX() {
		return this.ballPositionX;
	}
	public int getBallPositionY() {
		return this.ballPositionY;
	}

	public boolean getSessionActive() {
		return this.active;
	}
	public boolean getSessionFinish() {
		return this.finish;
	}
	public boolean hasUser(int id) {
		return (id == idGamer1) || (id == idGamer2);
	}
	public boolean haveFreeSlots() {
		return (idGamer1 == -1) || (idGamer2 == -1);
	}
}
