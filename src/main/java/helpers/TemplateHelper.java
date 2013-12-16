package helpers;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TemplateHelper {

    private static Configuration cfg;
    private Template template;

    public TemplateHelper(Configuration cfg){
        this.cfg = cfg;
        this.template = null;
    }
    public static void init() {
			try {
				cfg.setDirectoryForTemplateLoading(new File(System.getProperty("user.dir") + "/templates"));				
			} catch (IOException e) {
				System.exit(404);
			}
			
	}
	
	public boolean renderTemplate(String name, Writer out) {
        try {
            template = cfg.getTemplate(name);
            template.process(null, out);
            return true;
        } catch (Exception e){
            return false;
        }

    }

    public boolean renderTemplate(String name, Map map_arg, Writer out) {
        try {
            template = cfg.getTemplate(name);
            template.process(map_arg, out);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean renderTemplate(String name, String userName, Writer out) {
        try {
            template = cfg.getTemplate(name);
            Map<String, String> map = new  HashMap<String, String>();
            map.put("userName", userName);
            template.process(map, out);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean renderTemplate(String name, String userName, Map map_arg, Writer out) {
        try {
            template = cfg.getTemplate(name);
            Map map = new  HashMap();
            map.put("userName", userName);
            map.putAll(map_arg);
            template.process(map, out);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
