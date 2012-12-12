package resources;

public class ResourceFactory {
	private ResourceFactory factory;
	
	private ResourceFactory() {
		
	}
	
	public ResourceFactory instanse() {
		if (this.factory == null) {
			this.factory = new ResourceFactory();
		}
		return this.factory;
	}
}
