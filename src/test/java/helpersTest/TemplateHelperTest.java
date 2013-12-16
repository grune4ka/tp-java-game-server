package helpersTest;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import helpers.TemplateHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TemplateHelperTest {
    private TemplateHelper templateHelper;
    private Template template;

    private Map map_arg;
    private Configuration cfg;
    private Writer writer;

    private String name = "/join.html";
    private String userName = "user";

    @BeforeClass
    public void setUp() throws IOException{
        cfg = mock(Configuration.class);
        templateHelper = new TemplateHelper(cfg);
        writer = mock(Writer.class);
        map_arg = mock(Map.class);
        template = mock(Template.class);

        when(cfg.getTemplate(name)).thenReturn(template);

    }

    @Test
    public void renderTemplateTest() throws IOException, TemplateException{

        Assert.assertTrue(templateHelper.renderTemplate(name, writer));
        verify(template).process(null, writer);

        Map map = new HashMap();
        Assert.assertTrue(templateHelper.renderTemplate(name, map, writer));
        verify(template).process(map, writer);

        Assert.assertTrue(templateHelper.renderTemplate(name, userName, writer));

        Assert.assertTrue(templateHelper.renderTemplate(name, userName, map, writer));

    }
}
