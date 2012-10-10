package com.virtualwindow.jason;

import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.virtualwindow.media.StreamingMediaPlayer;

public class VirtualWindowActivity extends Activity {

	private Button streamButton;

	private ImageButton playButton;

	private TextView textStreamed;

	private boolean isPlaying;

	private StreamingMediaPlayer audioStreamer;

	/**
	 * Called when the activity is first created.
	 */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
	}

	//Returns an arraylist contains 255.255.255.255 and the broadcast IP of current subnet
	public ArrayList<InetAddress> getBroadcastAddresses()
			throws UnknownHostException {
		ArrayList<InetAddress> inets = new ArrayList<InetAddress>();

		InetAddress lastBroadcast = InetAddress.getByName("255.255.255.255");
		inets.add(lastBroadcast);
		WifiManager wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		int broadcast = dhcp.ipAddress | (~dhcp.netmask);
		byte[] broadbytes = new byte[4];
		for (int i = 0; i < 4; i++) {
			broadbytes[i] = (byte) (0xff & broadcast >> i * 8);
		}
		InetAddress broad = InetAddress.getByAddress(broadbytes);
		inets.add(broad);
		Log.e("z", "broadcast: " + broad.toString());
		return inets;
	}

	public void becomeClient() {
		String helloServer = "Hello, this is from your Android phone =)";
		byte[] buffer = helloServer.getBytes();

		try {
			DatagramSocket socket = new DatagramSocket();
			socket.setBroadcast(true);
			List<InetAddress> l = getBroadcastAddresses();
			for (InetAddress inet : l) {
				Log.e("z", "ip: " + inet.toString());
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inet, 9999);
				socket.send(packet);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

}