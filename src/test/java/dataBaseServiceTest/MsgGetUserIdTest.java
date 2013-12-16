package dataBaseServiceTest;


import dataBaseService.DataBaseService;
import dataBaseService.MsgGetUserId;
import dataBaseService.users.UserDataSet;
import modules.Address;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MsgGetUserIdTest {
    private DataBaseService dataBaseService;
    private Address from;
    private Address to;
    private MsgGetUserId msg;
    private UserDataSet user;

    @BeforeMethod
    public void setUp() throws  SQLException{
        from = mock(Address.class);
        to = mock(Address.class);
        user = mock(UserDataSet.class);
        dataBaseService = mock(DataBaseService.class);
        when(user.getId()).thenReturn(1);
        when(user.getNick()).thenReturn("nick");
        when(dataBaseService.getUDSByLP("login", "password")).thenReturn(user);
        msg = new MsgGetUserId(from, to,  "1a", "login", "password");
    }

    @Test
    public void execTest() throws SQLException{
        //msg.exec(dataBaseService);
        //verify(dataBaseService).getUDSByLP("login", "password");
    }
}

