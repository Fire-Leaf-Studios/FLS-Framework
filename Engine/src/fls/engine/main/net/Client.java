package fls.engine.main.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import fls.engine.main.net.packet.Packet;

public class Client extends NetObject {

	public InetAddress adrs;

	public Client(String address, int port) {
		try {
			socket = new DatagramSocket();
			adrs = InetAddress.getByName(address);
			this.port = port;
		} catch (SocketException e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Couldn't start a client connection... sorry");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Couldn't start a client connection... sorry");
		}
	}

	@Override
	public void run() {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		while (true) {
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			packetHand.parseServerData(this,packet.getData(),packet.getAddress(),packet.getPort());
		}
	}

	public void sendData(String msg) {
		byte[] data = msg.getBytes();
		DatagramPacket packet = new DatagramPacket(data, data.length, adrs, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendPacket(Packet p){
		DatagramPacket pak = new DatagramPacket(p.getData(), p.getData().length,adrs,port);
		try {
			socket.send(pak);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Client setPacketHandler(PacketHandler p) {
		this.packetHand = p;
		return this;
	}

}
