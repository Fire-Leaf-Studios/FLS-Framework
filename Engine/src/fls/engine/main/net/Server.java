package fls.engine.main.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

public class Server extends NetObject {
	
	private final String err = "Couldn't start a server... sorry";
	
	public HashMap<InetAddress,String> connections;
	
	public Server(int port){
		try {
			socket = new DatagramSocket(port);
			this.port = port;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(err);
		}
	}
	
	@Override
	public void run(){
		System.out.println("Server started on port: "+port);
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		while(true){
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String msg = new String(packet.getData()).trim();
			System.out.println(msg);
			if(msg.indexOf("ping") != -1){
				sendData("Pong",packet.getAddress(),packet.getPort());
			}
			packetHand.parseClientData(this,packet.getData(), packet.getAddress(), packet.getPort());
		}
	}
	
	public void sendData(String msg,InetAddress adrs,int port){
		byte[] data = msg.getBytes();
		DatagramPacket packet = new DatagramPacket(data,data.length,adrs,port);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getPort(){
		return socket.getLocalPort();
	}
	
	public String getAddress(){
		return (""+socket.getLocalAddress()).split("/")[1];
	}
	
	public void printInfo(){
		System.out.println("[Server] Address: "+getAddress()+", Port: "+getPort());
	}
	
	@Override
	public Server setPacketHandler(PacketHandler p){
		this.packetHand = p;
		return this;
	}
	
	

}
