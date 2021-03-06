package messageSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import modules.Abonent;
import modules.Address;
import modules.Msg;


public class MessageSystem {
	
	private static Map<Address, ArrayBlockingQueue<Msg>> messages = new HashMap<Address, ArrayBlockingQueue<Msg>>();
	
	public static void sendMessage(Msg message) {
		Queue<Msg> messageQueue = messages.get(message.getTo());
		messageQueue.add(message);	
	}
	
	public static void addService (Abonent abonent) {
		messages.put(abonent.getAddress(), new ArrayBlockingQueue<Msg>(1024));
	}
	
	public static void execForAbonent(Abonent abonent) {
		ArrayBlockingQueue<Msg> messageQueue = messages.get(abonent.getAddress());
		while (!messageQueue.isEmpty()) {
			Msg message = messageQueue.poll();
			message.exec(abonent);
		}
	}
}
