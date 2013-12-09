package helpersTest;


import helpers.VFS;
import org.testng.Assert;
import org.testng.annotations.Test;

public class VFSTest {
    private VFS vfs = new VFS();

    @Test
    public void isExistTest(){
        Assert.assertEquals(vfs.isExist("/"), true);
    }

    @Test
    public void isDirectory(){
        Assert.assertEquals(vfs.isDirectory("/"), true);
    }

    @Test
    public void getBytesTest() throws Exception{
        Assert.assertNull(vfs.getBytes("/"));
        Assert.assertNotNull(vfs.getBytes("/settings/resource.txt"));
    }
}
