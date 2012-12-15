package gameMechanics;

import gameServer.GameSettings;
import resources.ResourceFactory;


public class GameSession {
	private Gamer gamer1;
	private Gamer gamer2;
	
	private boolean active;
	private boolean finish;
	
	private long finishTime = 0;
	
	private int ballXPosition;
	private int ballYPosition;
		
	private long waiter = 0;
		
	private GameSettings settings;
	
	public GameSession() {
		this.gamer1 = null;
		this.gamer2 = null;
		
		this.active = false;
		this.finish = false;
		
		this.settings = (GameSettings) ResourceFactory.instanse()
				.getResource("settings/game_settings.xml");
				
		this.settings.widthGameField -= 20;
		this.settings.heigthGameField += 20;
		
		this.ballXPosition = this.settings.widthGameField / 2;
		this.ballYPosition = this.settings.heigthGameField / 2;
	}
	
	public boolean canRemove() {
		if (System.currentTimeMillis() - this.finishTime > 1000 && this.finishTime != 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private void setWaiter() {
		this.waiter = System.currentTimeMillis();
	}
	
	private boolean isWait() {
		if (System.currentTimeMillis() - this.waiter < this.settings.failWait && this.waiter != 0) {
			return true;		
		}
		else {
			return false;
		}
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
		if (this.gamer1 != null && this.gamer2 != null) {
			if (gamer1.getPoints() >= this.settings.winCounts || gamer2.getPoints() >= this.settings.winCounts) { //вынести 7ку в ресурсы.
				this.active = false;
				if (this.finish == false) {
					this.finishTime = System.currentTimeMillis();
				}
				this.finish = true;
				return true;
			}
			else {
				return false;
			}
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
				this.active, this.isGameEnd());
		return i;
	}
	
	public void nextTick() {
		if (!this.haveFreeSlots() && !this.isGameEnd() && !this.isWait()){
			if (this.ballXPosition < 0 || this.ballXPosition > this.settings.widthGameField) {
				this.settings.vector[0] = - this.settings.vector[0]; 
			}
			if (this.ballYPosition < 50 ) {
				if (this.gamer2.getBoardPosition() - 30  < this.ballXPosition && this.ballXPosition < this.gamer2.getBoardPosition() + 30) {
					if (this.settings.vector[1] < 0) {
						this.settings.vector[1] = - this.settings.vector[1];
					}
				}
				else {
					this.gamer1.setPoints(this.gamer1.getPoints() + 1);
					this.ballXPosition = this.settings.widthGameField / 2;
					this.ballYPosition = this.settings.heigthGameField / 2;
					this.setWaiter();
					return;
				}
			}
			
			if (this.ballYPosition > this.settings.heigthGameField - 70) {
				if ((this.gamer1.getBoardPosition() - 30  < this.ballXPosition && this.ballXPosition < this.gamer1.getBoardPosition() + 30)) {
					if (this.settings.vector[1] > 0) {
						this.settings.vector[1] = - this.settings.vector[1];
					}
				}
				else {
					this.gamer2.setPoints(this.gamer2.getPoints() + 1);
					this.ballXPosition = this.settings.widthGameField / 2;
					this.ballYPosition = this.settings.heigthGameField/ 2;
					this.setWaiter();
					return;
				}
			}			
			this.ballXPosition += this.settings.vector[0];
			this.ballYPosition += this.settings.vector[1];
		}
	}
}
