package frontendTest;

import frontend.Frontend;
import frontend.MsgUpdateUserId;
import modules.Address;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MsgUpdateUserIdTest {
    private MsgUpdateUserId msg;
    private Frontend frontend;
    private Address from;
    private Address to;

    @BeforeMethod
    public void setUp(){
        from = mock(Address.class);
        to = mock(Address.class);
        frontend = mock(Frontend.class);
        msg = new MsgUpdateUserId(from, to,  "1a", 1, "user");
    }

    @Test
    public void execTest(){
        msg.exec(frontend);
        verify(frontend).updateUserId("1a", 1, "user");
    }

}