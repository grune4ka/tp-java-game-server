package messageSystem;

import java.util.HashMap;
import java.util.Iterator;

import modules.Address;

public class AddressService {
	private static HashMap<String, Address> addressMap = new HashMap<String, Address>();
	public static Address getAddressByServiceName(String name) {
		if (addressMap.containsKey(name)) {
			return addressMap.get(name);
		}
		else {
			return null;
		}
	}
	
	public static String getServiceNameByAddress(Address address) {
		if (addressMap.containsValue(address)) {
			for (Iterator<String> i = addressMap.keySet().iterator(); i.hasNext();) {
				String currentElement;
				if (addressMap.get(currentElement = i.next())== address) {
					return  currentElement;
				}
			}
		}
		return null;
	}
	
	public static void addAddressOfService(String name, Address address) {
		addressMap.put(name, address);
	}
}
