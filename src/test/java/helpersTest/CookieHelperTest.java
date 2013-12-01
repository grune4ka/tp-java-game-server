package helpersTest;

import helpers.CookieHelper;
import org.mockito.stubbing.OngoingStubbing;
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

    @BeforeClass
    public void setUp(){
        cookies = mock(Cookie.class);
        //when(cookies.length).thenReturn(2);
        helper = new CookieHelper();
    }

    @Test
    public void getStringCookieTest(){
        Assert.assertEquals(helper.getCookie(null,key), null);

    }
}
