package fls.engine.main.net;

import java.net.DatagramSocket;

public class NetObject extends Thread{

	protected DatagramSocket socket;
	protected PacketHandler packetHand;
	protected int port;
	
	
	
	public void shutdown(){
		try {
			this.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.socket.close();
	}
	
	public NetObject setPacketHandler(PacketHandler p){
		this.packetHand = p;
		return this;
	}
}
