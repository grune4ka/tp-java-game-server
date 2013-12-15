package gameMechanicsTest;


import gameMechanics.GameMechanics;
import gameMechanics.GameSession;
import gameMechanics.GameSessionSnapshot;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GameMechanicsTest {
    private GameMechanics gameMechanics;
    private GameSession gameSessionId1;
    private GameSession gameSessionId2;
    private GameSession gameSessionId3;

    @BeforeMethod
    public void setUp(){
        gameSessionId1 = mock(GameSession.class);
        gameSessionId2 = mock(GameSession.class);
        gameSessionId3 = mock(GameSession.class);
        gameMechanics = new GameMechanics();

    }

    @Test
    public void addUserToGameTest(){
        when(gameSessionId1.haveUser(1)).thenReturn(false);
        gameMechanics.addUserToGame(1,gameSessionId1);
        Assert.assertEquals(gameMechanics.getGameSessionLast(), gameSessionId1);

        when(gameSessionId1.haveUser(2)).thenReturn(false);
        when(gameSessionId1.haveFreeSlots()).thenReturn(true);
        gameMechanics.addUserToGame(2, gameSessionId1);
        Assert.assertEquals(gameMechanics.getGameSessionLast(), gameSessionId1);

        when(gameSessionId1.haveUser(1)).thenReturn(true);
        gameMechanics.addUserToGame(1,gameSessionId1);
        Assert.assertEquals(gameMechanics.getGameSessionLast(), gameSessionId1);


        when(gameSessionId3.haveFreeSlots()).thenReturn(false);
        gameMechanics.addUserToGame(3, gameSessionId1);
        Assert.assertEquals(gameMechanics.getGameSessionLast(), gameSessionId1);

    }

    @Test
    public void updateBoardPositionTest(){
        gameMechanics.addUserToGame(1,gameSessionId1);
        when(gameSessionId1.haveUser(1)).thenReturn(true);
        gameMechanics.updateBoardPosition(1, 50);
        verify(gameSessionId1).setBoardPositionById(1,50);
    }

    @Test
    public void doActTest(){
        gameMechanics.addUserToGame(1,gameSessionId1);
        when(gameSessionId1.canRemove()).thenReturn(true);
        gameMechanics.doAct();
        Assert.assertEquals(gameMechanics.getGameSessionsSize(), 0);

        gameMechanics.addUserToGame(2,gameSessionId1);
        when(gameSessionId1.canRemove()).thenReturn(false);
        gameMechanics.doAct();
        verify(gameSessionId1).nextTick();

    }
}
