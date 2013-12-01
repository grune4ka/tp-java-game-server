package gameServer;

import helpers.VFS;

import java.io.Serializable;

import resources.Resource;

import com.thoughtworks.xstream.XStream;

public class GameSettings implements Serializable, Resource {
	private static final long serialVersionUID = 1L;
	public int winCounts;
	public int widthGameField;
	public int heigthGameField;
	public int failWait;
	public int[] vector;
	
	public GameSettings(int failWait, int winCounts, int widthGameField, int heigthGameField, int[] vector) {
		this.failWait = failWait;
		this.winCounts = winCounts;
		this.widthGameField = widthGameField;
		this.heigthGameField = heigthGameField;
		this.vector = vector;
	}
    //тест
	/*public static void test (int a, int b, int c, int d, int[] e) {
		GameSettings r = new GameSettings(a, b, c, d, e);
		XStream xstream = new XStream();
		String xml = xstream.toXML(r);
		VFS.writeToFile(xml, "settings/game_settings.xml");
	}*/
	
}
