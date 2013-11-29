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
	public HashMap<String,String> getHashMapByUserId(int id) {
		HashMap<String, String> map = new HashMap<String, String>();
		if (this.finish) {
			map.put("results", new Boolean(true).toString());
			if (this.idGamer1 == id) {
				map.put("yourPoints", new Integer(this.pointsGamer1).toString());
				map.put("enemyPoints", new Integer(this.pointsGamer2).toString());
			}
			else {
				map.put("yourPoints", new Integer(this.pointsGamer2).toString());
				map.put("enemyPoints", new Integer(this.pointsGamer1).toString());
			}
			return map;
		}
		else {
			map.put("results", new Boolean(false).toString());
		}
		if (this.idGamer1 == id) {
			map.put("enemyPosition", new Integer(this.positionGamer2).toString());
			map.put("yourPoints", new Integer(this.pointsGamer1).toString());
			map.put("enemyPoints", new Integer(this.pointsGamer2).toString());
			map.put("ballXPos", new Integer(this.ballPositionX).toString());
			map.put("ballYPos", new Integer(this.ballPositionY).toString());
		}
		else {
			map.put("enemyPosition", new Integer(this.positionGamer1).toString());
			map.put("yourPoints", new Integer(this.pointsGamer2).toString());
			map.put("enemyPoints", new Integer(this.pointsGamer1).toString());
			map.put("ballXPos", new Integer(this.ballPositionX).toString());
			map.put("ballYPos", new Integer(480 - this.ballPositionY).toString());//to resurce
		}
		return map;
	}
}