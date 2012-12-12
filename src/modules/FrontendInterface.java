package modules;

public interface FrontendInterface {
	public Address getAddress();
	public void updateUserId(String sessionId, Integer userId, String nick);
	public void sendRequestToUpdateUserId(String sessionId, String login, String password);
}
