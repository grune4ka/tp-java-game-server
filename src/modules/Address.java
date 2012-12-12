package modules;
import java.util.concurrent.atomic.AtomicInteger;

import messageSystem.AddressService;

public class Address {
	static private AtomicInteger abonentIdCreator = new AtomicInteger();
	final private int abonentId;

	public Address(String nameService){
		this.abonentId = abonentIdCreator.incrementAndGet();
		AddressService.addAddressOfService(nameService, this);
	}

	public int getAbonentId() {
		return abonentId;
	}
}

