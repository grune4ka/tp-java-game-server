package gameMechanicsTest;

import gameMechanics.GameSessionSnapshot;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;

public class GameSessionSnapshotTest {
    private GameSessionSnapshot gameSessionSnapshotActive;
    private GameSessionSnapshot gameSessionSnapshotFinish;
    private HashMap<String, String> map;

    @BeforeMethod
    public void setUp(){
        gameSessionSnapshotFinish = new GameSessionSnapshot(1,5,50,2,4,100,30,30,true,true);
        gameSessionSnapshotActive = new GameSessionSnapshot(1,5,50,2,4,100,30,30,true,false);

        map = new HashMap<String, String>();
    }

    @Test
    public void getHashMapTest(){
        map.put("results", "true");
        map.put("yourPoints", "5");
        map.put("enemyPoints","4");
        Assert.assertEquals(gameSessionSnapshotFinish.getHashMapByUserId(1), map);
        map.clear();
        map.put("results", "true");
        map.put("yourPoints", "4");
        map.put("enemyPoints","5");
        Assert.assertEquals(gameSessionSnapshotFinish.getHashMapByUserId(2), map);
        map.clear();
        map.put("results", "false");
        map.put("enemyPosition", "100");
        this.map.put("yourPoints", "5");
        this.map.put("enemyPoints", "4");
        this.map.put("ballXPos","30");
        this.map.put("ballYPos", "30");
        Assert.assertEquals(gameSessionSnapshotActive.getHashMapByUserId(1), map);
        map.clear();
        map.put("results", "false");
        map.put("enemyPosition", "50");
        this.map.put("yourPoints", "4");
        this.map.put("enemyPoints", "5");
        this.map.put("ballXPos","30");
        this.map.put("ballYPos", "450");
        Assert.assertEquals(gameSessionSnapshotActive.getHashMapByUserId(2), map);



    }
}
