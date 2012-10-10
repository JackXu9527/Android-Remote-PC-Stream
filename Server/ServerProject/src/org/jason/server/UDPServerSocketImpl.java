package org.jason.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.jmdns.impl.DNSEntry;
import javax.jmdns.impl.JmDNSImpl;

import org.jason.network.model.Crypto;
import org.jason.network.model.ServerSocket;

public class UDPServerSocketImpl extends Thread implements ServerSocket {
	private static final String INADDR_ANY = "0.0.0.0";
	private static final int PORT = 9999;
	private DatagramSocket serverSocket;
	
	
	public UDPServerSocketImpl() {
		this.start();
	}
	
	@Override
	public void setupSocket() throws SocketException, UnknownHostException{
		serverSocket = new DatagramSocket(PORT, InetAddress.getByName(INADDR_ANY));
		serverSocket.setBroadcast(true);
	}
	
	@Override
	public void run(){
		try {
			//buffer to contain data from received packet
			byte[] receivedBuffer = new byte[128];
			
			setupSocket();
			
			System.out.println(InetAddress.getLocalHost().getHostName() + " is now ---> LISTENING" );
			DatagramPacket packet = new DatagramPacket(receivedBuffer, receivedBuffer.length);
			serverSocket.receive(packet);
			
			System.out.println("Unknown client has replied! --> IP = " + packet.getSocketAddress() + " | PORT = " + packet.getPort());
			String clientMessage = new String(receivedBuffer, "UTF-8");
			System.out.println("Client message [ " + clientMessage + " ]");
			
			/*if(Crypto.decrypt(clientMessage)){
				//Do something
			}*/
			
			byte[] sendingBuffer = " has replied your request!".getBytes();
			sendData(sendingBuffer);
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void sendData(byte[] buffer) throws IOException {
		// TODO Auto-generated method stub
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(INADDR_ANY), PORT);
		serverSocket.send(packet);
	}

	@Override
	public void killThread() {
		// TODO Auto-generated method stub
		System.out.println("Destroyed the thread");
	}

}
