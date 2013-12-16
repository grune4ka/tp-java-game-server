package dataBaseServiceTest;


import dataBaseService.DataBaseService;
import dataBaseService.users.UserDataSet;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataBaseServiceTest {
    private DataBaseService dataBaseService;
    private ResultSet result;
    private UserDataSet userDataSet;

    @BeforeMethod
    public void setUp() throws SQLException{
        dataBaseService = new DataBaseService();
        userDataSet = new UserDataSet(1,"login", "password", "nick");
        result = mock(ResultSet.class);
        when(result.next()).thenReturn(true);
        when(result.getInt("id")).thenReturn(1);
        when(result.getString("login")).thenReturn("login");
        when(result.getString("password")).thenReturn("password");
        when(result.getString("nick")).thenReturn("nick");

    }

    /*@Test
    public void getUDSByLPTest() throws  SQLException, NullPointerException{
        Assert.assertEquals(dataBaseService.getUDSByLP("login", "password"), userDataSet);
    }*/
}
