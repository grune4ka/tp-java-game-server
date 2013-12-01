package frontendTest;


import frontend.Frontend;
import helpers.CookieHelper;
import junit.framework.Assert;
import org.eclipse.jetty.server.Request;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

        CookieHelper cookie1;
        CookieHelper cookie2;
        Request request;

        cookie1 = mock(CookieHelper.class);
        cookie2 = mock(CookieHelper.class);
        request = mock(Request.class);
        when(cookie1.getCookie(request.getCookies(),"SessionId")).thenReturn(null);
        Assert.assertNull(frontend.userNameByRequest(request));

        //when(cookie2.getCookie(request.getCookies(), "SessionId")).thenReturn("SessionId");

    }

    @Test
    public void userIdByRequestTest(){

    }

    @Test
    public void generationSessionIdTest(){

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

    }

    @Test
    public void joinTest(){

    }

    @Test
    public void isJoinTest(){

    }

    @Test
    public void gameTest(){

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
