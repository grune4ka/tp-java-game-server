package helpersTest;


import helpers.VFS;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;

public class VFSTest {
    private VFS vfs = new VFS();

    @Test
    public void isExistTest(){
        Assert.assertEquals(vfs.isExist("/"), true);
    }

    @Test
    public void isDirectory()throws FileNotFoundException{
        Assert.assertEquals(vfs.isDirectory("/"), true);
    }

    @Test
    public void getBytesTest() throws Exception{
        Assert.assertNull(vfs.getBytes("/"));
        Assert.assertNotNull(vfs.getBytes("/settings/game_settings.xml"));
    }

    @Test
    public void writeToToFileTest() throws FileNotFoundException{
        vfs.writeToFile( "<test_data></test_data>", "/test.xml");
        Assert.assertNotNull(vfs.getBytes("/settings/game_settings.xml"));
    }
}
