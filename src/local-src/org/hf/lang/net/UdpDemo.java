package org.hf.lang.net;

import java.net.DatagramSocket;

public class UdpDemo {

	public static void main(String[] args) throws Exception		{

		//启动发送端socket服务
		DatagramSocket sendSocket = new DatagramSocket();
		
		//启动接收端socket服务
		DatagramSocket receSocket = new DatagramSocket(10000);

		//启动发送端线程
		new Thread(new MySendRun(sendSocket)).start();
		
		//启动接收端线程
		new Thread(new MyReceiveRun(receSocket)).start();
	}
}
