package dataBaseService.users;

public class UserDataSet {
	private int id;
	private String login;
	private String password;
	private String nick;
	
	public UserDataSet(int id, String login, String password, String nick) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.nick = nick;
	}
	
	public int getId() {
		return this.id;
	}
	public String getLogin() {
		return this.login;
	}
	public String getPassword() {
		return this.password;
	}
	public String getNick() {
		return this.nick;
	}
}
