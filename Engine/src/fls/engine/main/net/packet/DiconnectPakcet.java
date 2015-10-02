package fls.engine.main.net.packet;

import fls.engine.main.net.Client;
import fls.engine.main.net.Server;

public class DiconnectPakcet extends Packet{

	public DiconnectPakcet() {
		super(01);
	}

	@Override
	public void handleOnServer(Server s) {
		
		
	}

	@Override
	public void handleOnClient(Client c) {
		
	}

	@Override
	public byte[] getData() {
		return null;
	}

}
