package dataBaseServiceTest;


import dataBaseService.DataBaseService;
import dataBaseService.Executor;
import dataBaseService.ResultHandler;
import dataBaseService.users.UserDataSet;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.security.spec.ECField;
import java.sql.*;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DataBaseServiceTest {
    private DataBaseService dataBaseService;
    private ResultSet result;
    private UserDataSet userDataSet;
    private UserDataSet user;
    private Connection conn;
    private Connection connNeg;
    private Statement st;
    private Statement stNeg;
    private ResultHandler rs;
    private ResultHandler rsNeg;
    private ResultSet resultNegative;

    private Executor executor;

    @BeforeMethod
    public void setUp() throws SQLException{
        dataBaseService = new DataBaseService();
        userDataSet = new UserDataSet(1,"login", "password", "nick");
        user = new UserDataSet(-3, "", "", "");
        executor = mock(Executor.class);

        result = mock(ResultSet.class);
        conn = mock(Connection.class);

        when(result.next()).thenReturn(true);
        when(result.getInt("id")).thenReturn(1);
        when(result.getString("login")).thenReturn("login");
        when(result.getString("password")).thenReturn("password");
        when(result.getString("nick")).thenReturn("nick");

        st = mock(Statement.class);
        rs = mock(ResultHandler.class);

        when(conn.createStatement()).thenReturn(st);
        when(st.getUpdateCount()).thenReturn(1);
        when(st.getResultSet()).thenReturn(result);
        when(rs.handle(result)).thenReturn("String");

        resultNegative = mock(ResultSet.class);
        connNeg = mock(Connection.class);
        when(resultNegative.next()).thenReturn(false);
        stNeg = mock(Statement.class);
        rsNeg = mock(ResultHandler.class);
        when(connNeg.createStatement()).thenReturn(st);
        when(stNeg.getUpdateCount()).thenReturn(1);
        when(stNeg.getResultSet()).thenReturn(result);
        when(rsNeg.handle(result)).thenReturn("String");
    }

    @Test
    public void getUDSByLPTest() throws  SQLException, NullPointerException{
        dataBaseService.setExecutor(executor);
        dataBaseService.setConnection(conn);
        dataBaseService.setUser(userDataSet);
        Assert.assertSame(dataBaseService.getUDSByLP("login", "password"), userDataSet);

        dataBaseService.setConnection(connNeg);
        dataBaseService.setUser(user);
        Assert.assertSame(dataBaseService.getUDSByLP("login", "password"), user);
    }

    @Test
    public void addUDSTest() throws SQLException{
        dataBaseService.setExecutor(executor);
        dataBaseService.setConnection(conn);
//        verify(executor).execUpdate(conn, anyString());
        verify(executor).execUpdate(conn, org.mockito.Mockito.eq("insert"));
    }

}
