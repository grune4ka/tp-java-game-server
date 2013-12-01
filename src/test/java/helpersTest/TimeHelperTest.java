package helpersTest;


import helpers.TimeHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TimeHelperTest {
    private TimeHelper time = new TimeHelper();

    @Test
    public void getTimeTest(){
        Assert.assertNotNull(time.getGMT());  //вообще-то нужно проверить формат, а не просто пусто/непусто
    }
}
