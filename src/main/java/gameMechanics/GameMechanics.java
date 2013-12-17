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
		
	public void addUserToGame(int userId, GameSession gameSession) {

        for (int i = 0; i < this.gameSessions.size(); i++) {
			if (this.gameSessions.get(i).haveUser(userId)) {
				return;
			}			
		}
		
		for (int i = 0; i < this.gameSessions.size(); i++) {
			if (this.gameSessions.get(i).haveFreeSlots()) {
				this.gameSessions.get(i).addGamer(userId);
				return;
			}			
		}

		gameSession.addGamer(userId);
        this.gameSessions.add(gameSession);
		
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
	
	public void nextGamesTicks() {
		for(int i = 0; i < this.gameSessions.size(); i++) {
			if (this.gameSessions.get(i).canRemove()) {
				this.gameSessions.remove(i);
				if (i != 0) {
					i--;
				}
			} 
			else {
				long timer = System.currentTimeMillis();
                this.gameSessions.get(i).nextTick(timer);
			}
		}
	}
	
	public void run() {
		while (true) {
            MessageSystem.execForAbonent(this);

            MsgUpdateGamersStatus msg = new MsgUpdateGamersStatus(this.address,
                    AddressService.getAddressByServiceName("Frontend"),
                    this.getGameSessionSnapshotArray());
            MessageSystem.sendMessage(msg);
            this.nextGamesTicks();
            TimeHelper.sleep(10);
		}
	}

    public GameSession getGameSessionLast(){
        return gameSessions.lastElement();
    }

    public int getGameSessionsSize(){
        return this.gameSessions.size();
    }
}
