package helpers;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import frontend.Frontend;

public class TemplateHelper {
	private static Configuration cfg = new Configuration();
	public static void init() {
			try {
				cfg.setDirectoryForTemplateLoading(new File(System.getProperty("user.dir") + "/templates"));				
			} catch (IOException e) {
				e.printStackTrace();
				Logger logger = Logger.getLogger(Frontend.class.toString());
				logger.info("Can't find folder with templates. Shutting down...");
				System.exit(404);
			}
			
	}
	
	public static Template getTemplate(String name) {
		Template template = null;
		try {
			template = cfg.getTemplate(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return template;
	}
	
	public static void renderTemplate(String name, Writer out) {
		try {
			Template template = cfg.getTemplate(name);
			template.process(null, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public static void renderTemplate(String name, Map map_arg, Writer out) {
		Template template;
		try {
			template = cfg.getTemplate(name);
			Map map = new HashMap();
			map.putAll(map_arg);
			template.process(map, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void renderTemplate(String name, String userName, Writer out) {
		Template template;
		try {
			template = cfg.getTemplate(name);
			Map<String, String> map = new  HashMap<String, String>();
			map.put("userName", userName);
			template.process(map, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void renderTemplate(String name, String userName, Map map_arg, Writer out) {
		Template template;
		try {
			template = cfg.getTemplate(name);
			Map map = new  HashMap();
			map.put("userName", userName);
			map.putAll(map_arg);
			template.process(map, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
