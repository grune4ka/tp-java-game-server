package messageSystem;

import modules.Abonent;
import modules.Address;
import modules.Msg;
import dataBaseService.DataBaseService;

public abstract class MsgToDataBaseService extends Msg {

	public MsgToDataBaseService(Address from, Address to) {
		super(from, to);
	}

	public void exec(Abonent abonent) {
		if(abonent instanceof DataBaseService){
			exec((DataBaseService)abonent);
		}
	}

	public abstract void exec(DataBaseService dataBaseService);
}