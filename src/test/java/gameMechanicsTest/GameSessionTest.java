package gameMechanicsTest;

import gameMechanics.GameSession;
import gameMechanics.GameSessionSnapshot;
import gameMechanics.Gamer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class GameSessionTest {
    private GameSession gameSession;
    private Gamer gamer1;
    private Gamer gamer2;

    @BeforeMethod
    public void setUp(){
        this.gameSession = new GameSession();
        gamer1 = mock(Gamer.class);
        gamer2 = mock(Gamer.class);


        when(gamer1.getId()).thenReturn(1);
        when(gamer2.getId()).thenReturn(2);
    }

    @Test
    public void canRemoveTest(){
        Assert.assertEquals(gameSession.canRemove(), false);
        gameSession.setFinishTime(1);
        Assert.assertTrue(gameSession.canRemove());

    }

    @Test
    public void isWaitTest(){
        long timer = 10;
        long timerFail = 1020;
        Assert.assertEquals(gameSession.isWait(timer),false); //когда не выполняется вторая часть условия

        gameSession.setWaiter(timer);

        Assert.assertEquals(gameSession.isWait(timerFail),false); //когда не выполняется первая часть условия
        gameSession.setWaiter(timer);
        Assert.assertEquals(gameSession.isWait(timer), true); //когда условие полностью выполняется

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

    @Test
    public void isGameEndTest(){
        Assert.assertEquals(gameSession.isGameEnd(),false);
        gameSession.setGamer1(gamer1);
        Assert.assertEquals(gameSession.isGameEnd(),false);
        gameSession.setGamer2(gamer2);

        when(gamer1.getPoints()).thenReturn(5);
        when(gamer2.getPoints()).thenReturn(5);
        Assert.assertEquals(gameSession.isGameEnd(), true);
        when(gamer1.getPoints()).thenReturn(1);
        when(gamer2.getPoints()).thenReturn(2);
        Assert.assertEquals(gameSession.isGameEnd(), false);

    }

    @Test
    public void getGameSessionSnapshotTest() throws Exception{
        gameSession.setGamer1(gamer1);
        gameSession.setGamer2(gamer2);
        when(gamer1.getPoints()).thenReturn(1);
        when(gamer1.getId()).thenReturn(1);
        when(gamer1.getBoardPosition()).thenReturn(50);
        when(gamer2.getPoints()).thenReturn(2);
        when(gamer2.getId()).thenReturn(2);
        when(gamer2.getBoardPosition()).thenReturn(100);

        GameSessionSnapshot snapshot = gameSession.getGameSessionSnapshot();
        Assert.assertEquals(snapshot.getIdGamer1(), 1);
        Assert.assertEquals(snapshot.getPointsGamer1(), 1);
        Assert.assertEquals(snapshot.getPositionGamer1(),50);

        Assert.assertEquals(snapshot.getIdGamer2(), 2);
        Assert.assertEquals(snapshot.getPointsGamer2(), 2);
        Assert.assertEquals(snapshot.getPositionGamer2(),100);
    }


    @Test
    public void nextTickTest(){
        when(gamer1.getId()).thenReturn(1);
        when(gamer1.getBoardPosition()).thenReturn(50);

        when(gamer2.getId()).thenReturn(2);
        when(gamer2.getBoardPosition()).thenReturn(50);
        //!haveFreeSlots
        gameSession.setGamer1(gamer1);
        gameSession.setGamer2(gamer2);

        //!isGameEnd
        when(gamer1.getPoints()).thenReturn(1);
        when(gamer2.getPoints()).thenReturn(1);
        //!isWait
        gameSession.setBallXPosition(-10);
        gameSession.setBallYPosition(10);

        long timer = 10;
        long timerFail = 1020;
        gameSession.nextTick(timer);

        Assert.assertEquals(gameSession.getBallXPosition(), 100);
        Assert.assertEquals(gameSession.getBallYPosition(), 320);
        //один набор можно оставить для покрытия ветки else
        gameSession.setBallXPosition(40);
        gameSession.setBallYPosition(20);

        gameSession.nextTick(timer);
        Assert.assertEquals(gameSession.getBallXPosition(), 40);
        Assert.assertEquals(gameSession.getBallYPosition(), 20);

        gameSession.nextTick(timerFail);
        Assert.assertEquals(gameSession.getBallXPosition(), 36);
        Assert.assertEquals(gameSession.getBallYPosition(), 24);

        gameSession.setBallXPosition(204);
        gameSession.setBallYPosition(574);

        gameSession.nextTick(timerFail);
        Assert.assertEquals(gameSession.getBallXPosition(), 100);
        Assert.assertEquals(gameSession.getBallYPosition(), 320);

        gameSession.setBallXPosition(40);
        gameSession.setBallYPosition(574);

        gameSession.setWaiter(timer);
        gameSession.nextTick(timerFail);
        Assert.assertEquals(gameSession.getBallXPosition(), 44);
        Assert.assertEquals(gameSession.getBallYPosition(), 570);
    }
}
