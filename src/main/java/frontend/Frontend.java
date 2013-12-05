package frontend;

import gameMechanics.GameSessionSnapshot;
import gameMechanics.MsgConnectUserToGame;
import gameMechanics.MsgUpdateBoardPosition;
import helpers.CookieHelper;
import helpers.ParseHelper;
import helpers.TemplateHelper;
import helpers.TimeHelper;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import messageSystem.AddressService;
import messageSystem.MessageSystem;
import modules.Abonent;
import modules.Address;
import modules.FrontendInterface;
import modules.Responder;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.simple.JSONObject;

import dataBaseService.MsgGetUserId;

import static org.eclipse.jetty.util.LazyList.add;


public class Frontend extends AbstractHandler implements Abonent, Runnable,
		FrontendInterface {
	private HashMap<String, Integer> sessionInformation;
	private HashMap<Integer, String> userNameById;
	private GameSessionSnapshot[] gameSessionSnapshots;
	private Address address;

	public Frontend() {
		sessionInformation = new HashMap<String, Integer>();
		userNameById = new HashMap<Integer, String>();

		address = new Address("Frontend");
		MessageSystem.addService(this);
	}

	public Address getAddress() {
		return address;
	}

	public void updateUserId(String sessionId, Integer userId, String nick) {

        if (userId > 0) {
			if (sessionInformation.containsValue(userId)) {
				sessionInformation.put(sessionId, -4);
				System.out.println("Cессия " + sessionId + ": доступ запрещен. Повторная авторизация.");
				return;
			}
			sessionInformation.put(sessionId, userId);
            userNameById.put(userId, nick);

			System.out.println("К сессии '" + sessionId + "' добавлен userId "
				+ userId);
		}
		else {
			sessionInformation.put(sessionId, -3);
			System.out.println("Cессия " + sessionId + ": доступ запрещен.");
		}
	}

	public void updateGameSessionSnapshots(GameSessionSnapshot[] snapshots) {
		this.gameSessionSnapshots = snapshots;
	}

	public void sendRequestToUpdateUserId(String sessionId, String login, String password) {
		sessionInformation.put(sessionId, -1);
		MsgGetUserId msg = new MsgGetUserId(
				this.getAddress(), 
				AddressService.getAddressByServiceName("DataBaseService"),
				sessionId, login, password);
		MessageSystem.sendMessage(msg);
	}
	
	private void connectUserToGame(int userId) {
		MessageSystem.sendMessage(new MsgConnectUserToGame(
						this.getAddress(),
						AddressService.getAddressByServiceName("GameMechanics"),
						userId));
	}
	
	private boolean isBothUserInGame(int userId) {
		for(int i = 0; i < this.gameSessionSnapshots.length; i++) {
			if (this.gameSessionSnapshots[i].hasUser(userId) == true 
					&& !this.gameSessionSnapshots[i].haveFreeSlots()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isUserJoinInGameSession(int userId) {
		for(int i = 0; i < this.gameSessionSnapshots.length; i++) {
			if (this.gameSessionSnapshots[i].hasUser(userId) == true) {
				return true;
			}
		}
		return false;
	}
	
	public String userNameByRequest(Request request) {
		String sessionId = CookieHelper.getCookie(request.getCookies(), "sessionId");
		if (sessionId != null) {
			if (sessionInformation.containsKey(sessionId)) {
				int id = sessionInformation.get(sessionId);
				if (id > 0) {
					if (userNameById.containsKey(id)) {
						String nick = userNameById.get(id);
						return nick;
					}
				}
			}
		}
		return null;
	}
	
	public int userIdByRequest(Request request) {
		String sessionId = CookieHelper.getCookie(request.getCookies(), "sessionId");
		if (sessionId != null) {
			if (sessionInformation.containsKey(sessionId)) {
				return sessionInformation.get(sessionId);
			}
		}
		return -42;
	}
	
	public void addSession(String sessionId) {
		sessionInformation.put(sessionId, -2);
	}

	private boolean isSessionActive(String sessionId) {
		return sessionInformation.containsKey(sessionId);
	}

	private int userIdBySessionId(String sessionId) {
		if (sessionInformation.get(sessionId) == null) {
			return -1;
		}
		return sessionInformation.get(sessionId);
	}

	private String generationSessionId() {
		String hashString = String.valueOf(System.currentTimeMillis());
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		md.reset();
		md.update(hashString.getBytes());

		return md.digest().toString();
	}
	
	public GameSessionSnapshot getGameSessionSnapshotByUserId(int userId) {
		for(int i = 0; i < gameSessionSnapshotsLength(); i++) {
			if (gameSessionSnapshotsByIndex(i).hasUser(userId)) {
				return gameSessionSnapshots[i];
			}
		}
		return null;
	}
	

	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);

		response.setHeader("Cache-Control","no-store, no-cache, must-revalidate");
		response.setHeader("Expires", TimeHelper.getGMT());

		if (target.equals("/")) {
			this.welcome(target, baseRequest, request, response);
			baseRequest.setHandled(true);
			return;
		}

		if (target.equals("/join")) {
			this.join(target, baseRequest, request, response);
			baseRequest.setHandled(true);
			return;
		}
		
		if (target.equals("/isJoin")) {
			this.isJoin(target, baseRequest, request, response);
			baseRequest.setHandled(true);
			return;
		}

		if (target.equals("/game")) {
			this.game(target, baseRequest, request, response);
			baseRequest.setHandled(true);
			return;
		}
		
		if (target.equals("/isGameActive")) {
			this.isGameActive(target, baseRequest, request, response);
			baseRequest.setHandled(true);
			return;
		}
		
		if (target.equals("/updateGameData")) {
			this.updateGameData(target, baseRequest, request, response);
			baseRequest.setHandled(true);
			return;
		}
		
		if (target.equals("/logout")) {
			this.logout(target, baseRequest, request, response);
			baseRequest.setHandled(true);
			return;
		}
		
		if (target.equals("/results")) {
			results(target, baseRequest, request, response);
			baseRequest.setHandled(true);
			return;
		}
		
		//добавить обработчик для 404 ошибки.
	}
	
	@Responder
	private void welcome(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String userName = this.userNameByRequest(baseRequest); 
		if (userName != null) {
			TemplateHelper.renderTemplate("index.html", userName, response.getWriter());
		}
		else {
			TemplateHelper.renderTemplate("index.html", response.getWriter());
		}
	}
	
	@Responder
	private void join(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String sessionId = CookieHelper.getCookie(request.getCookies(),"sessionId");
		if (sessionId == null || !this.isSessionActive(sessionId)) {
			sessionId = this.generationSessionId();
			Cookie[] cookie = new Cookie[1];
			cookie[0] = new Cookie("sessionId", target);
			cookie[0].setValue(sessionId);
			response.addCookie(cookie[0]);
			this.addSession(sessionId);
			TemplateHelper.renderTemplate("join.html", response.getWriter());
		} else {
			if (this.userIdBySessionId(sessionId) > 0) {
				response.sendRedirect("/");
				return;
			}
			else if (this.userIdBySessionId(sessionId) == -4) {
				Map<String, Boolean> map = new HashMap<String, Boolean>();
				map.put("alreadyJoin", true);
				TemplateHelper.renderTemplate("join.html", map, response.getWriter());
				this.sessionInformation.put(sessionId, -2);
			}
			else if (this.userIdBySessionId(sessionId) == -3) {
				Map<String, Boolean> map = new HashMap<String, Boolean>();
				map.put("wrongData", true);
				TemplateHelper.renderTemplate("join.html", map, response.getWriter());
				this.sessionInformation.put(sessionId, -2);
			}
			else if (this.userIdBySessionId(sessionId) == -2) {
				String	login = request.getParameter("login");
				String	password = request.getParameter("password");
				if (login == null || password == null) {
					TemplateHelper.renderTemplate("join.html", response.getWriter());
				}
				else {
					this.sendRequestToUpdateUserId(sessionId, login, password);
					TemplateHelper.renderTemplate("waitAuth.html", response.getWriter());
				}
			} else if (this.userIdBySessionId(sessionId) == -1) {
				TemplateHelper.renderTemplate("waitAuth.html", response.getWriter());
			}
		}
	}
	
	@Responder
	private void isJoin(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String sessionId = CookieHelper.getCookie(baseRequest.getCookies(),
				"sessionId");
		boolean isJoin = false;
		JSONObject obj = new JSONObject();
		if (sessionId != null) {
			if (this.isSessionActive(sessionId)) {
				if (this.userIdBySessionId(sessionId) > 0 || this.userIdBySessionId(sessionId) == -3 || this.userIdBySessionId(sessionId) == -4) {
					isJoin = true;
				}
			}
		}
		obj.put("isJoin", isJoin);
		response.getWriter().print(obj);
	}
	
	@Responder
	private void game(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		int userId = this.userIdByRequest(baseRequest);
		if (userId > 0) {
			if (this.isBothUserInGame(userId) == true) {
				TemplateHelper.renderTemplate("game.html", 
						this.userNameById.get(userId), 
						response.getWriter());
			}
			else {
				if (!this.isUserJoinInGameSession(userId)) {
					this.connectUserToGame(userId);
				}
				TemplateHelper.renderTemplate("wait.html", 
						this.userNameById.get(userId), 
						response.getWriter());
			}
		}
		else {
			response.sendRedirect("/join");
		}
	}
	
	@Responder
	private void isGameActive(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		boolean isGameActive = false;
		JSONObject obj = new JSONObject();			
		isGameActive = this.isBothUserInGame(this.userIdByRequest(baseRequest));
		obj.put("isGameActive", isGameActive);
		response.getWriter().print(obj);
	}
	
	@Responder
	private void updateGameData(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		int userId = this.userIdByRequest(baseRequest);
		if (userId <= 0) {
			response.sendRedirect("/join");
			return;
		}
		String boardPosition = request.getParameter("boardPos");
		if (boardPosition != null) {
			int intBoardPosition = ParseHelper.strToInt(boardPosition);
			MsgUpdateBoardPosition msg = new MsgUpdateBoardPosition(this.getAddress(), 
					AddressService.getAddressByServiceName("GameMechanics"), 
					userId, intBoardPosition);
			MessageSystem.sendMessage(msg);
		}
		
		GameSessionSnapshot snapshot = this.getGameSessionSnapshotByUserId(userId);
		JSONObject obj = new JSONObject();			
		obj.putAll(snapshot.getHashMapByUserId(userId));
		response.getWriter().print(obj);
	}
	
	@Responder
	private void results(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String mePoints = request.getParameter("me");
		String enemyPoints = request.getParameter("enemy");
		if (mePoints == null) {
			mePoints = "";
		}
		if (enemyPoints == null) {
			enemyPoints = "";
		}
		HashMap<String, String> results = new HashMap<String, String>();
		results.put("me", mePoints);
		results.put("enemy", enemyPoints);
		TemplateHelper.renderTemplate("results.html", userNameByRequest(baseRequest), results, response.getWriter());
	}
	
	@Responder
	private void logout(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String sessionId = CookieHelper.getCookie(baseRequest.getCookies(), "sessionId");
		if (sessionId != null) {
			if (sessionInformation.containsKey(sessionId)) {
				int id = sessionInformation.get(sessionId);
				if (id > 0) {
					if (userNameById.containsKey(id)) {
						System.out.println("Пользователь #"+id+" (Сессия " + sessionId+") вышел из системы");
						userNameById.remove(id);
						sessionInformation.remove(sessionId);
						response.sendRedirect("/");
						return;
					}
				}
			}
		}
		TemplateHelper.renderTemplate("index.html", response.getWriter());
	}
		
	public void run() {
		while (true) {
			MessageSystem.execForAbonent(this);
			TimeHelper.sleep(50);
		}
	}

    public HashMap<String, Integer> getSessionInformation(){
        return sessionInformation;
    }

    public HashMap<Integer, String> getUserNameById(){
        return userNameById;
    }

    public GameSessionSnapshot[] getGameSessionSnapshots(){
        return gameSessionSnapshots;
    }

    public void addGameSessionSnapshots(GameSessionSnapshot gameSessionSnapshot){
        add(this.gameSessionSnapshots, gameSessionSnapshot);
    }

    public int gameSessionSnapshotsLength(){
        return this.gameSessionSnapshots.length;
    }

    public GameSessionSnapshot gameSessionSnapshotsByIndex(int i){
        return  gameSessionSnapshots[i];
    }
}
