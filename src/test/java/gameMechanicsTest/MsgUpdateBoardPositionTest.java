package gameMechanicsTest;


import frontend.Frontend;
import gameMechanics.GameMechanics;
import gameMechanics.MsgUpdateBoardPosition;
import modules.Address;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MsgUpdateBoardPositionTest {
    private MsgUpdateBoardPosition msg;
    private Frontend frontend;
    private Address from;
    private Address to;
    private GameMechanics gameMechanics;

    @BeforeMethod
    public void setUp(){
        from = mock(Address.class);
        to = mock(Address.class);
        frontend = mock(Frontend.class);
        gameMechanics = mock(GameMechanics.class);
        msg = new MsgUpdateBoardPosition(from, to, 1, 10);
    }

    @Test
    public void execTest(){
        msg.exec(gameMechanics);
        verify(gameMechanics).updateBoardPosition(1,10);
    }

}
