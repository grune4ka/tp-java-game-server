package frontendTest;


import freemarker.template.Configuration;
import frontend.Frontend;
import gameMechanics.GameSession;
import gameMechanics.GameSessionSnapshot;
import helpers.CookieHelper;
import helpers.TemplateHelper;
import helpers.TimeHelper;
import org.eclipse.jetty.server.Request;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class frontendTest {
    private Frontend frontend;
    private Request baseRequest;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Configuration cfg;
    private String targetRoot= "/";
    private String targetJoin = "/join";
    private String targetIsJoin = "/isJoin";
    private String targetGame = "/game";
    private String targetIsGameActive = "/isGameActive";
    private String targetUpdateGameData = "/updateGameData";
    private String targetLogout = "/logout";
    private String targetResults = "/results";
    private String targetNotFound = "/notFound";

    private Request baseRequestPositive;
    private HttpServletRequest requestPositive;
    private HttpServletResponse responsePositive;

    private Request baseRequestNegative;
    private HttpServletRequest requestNegative;
    private HttpServletResponse responseNegative;

    private TemplateHelper templateHelper;

    @BeforeMethod
    public void setUp(){
        cfg = mock(Configuration.class);
        frontend = new Frontend(cfg);
        ///frontend = new Frontend();
        baseRequest = mock(Request.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        baseRequestPositive = mock(Request.class);
        requestPositive = mock(HttpServletRequest.class);
        responsePositive = mock(HttpServletResponse.class);

        baseRequestNegative = mock(Request.class);
        requestNegative = mock(HttpServletRequest.class);
        responseNegative = mock(HttpServletResponse.class);

        templateHelper = mock(TemplateHelper.class);
        frontend.updateTemplateHelper(templateHelper);
    }

    @Test
    public void updateUserIdTest(){
        String sessionId ="1a2b";
        Integer userId = new Integer(1);
        String nick = "nick";
        frontend.updateUserId(sessionId, userId, nick);
        //доступ разрешен, обновилась информация сессии
        //доступ разрешен, обновилась информация юзера
        Assert.assertSame(Integer.valueOf(frontend.getSessionInformation().get(sessionId)), 1);
        Assert.assertEquals(frontend.getUserNameById().get(userId), nick);
        //доступ запрещен, повторная авторизация невозможна, обновилась сессия
        //доступ запрещен, повторная авторизацияневозможна, юзер не обновился
        frontend.updateUserId(sessionId, userId, nick);
        Assert.assertSame(Integer.valueOf(frontend.getSessionInformation().get(sessionId)), -4);
        Assert.assertEquals(frontend.getUserNameById().get(userId), nick);
        //некорректный ид юзера, обновилась сессия
        //некорректный ид юзера, ид не обновился
        Integer userIdIncorrect = new Integer(-1);
        frontend.updateUserId(sessionId, userIdIncorrect, nick);
        Assert.assertSame(Integer.valueOf(frontend.getSessionInformation().get(sessionId)), -3);
        Assert.assertEquals(frontend.getUserNameById().get(userId), nick);

    }

    @Test
    public void isBothUserInGame(){
        GameSessionSnapshot[] gameSessionSnapshots = new GameSessionSnapshot[1];
        GameSessionSnapshot snapshot = mock(GameSessionSnapshot.class);
        gameSessionSnapshots[0] = snapshot;
        when(snapshot.hasUser(1)).thenReturn(true);
        when(snapshot.haveFreeSlots()).thenReturn(false);
        frontend.updateGameSessionSnapshots(gameSessionSnapshots);
        Assert.assertTrue(frontend.isBothUserInGame(1));
    }

    @Test
    public void isUserJoinInGameSessionTest(){
        GameSessionSnapshot[] gameSessionSnapshots = new GameSessionSnapshot[1];
        GameSessionSnapshot snapshot = mock(GameSessionSnapshot.class);
        gameSessionSnapshots[0] = snapshot;
        when(snapshot.hasUser(1)).thenReturn(true);
        frontend.updateGameSessionSnapshots(gameSessionSnapshots);
        Assert.assertTrue(frontend.isUserJoinInGameSession(1));

    }

    @Test
    public void userNameByRequestTest(){
        //позитивный тест, ид сессии не нулл, сессия содержится в информации, есть ид юзера,он не меньше нуля, и и есть логин(меньше нуля, когда сессия только добавлена, либо какая-то ошибка)
        Cookie[] cookie1 =  new Cookie[1];
        cookie1[0] = new Cookie("sessionId", "1a");
        Request request1 = mock(Request.class);
        when(request1.getCookies()).thenReturn(cookie1);

        frontend.addSession("1a");
        frontend.updateUserId("1a", 1, "Vasya");
        Assert.assertEquals(frontend.userNameByRequest(request1),"Vasya");

        //негативный тест, ид сессии не нулл, сессия содержится в информации, есть ид юзера,он не меньше нуля, нет логина
        Cookie[] cookie2 =  new Cookie[1];
        cookie2[0] = new Cookie("sessionId", "1a2a");
        Request request2 = mock(Request.class);
        when(request2.getCookies()).thenReturn(cookie2);

        frontend.addSession("1a2b");
        frontend.updateUserId("1a2b", 1, "");
        Assert.assertNull(frontend.userNameByRequest(request2));

        //негативный тест,ид сессии не нулл, сессия содержится в информации, есть ид юзера,он меньше нуля
        Cookie[] cookie3 =  new Cookie[1];
        cookie3[0] = new Cookie("sessionId", "1a2a3b");
        Request request3 = mock(Request.class);
        when(request3.getCookies()).thenReturn(cookie3);

        frontend.addSession("1a2b3c");
        Assert.assertNull(frontend.userNameByRequest(request3));

        //негативный тест, ид сессии не нулл, сессии нет в информации
        Cookie[] cookie4 =  new Cookie[1];
        cookie4[0] = new Cookie("sessionId", "1a2a3b4c");
        Request request4 = mock(Request.class);
        when(request4.getCookies()).thenReturn(cookie4);
        Assert.assertNull(frontend.userNameByRequest(request3));

        //негативный тест,ид сессии нулл
        Cookie[] cookie5 =  new Cookie[1];
        cookie5[0] = new Cookie("sessionId", null);
        Request request5 = mock(Request.class);
        when(request5.getCookies()).thenReturn(cookie5);
        Assert.assertNull(frontend.userNameByRequest(request5));
    } 
    @Test
    public void userIdByRequestTest(){
        //позитивный тест, кука не равна нулю, в информации сессия содержится, код обновления (-2)
        Cookie[] cookie1 =  new Cookie[1];
        cookie1[0] = new Cookie("sessionId", "1a2b");
        Request request1 = mock(Request.class);
        when(request1.getCookies()).thenReturn(cookie1);

        frontend.addSession("1a2b");
        Assert.assertSame(Integer.valueOf(frontend.userIdByRequest(request1)), -2);

        //негативный тест, кука равну нулю
        Cookie[] cookie2 =  new Cookie[1];
        cookie2[0] = new Cookie("sessionId", null);
        Request request2 = mock(Request.class);
        when(request2.getCookies()).thenReturn(cookie2);

        Assert.assertSame(Integer.valueOf(frontend.userIdByRequest(request2)), -42);

        //негативный тест, кука не равна нулю, но информации о сессии нет
        Cookie[] cookie3 =  new Cookie[1];
        cookie3[0] = new Cookie("sessionId", "1a2a");
        Request request3 = mock(Request.class);
        when(request3.getCookies()).thenReturn(cookie3);

        Assert.assertSame(Integer.valueOf(frontend.userIdByRequest(request3)), -42);
    }

    @Test
    public void generationSessionIdTest(){
        Assert.assertNotNull(frontend.generationSessionId());
    }

    @Test
    public void getGameSessionSnapshotByUserId(){
        int userId1 = 1;
        int userId2 = 2;
        GameSessionSnapshot[] gameSessionSnapshots = new GameSessionSnapshot[1];
        GameSessionSnapshot gameSessionSnapshot = mock(GameSessionSnapshot.class);
        gameSessionSnapshots[0] = gameSessionSnapshot;
        when(gameSessionSnapshot.hasUser(userId1)).thenReturn(true);
        when(gameSessionSnapshot.hasUser(userId2)).thenReturn(false);
        frontend.updateGameSessionSnapshots(gameSessionSnapshots);
        Assert.assertEquals(frontend.getGameSessionSnapshotByUserId(userId1), gameSessionSnapshot);
        Assert.assertNull(frontend.getGameSessionSnapshotByUserId(userId2));

    }

    @Test
    public void handleWelcomeTest() throws Exception{
        Cookie[] cookie =  new Cookie[1];
        cookie[0] = new Cookie("sessionId", "1a");
        frontend.addSession("1a");
        frontend.updateUserId("1a", 1, "Vasya");
        when(baseRequestPositive.getCookies()).thenReturn(cookie);
        when(templateHelper.renderTemplate("index.html", "Vasya", responsePositive.getWriter())).thenReturn(true);
        frontend.handle(targetRoot, baseRequestPositive, requestPositive, responsePositive);
        verify(responsePositive).setContentType("text/html;charset=utf-8");
        verify(responsePositive).setStatus(HttpServletResponse.SC_OK);
        verify(responsePositive).setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        verify(responsePositive).setHeader("Expires", TimeHelper.getGMT());

        verify(baseRequestPositive).setHandled(true);
        Assert.assertTrue(frontend.IsHandled);
        frontend.updateUserId("1a", 1, null);
        when(baseRequestNegative.getCookies()).thenReturn(cookie);
        when(templateHelper.renderTemplate("index.html",  responseNegative.getWriter())).thenReturn(false);

        frontend.handle(targetRoot,baseRequestNegative, requestNegative, responseNegative);
        verify(responseNegative).setContentType("text/html;charset=utf-8");
        verify(responseNegative).setStatus(HttpServletResponse.SC_OK);
        verify(responseNegative).setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        verify(baseRequestNegative).setHandled(true);
        Assert.assertFalse(frontend.IsHandled);

    }

    @Test
    public void handleJoinTest() throws IOException, ServletException{
        Cookie[] cookie =  new Cookie[1];
        cookie[0] = new Cookie("sessionId", "1a");

        Map<String, Boolean> alreadyJoinMap = new HashMap<String, Boolean>();
        alreadyJoinMap.put("alreadyJoin", true);

        Map<String, Boolean> wrongDataMap = new HashMap<String, Boolean>();
        alreadyJoinMap.put("wrongData", true);

        when(requestPositive.getCookies()).thenReturn(cookie);

        when(templateHelper.renderTemplate("join.html", responsePositive.getWriter())).thenReturn(true);
        when(templateHelper.renderTemplate("join.html", alreadyJoinMap, responsePositive.getWriter())).thenReturn(true);
        when(templateHelper.renderTemplate("join.html", wrongDataMap, responsePositive.getWriter())).thenReturn(true);
        when(templateHelper.renderTemplate("join.html", responsePositive.getWriter())).thenReturn(true);
        when(templateHelper.renderTemplate("waitAuth.html", responsePositive.getWriter())).thenReturn(true);


        frontend.handle(targetJoin, baseRequestPositive, requestPositive, responsePositive);
        verify(responsePositive).setContentType("text/html;charset=utf-8");
        verify(responsePositive).setStatus(HttpServletResponse.SC_OK);
        verify(responsePositive).setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

        verify(baseRequestPositive).setHandled(true);
        Assert.assertTrue(frontend.IsHandled);

        frontend.addSession("1a");
        frontend.handle(targetJoin, baseRequestPositive, requestPositive, responsePositive);
        Assert.assertTrue(frontend.IsHandled);


        when(requestPositive.getParameter("login")).thenReturn(null);
        when(requestPositive.getParameter("password")).thenReturn(null);
        frontend.handle(targetJoin, baseRequestPositive, requestPositive, responsePositive);
        Assert.assertTrue(frontend.IsHandled);

        when(requestNegative.getParameter("login")).thenReturn("Vasya");
        when(requestNegative.getParameter("password")).thenReturn("password");
        when(templateHelper.renderTemplate("waitAuth.html", responseNegative.getWriter())).thenReturn(true);

        frontend.handle(targetJoin, baseRequestNegative, requestNegative, responseNegative);
        Assert.assertTrue(frontend.IsHandled);

        frontend.updateUserId("1a", 1, "Vasya");
        frontend.handle(targetJoin, baseRequestPositive, requestPositive, responsePositive);
        Assert.assertTrue(frontend.IsHandled);

        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("alreadyJoin", true);
        frontend.updateUserId("1a", 1, "Vasya");
        frontend.handle(targetJoin, baseRequestPositive, requestPositive, responsePositive);
        verify(templateHelper).renderTemplate("join.html", map, response.getWriter());
        Assert.assertTrue(frontend.IsHandled);

        Map<String, Boolean> map2 = new HashMap<String, Boolean>();
        map2.put("wrongData", true);
        frontend.updateUserId("1a", -1, "Vasya");
        frontend.handle(targetJoin, baseRequestPositive, requestPositive, responsePositive);
        templateHelper.renderTemplate("join.html", map2, responsePositive.getWriter());
        Assert.assertTrue(frontend.IsHandled);

    }

    @Test
    public void handleIsJoinTest() throws IOException, ServletException {
        frontend.addSession("1a");
        frontend.updateUserId("1a", 1, "Vasya");
        Cookie[] cookie =  new Cookie[1];
        cookie[0] = new Cookie("sessionId", "1a");
        when(baseRequestPositive.getCookies()).thenReturn(cookie);
        frontend.handle(targetIsJoin, baseRequestPositive, requestPositive, responsePositive);
        Assert.assertTrue(frontend.IsHandled);

    }

    @Test
    public void isGameActiveTest() throws IOException, ServletException{
        Cookie[] cookie =  new Cookie[2];
        cookie[0] = new Cookie("sessionId", "1a");
        cookie[1] =new Cookie("sessionId", "2a");
        frontend.addSession("1a");
        frontend.addSession("2a");
        frontend.updateUserId("1a", 1, "Vasya");
        frontend.updateUserId("1a", 2, "Petya");
        when(baseRequestPositive.getCookies()).thenReturn(cookie);
        when(baseRequestNegative.getCookies()).thenReturn(cookie);
        GameSessionSnapshot[] gameSessionSnapshots = new GameSessionSnapshot[1];
        GameSessionSnapshot gameSessionSnapshot = mock(GameSessionSnapshot.class);
        gameSessionSnapshots[0] = gameSessionSnapshot;
        frontend.updateGameSessionSnapshots(gameSessionSnapshots);

        int userId1 = 1;
        int userId2 = 2;
        when(frontend.gameSessionSnapshotsByIndex(0).hasUser(userId1)).thenReturn(true);
        when(frontend.gameSessionSnapshotsByIndex(0).hasUser(userId2)).thenReturn(true);
        when(frontend.gameSessionSnapshotsByIndex(0).haveFreeSlots()).thenReturn(false);
        frontend.handle(targetIsGameActive, baseRequestPositive, requestPositive, responsePositive);
        verify(responsePositive).setContentType("text/html;charset=utf-8");
        verify(responsePositive).setStatus(HttpServletResponse.SC_OK);
        verify(responsePositive).setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

        Assert.assertTrue(frontend.IsHandled);
        //frontend.handle(targetIsGameActive, baseRequestNegative,requestNegative, responseNegative);
        //Assert.assertTrue(frontend.IsHandled);

    }

    @Test
    public void updateGameDataTest() throws IOException, ServletException, NullPointerException{
        Cookie[] cookie =  new Cookie[1];
        cookie[0] = new Cookie("sessionId", "1a");
        frontend.addSession("1a");
        frontend.updateUserId("1a", 1, "Vasya");

        when(baseRequestPositive.getCookies()).thenReturn(cookie);
        when(requestPositive.getParameter("boardPos")).thenReturn("50");
        when(requestNegative.getParameter("boardPos")).thenReturn("50");

        frontend.handle(targetUpdateGameData, baseRequestPositive, requestPositive, responsePositive);
        verify(responsePositive).setContentType("text/html;charset=utf-8");
        verify(responsePositive).setStatus(HttpServletResponse.SC_OK);
        verify(responsePositive).setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        //verify(responsePositive).setHeader("Expires", TimeHelper.getGMT());

        verify(baseRequestPositive).setHandled(true);
        Assert.assertTrue(frontend.IsHandled);

        frontend.updateUserId("1a", 0, "Vasya");
        frontend.handle(targetUpdateGameData,baseRequestNegative, requestNegative, responseNegative);
        Assert.assertFalse(frontend.IsHandled);


    }

    @Test
    public void handleResultsTest() throws ServletException, IOException{

        when(requestPositive.getParameter("me")).thenReturn("10");
        when(requestPositive.getParameter("enemy")).thenReturn("10");

        when(requestNegative.getParameter("me")).thenReturn(null);
        when(requestNegative.getParameter("enemy")).thenReturn(null);

        when(templateHelper.renderTemplate("results.html", frontend.userNameByRequest(baseRequestPositive), frontend.results, responsePositive.getWriter())).thenReturn(true);
        when(templateHelper.renderTemplate("results.html", frontend.userNameByRequest(baseRequestNegative), frontend.results, responseNegative.getWriter())).thenReturn(true);
        frontend.handle(targetResults, baseRequestPositive, requestPositive, responsePositive);
        verify(responsePositive).setContentType("text/html;charset=utf-8");
        verify(responsePositive).setStatus(HttpServletResponse.SC_OK);
        verify(responsePositive).setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        //verify(responsePositive).setHeader("Expires", TimeHelper.getGMT());

        verify(baseRequestPositive).setHandled(true);
        Assert.assertTrue(frontend.IsHandled);

        frontend.handle(targetResults,baseRequestNegative, requestNegative, responseNegative);
        Assert.assertTrue(frontend.IsHandled);

    }

    @Test
    public void handleLogoutTest() throws IOException, ServletException{
        Cookie[] cookie =  new Cookie[1];
        cookie[0] = new Cookie("sessionId", "1a");
        when(baseRequest.getCookies()).thenReturn(cookie);
        when(templateHelper.renderTemplate("index.html", response.getWriter())).thenReturn(true);
        frontend.handle(targetLogout, baseRequest, request,response);
        verify(response).setContentType("text/html;charset=utf-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        //verify(response).setHeader("Expires", TimeHelper.getGMT());

        verify(baseRequest).setHandled(true);
        Assert.assertFalse(frontend.IsHandled);

        frontend.addSession("1a");
        frontend.updateUserId("1a", 1, "Vasya");
        frontend.handle(targetLogout, baseRequest, request,response);

        Assert.assertTrue(frontend.IsHandled);

    }

    @Test
    public void notFoundTest() throws IOException, ServletException{
        frontend.handle(targetNotFound, baseRequest, request,response);
        Assert.assertFalse(frontend.IsHandled);
    }

    @Test
    public void gameTest() throws  IOException, ServletException{
        String userName = "Vasya";
        Cookie[] cookie =  new Cookie[1];
        cookie[0] = new Cookie("sessionId", "1a");
        frontend.addSession("1a");
        frontend.updateUserId("1a", 1, "Vasya");

        GameSessionSnapshot[] gameSessionSnapshots = new GameSessionSnapshot[1];
        GameSessionSnapshot gameSessionSnapshot = mock(GameSessionSnapshot.class);
        gameSessionSnapshots[0] = gameSessionSnapshot;
        frontend.updateGameSessionSnapshots(gameSessionSnapshots);
        when(gameSessionSnapshot.hasUser(1)).thenReturn(true);
        when(gameSessionSnapshot.haveFreeSlots()).thenReturn(false);
        when(baseRequest.getCookies()).thenReturn(cookie);
        when(templateHelper.renderTemplate("game.html",userName,responsePositive.getWriter())).thenReturn(true);
        when(templateHelper.renderTemplate("wait.html",userName,responseNegative.getWriter())).thenReturn(true);
        frontend.handle(targetGame, baseRequest, request,responsePositive);
        Assert.assertTrue(frontend.IsHandled);

        frontend.handle(targetGame, baseRequest, request,responseNegative);
        Assert.assertTrue(frontend.IsHandled);
    }


}
