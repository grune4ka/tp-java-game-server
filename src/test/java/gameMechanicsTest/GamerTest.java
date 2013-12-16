package gameMechanicsTest;

import gameMechanics.Gamer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GamerTest {
    private Gamer gamer;

    @BeforeMethod
    public void setUp(){
        gamer = new Gamer(1);
    }

    @Test
    public void test(){
        gamer.setId(2);
        gamer.setBoardPosition(10);
        gamer.setPoints(20);
        Assert.assertEquals(gamer.getId(), 2);
        Assert.assertEquals(gamer.getBoardPosition(),10);
        Assert.assertEquals(gamer.getPoints(), 20);
    }
}
