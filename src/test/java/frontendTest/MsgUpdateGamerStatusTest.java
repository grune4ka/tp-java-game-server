package frontendTest;

import frontend.Frontend;
import frontend.MsgUpdateGamersStatus;
import gameMechanics.GameSessionSnapshot;
import modules.Address;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MsgUpdateGamerStatusTest {
    private MsgUpdateGamersStatus msg;
    private Frontend frontend;
    private Address from;
    private Address to;
    private GameSessionSnapshot gameSession;
    private GameSessionSnapshot[] snapshots;

    @BeforeMethod
    public void setUp(){
        from = mock(Address.class);
        to = mock(Address.class);
        frontend = mock(Frontend.class);
        gameSession = mock(GameSessionSnapshot.class);
        snapshots = new GameSessionSnapshot[1];
        snapshots[0] = gameSession;

        msg = new MsgUpdateGamersStatus(from, to,  snapshots);
    }

        @Test
        public void execTest(){
            msg.exec(frontend);
            verify(frontend).updateGameSessionSnapshots(snapshots);
        }

    }
