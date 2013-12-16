package dataBaseServiceTest;


import dataBaseService.Executor;
import dataBaseService.ResultHandler;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExecutorTest {
    private Connection conn;
    private Statement st;
    private ResultHandler rs;
    private ResultSet result;

    @BeforeMethod
    public void setUp() throws SQLException{

        conn = mock(Connection.class);
        st = mock(Statement.class);
        rs = mock(ResultHandler.class);
        result = mock(ResultSet.class);
        when(conn.createStatement()).thenReturn(st);
        when(st.getUpdateCount()).thenReturn(1);
        when(st.getResultSet()).thenReturn(result);
        when(rs.handle(result)).thenReturn("String");
    }

    @Test
    public void updateTest(){
        Executor executor = new Executor();
        Assert.assertEquals(executor.execUpdate(conn, "Vasya"), 1);
    }

    @Test
    public void execTest() throws SQLException{
        Executor executor = new Executor();
        Assert.assertEquals(executor.execQuery(conn, "String", rs), "String");
    }
}
