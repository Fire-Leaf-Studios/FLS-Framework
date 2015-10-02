package fls.engine.main.net.packet;

import fls.engine.main.net.Client;
import fls.engine.main.net.Server;

public class LogInPacket extends Packet {

	private String username;
	private int x,y;
	
	public LogInPacket(byte[] data){
		super(00);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.x = Integer.parseInt(dataArray[1]);
		this.y = Integer.parseInt(dataArray[2]);
	}
	
	public LogInPacket(String username,int x,int y){
		super(00);
		this.username = username;
		this.x = x;
		this.y = y;
	}

	@Override
	public void handleOnServer(Server s) {
		
	}

	@Override
	public void handleOnClient(Client c) {
		
	}

	@Override
	public byte[] getData() {
		return ("00"+this.username+","+this.x+","+this.y).getBytes();
	}
}
