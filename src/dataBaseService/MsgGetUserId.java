package dataBaseService;

import java.sql.SQLException;

import messageSystem.MessageSystem;
import messageSystem.MsgToDataBaseService;
import modules.Address;
import dataBaseService.users.UserDataSet;
import frontend.MsgUpdateUserId;


public class MsgGetUserId extends MsgToDataBaseService {
	final private String sessionId;
	final private String login;
	final private String password;

	public MsgGetUserId(Address from, Address to, String sessionId, String login, String password) {
		super(from, to);
		this.sessionId = sessionId;
		this.login = login;
		this.password = password;
	}

	public void exec(DataBaseService dataBaseService) {
		UserDataSet user = null;
		try {
			user = dataBaseService.getUDSByLP(login, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		MsgUpdateUserId msg = new MsgUpdateUserId(getTo(), getFrom(), sessionId, 
				user.getId(), user.getNick());
		MessageSystem.sendMessage(msg);
	}


}