package frontend;

import messageSystem.MsgToFrontend;
import modules.Address;

public class MsgUpdateUserId extends MsgToFrontend {
	final private String sessionId;
	final private int userId;
	final private String nick; 

	public MsgUpdateUserId(Address from, Address to, String sessionId, int userId, String nick) {
			super(from, to);
			this.sessionId = sessionId;
			this.userId = userId;
			this.nick = nick;
	}	

	public void exec(Frontend frontend) {
		frontend.updateUserId(sessionId,  userId, nick);
	}
}
