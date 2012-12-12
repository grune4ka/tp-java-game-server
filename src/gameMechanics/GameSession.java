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
		int idGamer1;
		int pointsGamer1;
		int positionGamer1;
		
		int idGamer2;
		int pointsGamer2;
		int positionGamer2;
		
		int ballPositionX;
		int ballPositionY;
		
		boolean active;
		boolean finish;
		
		
		
		
		GameSessionSnapshot i = new GameSessionSnapshot(
				gamer1.getId(), gamer1.getPoints(), gamer1.getBoardPosition(),
				gamer2.getId(), gamer2.getPoints(), gamer2.getBoardPosition(), 
				this.ballXPosition, this.ballYPosition, 
				this.active, this.finish);
		return i;
	}
	
	public void nextTickSession() {
		if (this.ballXPosition == 0 || this.ballXPosition == this.widthGameSpace) {
			this.ballVector[0] = - this.ballVector[0]; 
		}
		
		if (this.ballYPosition == 0 || this.ballYPosition == this.heightGameSpace) {
			this.ballVector[1] = - this.ballVector[1];
		}
		this.ballXPosition += this.ballVector[0];
		this.ballYPosition += this.ballVector[1];
	}
}
