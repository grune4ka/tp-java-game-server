package helpersTest;

import helpers.CookieHelper;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.servlet.http.Cookie;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CookieHelperTest {

    private Cookie cookies;
    private CookieHelper helper;
    private String key="password";
    private Cookie[] cookie;
    @BeforeClass
    public void setUp(){
        cookies = mock(Cookie.class);
        helper = new CookieHelper();
        cookie =  new Cookie[1];
        cookie[0] = new Cookie("sessionId", "1a");
    }

    @Test
    public void getStringCookieTest(){
        Assert.assertEquals(helper.getCookie(null,key), null);
        Assert.assertEquals(helper.getCookie(cookie, "sessionId"), "1a");

    }
}
