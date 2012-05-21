package org.hf.lang.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MySend {
	public static void main(String[] args) throws Exception	{

		//1，通过DatagramSocket对象创建udp服务
		DatagramSocket ds = new DatagramSocket();

		//2,提供数据,并将数据封装到数据包中。并提供了发送地址为我的本机地址，端口好为10000.
		byte[] data= "hello 太平洋! ".getBytes();

		DatagramPacket dp = new DatagramPacket(data, data.length, InetAddress.getByName("127.0.0.1"),10000);

		//3，通过socket服务，将已有的数据包发送出去。通过send方法。
		ds.send(dp);

		//4，关闭资源。
		ds.close();
	}
}
