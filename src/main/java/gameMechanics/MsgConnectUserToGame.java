package gameMechanics;

import messageSystem.MsgToGameMechanics;
import modules.Address;

public class MsgConnectUserToGame extends MsgToGameMechanics {
	int userId;

    public MsgConnectUserToGame (Address from, Address to, int userId) {
			super(from, to);
			this.userId = userId;
	}	

	public void exec(GameMechanics gameMechanics) {
		GameSession gameSession = new GameSession();
        gameMechanics.addUserToGame(userId, gameSession);
	}
}
