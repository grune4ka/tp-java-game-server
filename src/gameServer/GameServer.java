package gameServer;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;

import dataBaseService.DataBaseService;
import frontend.Frontend;
import gameMechanics.GameMechanics;
import helpers.TemplateHelper;

public class GameServer {
	public static void main(String[] arg) throws Exception {
		Frontend frontend = new Frontend();
		GameMechanics gameMechanics = new GameMechanics();
		DataBaseService dataBaseService = new DataBaseService();
			
		Thread frontendThread = new Thread (frontend);
		Thread gameMechanicsThread = new Thread (gameMechanics);
		Thread dataBaseServiceThread = new Thread (dataBaseService);
		
		
		frontendThread.start();
		gameMechanicsThread.start();
		dataBaseServiceThread.start();
		
		Server server = new Server(8080);
		
		SelectChannelConnector connector = new SelectChannelConnector();
		server.addConnector(connector);
		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setDirectoriesListed(true);
		
		resource_handler.setResourceBase("static");
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { frontend, resource_handler});
		server.setHandler(handlers);
		
		TemplateHelper.init();
		
		server.start();
		server.join();
	}
}
