package gameMechanicsTest;

import gameMechanics.GameSession;
import gameMechanics.GameSessionSnapshot;
import gameMechanics.Gamer;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class GameSessionTest {
    private GameSession gameSession;

    @BeforeMethod
    public void setUp(){
        this.gameSession = new GameSession();

    }

    @Test
    public void canRemoveTest(){
        Assert.assertEquals(gameSession.canRemove(), false);
        //вообще-то нужен еще нужен тест на проверку true, но значение  finishTime меняется только в методе isTheGameEnd
    }

    @Test
    public void isWaitTest(){
        Assert.assertEquals(gameSession.isWait(),false); //когда не выполняется вторая часть условия
        gameSession.setWaiter();
        try {
            Thread.sleep(2000);
            Assert.assertEquals(gameSession.isWait(),false); //когда не выполняется первая часть условия
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameSession.setWaiter();
        Assert.assertEquals(gameSession.isWait(), true); //когда условие полностью выполняется

    }
    @Test
    public void addGamerTest(){
        Assert.assertEquals(gameSession.addGamer(1), true);
        Assert.assertEquals(gameSession.addGamer(2), true);
        Assert.assertEquals(gameSession.addGamer(3),false);

    }

    @Test
    public void haveFreeSlotsTest(){
        Assert.assertEquals(gameSession.haveFreeSlots(),true); //свободны оба
        gameSession.addGamer(1);
        Assert.assertEquals(gameSession.haveFreeSlots(),true); //свободен один
        gameSession.addGamer(2);
        Assert.assertEquals(gameSession.haveFreeSlots(),false); //все занято

    }

    @Test
    public void haveUserTest(){
        Assert.assertEquals(gameSession.haveUser(1), false);
        Assert.assertEquals(gameSession.haveUser(2), false);
        gameSession.addGamer(1);
        gameSession.addGamer(2);
        Assert.assertEquals(gameSession.haveUser(1), true);
        Assert.assertEquals(gameSession.haveUser(2), true);

    }
    private Gamer gamer1;
    private Gamer gamer2;

    @Test
    public void isGameEndTest(){
        Assert.assertEquals(gameSession.isGameEnd(),false);
        gameSession.addGamer(1);
        Assert.assertEquals(gameSession.isGameEnd(),false);
        gameSession.addGamer(2);

        //надо через функцию nextTick()
        //Assert.assertEquals(gameSession.isGameEnd(),true);
    }

    @Test
    public void getGameSessionSnapshotTest() throws Exception{
        gameSession.addGamer(1);
        gameSession.addGamer(2);
        GameSessionSnapshot snapshot = gameSession.getGameSessionSnapshot();
        Assert.assertEquals(snapshot.getIdGamer1(), 1);
        Assert.assertEquals(snapshot.getIdGamer2(), 2);
    }

    //void-функции

    @Test
    public void setBoardPositionByIdTest(){

    }

    @Test
    public void nextTickTest(){
        gameSession.addGamer(1);
        gameSession.addGamer(2);
    }
}
