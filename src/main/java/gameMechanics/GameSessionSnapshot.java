package gameMechanics;

import java.util.HashMap;

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
    private HashMap<String, String> map;
	
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
        this.map = new  HashMap<String, String>();
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
	public HashMap<String,String> getHashMapByUserId(int id) {

		if (this.finish) {
			this.map.put("results", new Boolean(true).toString());
			if (this.idGamer1 == id) {
				this.map.put("yourPoints", new Integer(this.pointsGamer1).toString());
				this.map.put("enemyPoints", new Integer(this.pointsGamer2).toString());
			}
			else {
				this.map.put("yourPoints", new Integer(this.pointsGamer2).toString());
				this.map.put("enemyPoints", new Integer(this.pointsGamer1).toString());
			}
			return this.map;
		}
		else {
			this.map.put("results", new Boolean(false).toString());
		}
		if (this.idGamer1 == id) {
			this.map.put("enemyPosition", new Integer(this.positionGamer2).toString());
			this.map.put("yourPoints", new Integer(this.pointsGamer1).toString());
			this.map.put("enemyPoints", new Integer(this.pointsGamer2).toString());
			this.map.put("ballXPos", new Integer(this.ballPositionX).toString());
			this.map.put("ballYPos", new Integer(this.ballPositionY).toString());
		}
		else {
			this.map.put("enemyPosition", new Integer(this.positionGamer1).toString());
			this.map.put("yourPoints", new Integer(this.pointsGamer2).toString());
			this.map.put("enemyPoints", new Integer(this.pointsGamer1).toString());
			this.map.put("ballXPos", new Integer(this.ballPositionX).toString());
			this.map.put("ballYPos", new Integer(480 - this.ballPositionY).toString());//to resurce
		}
		return this.map;
	}
}
