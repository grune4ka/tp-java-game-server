package gameMechanics;

import messageSystem.MsgToGameMechanics;
import modules.Address;

public class MsgUpdateBoardPosition extends MsgToGameMechanics {
	private final int userId;
	private final int boardPosition;
	
	public MsgUpdateBoardPosition (Address from, Address to, int userId, int boardPosition) {
		super(from, to);
		this.userId = userId;
		this.boardPosition = boardPosition;
	}	

	public void exec(GameMechanics gameMechanics) {
		gameMechanics.updateBoardPosition(userId, boardPosition);
	}
}
