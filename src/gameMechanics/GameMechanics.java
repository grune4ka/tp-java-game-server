package gameMechanics;

import frontend.MsgUpdateGamersStatus;
import helpers.TimeHelper;

import java.util.Vector;

import messageSystem.AddressService;
import messageSystem.MessageSystem;
import modules.Abonent;
import modules.Address;
import modules.GameMechanicsInterface;

public class GameMechanics implements Abonent, Runnable, GameMechanicsInterface {
	private Address address;
	private Vector<GameSession> gameSessions = new Vector<GameSession>(); 
	
	public GameMechanics() {
		address = new Address("GameMechanics");
		MessageSystem.addService(this);
		
	}
	public Address getAddress() {
		return address;
	}
		
	public void addUserToGame(int userId) {
		for (int i = 0; i < gameSessions.size(); i++) {
			if (gameSessions.get(i).haveFreeSlots()) {
				gameSessions.get(i).addGamer(userId);
				return;
			}			
		}
		GameSession newSession = new GameSession();
		newSession.addGamer(userId);
		gameSessions.add(newSession);
 	}
		
	public void updateBoardPosition(int userId, int position) {
		for(int i = 0; i < gameSessions.size(); i++) {
			if (gameSessions.get(i).haveUser(userId)) {
				gameSessions.get(i).setBoardPositionById(userId, position);
				break;
			}
		}
	}
	
	private GameSessionSnapshot[] getGameSessionSnapshotArray() {
		GameSessionSnapshot[] gameSessionSnapshotArray = new GameSessionSnapshot[gameSessions.size()];
		for(int i = 0; i < this.gameSessions.size(); i++) {
			gameSessionSnapshotArray[i] = this.gameSessions.get(i).getGameSessionSnapshot();
		}
		return gameSessionSnapshotArray;
	}
	
	public void run() {
		while (true) { 
			MessageSystem.execForAbonent(this);
			MsgUpdateGamersStatus msg = new MsgUpdateGamersStatus(this.address, 
					AddressService.getAddressByServiceName("Frontend"), 
					this.getGameSessionSnapshotArray());
			MessageSystem.sendMessage(msg);
			TimeHelper.sleep(10);
		}
	}
}
