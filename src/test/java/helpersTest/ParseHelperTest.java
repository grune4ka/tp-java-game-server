package helpersTest;

import helpers.ParseHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ParseHelperTest {

    private ParseHelper parseHelper;
    @Test
    public void strToIntTest() throws Exception{
        parseHelper= new ParseHelper();
        Assert.assertEquals(parseHelper.strToInt("1"),1);
        Assert.assertEquals(parseHelper.strToInt("24523234523625264536666345"),0);
    }
}
