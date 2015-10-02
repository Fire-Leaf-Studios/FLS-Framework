package fls.engine.main.net.packet;

import fls.engine.main.net.Client;
import fls.engine.main.net.Server;

public class MessagePacket extends Packet{

	private String message;
	public MessagePacket(String msg) {
		super(03);
		this.message = msg;
	}

	@Override
	public void handleOnServer(Server s) {
		
	}

	@Override
	public void handleOnClient(Client c) {
		
	}

	@Override
	public byte[] getData() {
		return ("03"+this.message).getBytes();
	}

}
