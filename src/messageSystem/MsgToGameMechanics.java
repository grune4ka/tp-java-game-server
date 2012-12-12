package messageSystem;

import gameMechanics.GameMechanics;
import modules.Abonent;
import modules.Address;
import modules.Msg;

public abstract class MsgToGameMechanics extends Msg{
	
	public MsgToGameMechanics(Address from, Address to) {
		super(from, to);
	}

	public void exec(Abonent abonent) {
		if(abonent instanceof GameMechanics){
			exec((GameMechanics)abonent);
		}
	}

	public abstract void exec(GameMechanics gameMechanics);
}
