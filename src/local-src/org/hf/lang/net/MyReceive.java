package org.hf.lang.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class MyReceive {

	public static void main(String[] args) throws Exception	{

		//1,创建socket，建立端口号（必须与发送端的发送的端口号一致才能接收）。
		DatagramSocket ds = new DatagramSocket(10000);
		
		int i = 0;

		while(++i < 100)		{

			//2,定义数据包。用于存储数据。
			byte[] buff = new byte[1024];
			DatagramPacket dp = new DatagramPacket(buff, buff.length);

			//3，通过服务的receive方法将收到数据存入数据包中。
			ds.receive(dp);

			//4，通过数据包的方法获取其中的数据。
			String ip = dp.getAddress().getHostAddress();

			String data = new String(dp.getData(),0, dp.getLength());

			int port = dp.getPort();

			System.out.println(data +":" + ip +":" + port);
		}

		//5，关闭资源
		ds.close();

	}
}
