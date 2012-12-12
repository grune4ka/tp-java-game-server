package frontend;

import gameMechanics.GameSessionSnapshot;

import java.util.HashMap;

import messageSystem.MsgToFrontend;
import modules.Address;

public class MsgUpdateGamersStatus extends MsgToFrontend{
	private final GameSessionSnapshot[] snapshots;

	public MsgUpdateGamersStatus(Address from, Address to, GameSessionSnapshot[] snapshots) {			
			super(from, to);
			this.snapshots = snapshots;			
	}	

	public void exec(Frontend frontend) {
		frontend.updateGameSessionSnapshots(snapshots);		
	}
}
