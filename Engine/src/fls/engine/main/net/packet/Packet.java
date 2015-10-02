package fls.engine.main.net.packet;

import fls.engine.main.net.Client;
import fls.engine.main.net.Server;

public abstract class Packet {

	public static enum PacketType{
		
		INVALID(-1), LOGIN(00),DISCONNECT(01),MESSAGE(02);
		
		private int packetID;
		
		private PacketType(int id){
			this.packetID = id;
		}
		
		public int getID(){
			return this.packetID;
		}
	}
	
	public byte packetID;
	
	public Packet(int id){
		this.packetID = (byte)id;
	}
	
	public abstract void handleOnServer(Server s);
	
	public abstract void handleOnClient(Client c);
	
	public abstract byte[] getData();
	
	public static PacketType getPacket(int id){
		for(PacketType p : PacketType.values()){
			if(p.getID() == id)return p;
		}
		return PacketType.INVALID;
	}
	
	public String readData(byte[] data){
		String msg = new String(data).trim();
		return msg.substring(2);
	}
	
	public static PacketType getPacket(String msg){
		return getPacket(Integer.parseInt(msg));
	}
}
