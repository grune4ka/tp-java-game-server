package helpersTest;


import freemarker.template.Configuration;
import helpers.TemplateHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.Writer;
import java.io.File;
import java.util.Map;

import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;

public class TemplateHelperTest {
    private TemplateHelper template;
    private Map map_arg;
    private Configuration cfg;
    private Writer writer;

    @BeforeClass
    public void setUp(){
        cfg = new Configuration();
        template = new TemplateHelper(cfg);
        writer = mock(Writer.class);
        map_arg = mock(Map.class);

    }

    @Test
    public void initTest(){
        //что-то типа file.getPath();
        template.init();
    }
    @Test
    public void renderTemplateTest(){
        TemplateHelper.renderTemplate("positiveTestNull.html", writer );
        //Assert.assertEquals(new File("/positiveTestNull.html").exists(), true);

        TemplateHelper.renderTemplate("positiveTest.html", map_arg, writer);
        //Assert.assertEquals(new File("/positiveTest.html").exists(), true);

        TemplateHelper.renderTemplate("positiveUserTest.html","user", writer);
        TemplateHelper.renderTemplate("positiveUserMapTest.html","user",map_arg, writer);
    }
}
