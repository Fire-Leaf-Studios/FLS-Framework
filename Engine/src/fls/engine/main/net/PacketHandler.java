package fls.engine.main.net;

import java.net.InetAddress;

import fls.engine.main.net.packet.LogInPacket;
import fls.engine.main.net.packet.Packet;
import fls.engine.main.net.packet.Packet.PacketType;


public abstract class PacketHandler {
	
	public final void parseClientData(Server s,byte[] data,InetAddress adrs, int port){
		String message = new String(data).trim();
		PacketType type = getPacketType(data);
		Packet packet = null;
		
		switch(type){
		default:
		case INVALID:
			break;
		case LOGIN:
			packet = new LogInPacket(data);
			System.out.println(((LogInPacket)packet).packetID);
			break;
		case MESSAGE:
			System.out.println(message);
			break;
		}
		
		clientDataProcess(s, data, adrs, port);
	}

	public final void parseServerData(Client c,byte[] data, InetAddress adrs, int port){
		
	}
	
	public abstract void clientDataProcess(Server s,byte[] data,InetAddress adrs,int port);
	
	public abstract void serverDataProcess(Server s,byte[] data,InetAddress adrs,int port);
	
	public PacketType getPacketType(byte[] data){
		return Packet.getPacket(new String(data).trim().substring(0,2));
	}
}
