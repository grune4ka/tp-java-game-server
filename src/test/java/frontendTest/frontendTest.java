package frontendTest;


import freemarker.template.Configuration;
import frontend.Frontend;
import gameMechanics.GameSessionSnapshot;
import helpers.CookieHelper;
import helpers.TemplateHelper;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.SessionIdManager;
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

import static org.eclipse.jetty.util.LazyList.add;
import static org.mockito.Matchers.anyMap;
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
        //попробовать передать вместо входа везде null
    }

    @Test
    public void sendRequestToUpdateUserIdTest(){

    }

    @Test
    public void connectUserToGameTest(){

    }

    @Test
    public void isBothUserInGame(){

    }

    @Test
    public void isUserJoinInGameSessionTest(){

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
        Assert.assertTrue(frontend.IsHandled);
        frontend.updateUserId("1a", 1, null);
        when(baseRequestNegative.getCookies()).thenReturn(cookie);
        when(templateHelper.renderTemplate("index.html",  responseNegative.getWriter())).thenReturn(false);

        frontend.handle(targetRoot,baseRequestNegative, requestNegative, responseNegative);
        Assert.assertFalse(frontend.IsHandled);

    }

    @Test
    public void handleJoinTest() throws IOException, ServletException{
        Cookie[] cookie =  new Cookie[1];
        cookie[0] = new Cookie("sessionId", "1a");
        when(requestPositive.getCookies()).thenReturn(cookie);
        frontend.handle(targetJoin, baseRequestPositive, requestPositive, responsePositive);
        Assert.assertTrue(frontend.IsHandled);
        frontend.addSession("1a");
        frontend.updateUserId("1a", 1, "Vasya");
        frontend.handle(targetJoin, baseRequestPositive, requestPositive, responsePositive);
        Assert.assertTrue(frontend.IsHandled);
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("alreadyJoin", true);

        when(templateHelper.renderTemplate("join.html", response.getWriter())).thenReturn(true);
        when(templateHelper.renderTemplate("join.html", map, response.getWriter())).thenReturn(true);
       /* when(templateHelper.renderTemplate("join.html", anyMap(), response.getWriter())).thenReturn(true);
        when(templateHelper.renderTemplate("join.html", response.getWriter())).thenReturn(true);
        when(templateHelper.renderTemplate("waitAuth.html", response.getWriter())).thenReturn(true);
        when(templateHelper.renderTemplate("waitAuth.html", response.getWriter())).thenReturn(true);
*/

        //позитивный тест, есть ид сессии, сессия активна

        //негативный тест, есть ид сессии, сессия неактивна
        //негативный тест, есть ид пользователя, но он неавторизован -> отправитьна страницу авторизации, т.е активировать сессию
        //негативный тест, пользователь уже присоединен
        //негативный тест, пользователю доступ запрещен
        //негативный тест, у пользователя нет айди
        //негативный тест, у пользователя нет логина/пароля
        //негативный тест, у пользователя есть логин, пароль, нужно обновить сессию
        //негативный тест, ожидание авторизации
    }

    @Test
    public void handleIsJoinTest() throws IOException, ServletException {
       /* frontend.addSession("1a");
        Cookie[] cookie1 =  new Cookie[1];
        cookie1[0] = new Cookie("sessionId", "1a");
        when(baseRequest.getCookies()).thenReturn("1a");*/
        //позитивный тест, ид сессии не нулл, сессия активна, есть ид юзера>0
        //позитивный тест, ид сессии не нулл, сессия активна, доступ запрещен ,-3
        //позитивный тест, ид сессии не нулл, сессия активна,  уже присоединен, -4
        //негативный тест, ид сессии не нулл, сессия активна, есть ид юзера -2
        //негативный тест, ид сессии нулл
    }

    @Test
    public void handleGameTest() throws IOException, ServletException{
        Cookie[] cookie =  new Cookie[2];
        cookie[0] = new Cookie("sessionId", "1a");
        cookie[1] =new Cookie("sessionId", "2a");
        frontend.addSession("1a");
        frontend.addSession("2a");
        frontend.updateUserId("1a", 1, "Vasya");
        frontend.updateUserId("1a", 2, "Petya");

        GameSessionSnapshot[] gameSessionSnapshots = new GameSessionSnapshot[1];
        GameSessionSnapshot gameSessionSnapshot = mock(GameSessionSnapshot.class);
        gameSessionSnapshots[0] = gameSessionSnapshot;
        frontend.updateGameSessionSnapshots(gameSessionSnapshots);

        int userId1 = 1;
        int userId2 = 2;
        when(baseRequestPositive.getCookies()).thenReturn(cookie);
        when(baseRequestNegative.getCookies()).thenReturn(cookie);


        when(frontend.gameSessionSnapshotsByIndex(0).hasUser(userId1)).thenReturn(true);
        when(frontend.gameSessionSnapshotsByIndex(0).hasUser(userId2)).thenReturn(true);
        when(frontend.gameSessionSnapshotsByIndex(0).haveFreeSlots()).thenReturn(false);
        when(templateHelper.renderTemplate("game.html", frontend.userNameById(userId1),response.getWriter())).thenReturn(true);
        when(templateHelper.renderTemplate("wait.html", frontend.userNameById(userId1), response.getWriter())).thenReturn(true);

        /*int userId3 = 3;
        Request baseRequestPositive2 = mock(Request.class);
        HttpServletRequest requestPositive2 = mock(HttpServletRequest.class);
        HttpServletResponse responsePositive2 = mock(HttpServletResponse.class);
        when(frontend.gameSessionSnapshotsByIndex(0).hasUser(userId3)).thenReturn(false);
*/
        //frontend.handle(targetGame, baseRequestPositive, requestPositive, responsePositive);
        //Assert.assertTrue(frontend.IsHandled);

        //isUserJoinInGameSession test
        //when(frontend.userIdByRequest(baseRequestPositive2)).thenReturn(userId3);
        //when(frontend.gameSessionSnapshotsByIndex(0).hasUser(userId3)).thenReturn(false);
        //Assert.assertTrue(frontend.handle(targetGame, baseRequestPositive2, requestPositive2, responsePositive2));

        //Assert.assertFalse(frontend.handle(targetGame,baseRequestNegative, requestNegative, responseNegative));
        //позитивный тест, игра, ид текущего юзера больше нуля, и нет больше свободных слотов
        //похоже какая-то ошибка в логике управления игрой!

        //позитивный тест, ожидание

        //негативный тест, редирект на страницу авторизации
    }

    @Test
    public void isGameActive() throws IOException, ServletException{
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
        frontend.handle(targetIsGameActive, baseRequestPositive,requestPositive, responsePositive);
        Assert.assertTrue(frontend.IsHandled);
        //frontend.handle(targetIsGameActive, baseRequestNegative,requestNegative, responseNegative);
        //Assert.assertTrue(frontend.IsHandled);

    }

    @Test
    public void updateGameDataTest() throws IOException, ServletException{
        Cookie[] cookie =  new Cookie[1];
        cookie[0] = new Cookie("sessionId", "1a");
        frontend.addSession("1a");
        frontend.updateUserId("1a", 1, "Vasya");

        when(baseRequestPositive.getCookies()).thenReturn(cookie);
        when(requestPositive.getParameter("boardPos")).thenReturn("50");
        when(requestNegative.getParameter("boardPos")).thenReturn("50");

        frontend.handle(targetUpdateGameData, baseRequestPositive, requestPositive, responsePositive);
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
        Assert.assertFalse(frontend.IsHandled);

        frontend.addSession("1a");
        frontend.updateUserId("1a", 1, "Vasya");
        frontend.handle(targetLogout, baseRequest, request,response);

        Assert.assertTrue(frontend.IsHandled);

    }
}
