package dataBaseServiceTest;

import dataBaseService.users.UserDataSet;
import gameMechanics.Gamer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UserDataSetTest {
    private UserDataSet user;

    @BeforeMethod
    public void setUp(){
        user = new UserDataSet(1, "login", "password", "nick");

    }

    @Test
    public void test(){
        Assert.assertEquals(user.getId(),1);
        Assert.assertEquals(user.getLogin(), "login");
        Assert.assertEquals(user.getPassword(), "password");
        Assert.assertEquals(user.getNick(), "nick");
    }
}


