package gameServerTest;


import gameServer.GameSettings;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Vector;

public class GameSettingsTest {

    @Test
    public void gameSettingsTest(){
        int[] v = new int[2];
        v[0]=4;
        v[1]=4;

        GameSettings gameSettings = new GameSettings(1000, 5, 360, 480, v);
        Assert.assertEquals(gameSettings.failWait, 1000);
        Assert.assertEquals(gameSettings.winCounts, 5);
        Assert.assertEquals(gameSettings.widthGameField, 360);
        Assert.assertEquals(gameSettings.heigthGameField, 480);

    }
}
