package resources;

import java.io.File;
import java.util.HashMap;

import com.thoughtworks.xstream.XStream;


public class ResourceFactory {
	private HashMap<String, Resource> cache;
	private static ResourceFactory factory;
	private	XStream xstream;
	private ResourceFactory() {
		this.xstream = new XStream();
		this.cache = new HashMap<String, Resource>();
	}
	
	public static ResourceFactory instanse() {
		if (factory == null) {
			factory = new ResourceFactory();
		}
		return factory;
	}
	
	public Resource getResource(String path) {
		if (cache.containsKey(path)) {
			return cache.get(path);
		}
		else {
			Resource buffer = (Resource) xstream.fromXML(new File(path));
			cache.put(path, buffer);
			return buffer;
		}
	}
}
