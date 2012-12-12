package gameMechanics;


public class GameSession {
	private Gamer gamer1;
	private Gamer gamer2;
	
	private boolean active;
	private boolean finish;
	
	private int ballXPosition;
	private int ballYPosition;
	
	private int widthGameSpace;
	private int heightGameSpace;
	
	private int[] ballVector;
	
	public GameSession() {
		this.gamer1 = null;
		this.gamer2 = null;
		
		this.active = false;
		this.finish = false;
		
		this.widthGameSpace = 360; // в
		this.heightGameSpace = 480;// ресурсы
		
		this.widthGameSpace -= 20;
		this.heightGameSpace += 20;
		
		this.ballXPosition = this.widthGameSpace / 2;
		this.ballYPosition = this.heightGameSpace / 2;
		
		int[] ballVector = {1,1};
		this.ballVector = ballVector;
	}
	
	public boolean addGamer(int id) {
		if (gamer1 == null) {
			gamer1 = new Gamer(id);
		}
		else if (gamer2 == null) {
			gamer2 = new Gamer(id);
		}
		else {
			return false;
		}
		if (!this.haveFreeSlots()) {
			this.gameStart();
		}
		return true;
	}
	
	public boolean haveFreeSlots() {
		return gamer1 == null || gamer2 == null;
	}
	
	public boolean haveUser(int userId) {
		if (this.gamer1 != null) {
			if (this.gamer1.getId() == userId) {
				return true;
			}
		}
		if (this.gamer2 != null) {
			if (this.gamer2.getId() == userId) {
				return true;
			}
		}
		return false;
	}
	
	public void setBoardPositionById(int userId, int boardPosition) {
		if (this.gamer1 != null) {
			if (this.gamer1.getId() == userId) {
				this.gamer1.setBoardPosition(boardPosition);
				return;
			}
		}
		if (this.gamer2 != null) {
			if (this.gamer2.getId() == userId) {
				this.gamer2.setBoardPosition(boardPosition);
				return;
			}
		}
	}
	
	public boolean isGameEnd() {
		if (gamer1.getPoints() > 7 || gamer2.getPoints() > 7) { //вынести 7ку в ресурсы.
			this.active = false;
			this.finish = true;
			return true;
		}
		else {
			return false;
		}		
	}
	private void gameStart() {
		this.active = true; 
	}
	
	public GameSessionSnapshot getGameSessionSnapshot() {
		int idGamer1 = -1;
		int pointsGamer1 = -1;
		int positionGamer1 = -1;
		
		int idGamer2 = -1;
		int pointsGamer2 = -1;
		int positionGamer2 = -1;
				
		if (this.gamer1 != null) {
			idGamer1 = this.gamer1.getId();
			pointsGamer1 = this.gamer1.getPoints();
			positionGamer1 = this.gamer1.getBoardPosition();
		}
		
		if (this.gamer2 != null) {
			idGamer2 = this.gamer2.getId();
			pointsGamer2 = this.gamer2.getPoints();
			positionGamer2 = this.gamer2.getBoardPosition();
		}
		
		
		GameSessionSnapshot i = new GameSessionSnapshot(
				idGamer1, pointsGamer1, positionGamer1,
				idGamer2, pointsGamer2, positionGamer2, 
				this.ballXPosition, this.ballYPosition, 
				this.active, this.finish);
		return i;
	}
	
	public void nextTick() {
		if (this.ballXPosition == 0 || this.ballXPosition == this.widthGameSpace) {
			this.ballVector[0] = - this.ballVector[0]; 
		}
		
		if (this.ballYPosition == -30 || this.ballYPosition == this.heightGameSpace) {
			this.ballVector[1] = - this.ballVector[1];
		}
		this.ballXPosition += this.ballVector[0];
		this.ballYPosition += this.ballVector[1];
	}
}
