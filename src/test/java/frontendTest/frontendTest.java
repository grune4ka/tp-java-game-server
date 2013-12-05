package frontendTest;


import frontend.Frontend;
import gameMechanics.GameSessionSnapshot;
import helpers.CookieHelper;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.SessionIdManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.eclipse.jetty.util.LazyList.add;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class frontendTest {
    private Frontend frontend;
    private Request baseRequest;
    private HttpServletRequest request;
    private HttpServletResponse response;

    private String targetRoot= "/";
    private String targetJoin = "/join";
    private String targetIsJoin = "/isJoin";
    private String targetGame = "/game";
    private String targetIsGameActive = "/isGameActive";
    private String targetUpdateGameData = "/updateGameData";
    private String targetLogout = "/logout";
    private String targetResults = "results";

    @BeforeMethod
    public void setUp(){
        frontend = new Frontend();
        baseRequest = mock(Request.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
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
        //возможно тут как раз ошибка, стоит ли добавлять информацию о сессии, если она не найдена?
    }

    @Test
    public void generationSessionIdTest(){
        //позитивный тест
        GameSessionSnapshot gameSessionSnapshot = mock(GameSessionSnapshot.class);
        int userId1 = 1;
        int userId2 = 2;
        frontend.addGameSessionSnapshots(gameSessionSnapshot);
        //when(frontend.gameSessionSnapshotsLength()).thenReturn(1);
       // when(frontend.gameSessionSnapshotsByIndex(1).hasUser(userId1)).thenReturn(true);
        //Assert.assertNotNull(frontend.getGameSessionSnapshotByUserId(userId1));

        //негативный тест
        //when(frontend.gameSessionSnapshotsByIndex(1).hasUser(userId2)).thenReturn(false);
        //Assert.assertNull(frontend.getGameSessionSnapshotByUserId(userId2));
    }

    @Test
    public void getGameSessionSnapshotByUserId(){

    }

    @Test
    public void handleTest() throws Exception{
        //frontend.handle(targetRoot, baseRequest, request, response);
    }

    @Test
    public void welcomeTest(){
        //позитивный тест, ид юзера не нулл
        //негативный тест, ид юзера нулл
    }

    @Test
    public void joinTest(){
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
    public void isJoinTest(){
        //позитивный тест, ид сессии не нулл, сессия активна, есть ид юзера>0
        //позитивный тест, ид сессии не нулл, сессия активна, доступ запрещен ,-3
        //позитивный тест, ид сессии не нулл, сессия активна,  уже присоединен, -4
        //негативный тест, ид сессии не нулл, сессия активна, есть ид юзера -2
        //негативный тест, ид сессии нулл
    }

    @Test
    public void gameTest(){
        //позитивный тест, игра, ид текущего юзера больше нуля, и нет больше свободных слотов
        //похоже какая-то ошибка в логике управления игрой!

        //позитивный тест, ожидание

        //негативный тест, редирект на страницу авторизации
    }

    @Test
    public void isGameActive(){

    }

    @Test
    public void updateGameDataTest(){

    }

    @Test
    public void resultsTest(){

    }

    @Test
    public void logoutTest(){

    }
}
