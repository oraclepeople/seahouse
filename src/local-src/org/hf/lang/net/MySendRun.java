package org.hf.lang.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MySendRun implements Runnable{

	private DatagramSocket ds;

	public  MySendRun(DatagramSocket ds)	{
		this.ds = ds;
	}

	public void run()	{
		try		{
			//使用缓冲字符流储存键盘录入数据
			BufferedReader bufr = new BufferedReader(new InputStreamReader(System.in));

			String line = null;

			while((line = bufr.readLine()) != null)			{

				byte[] buf = line.getBytes();

				//每次键盘输入一行就发送一行数据
				DatagramPacket dp = new DatagramPacket(buf,buf.length,InetAddress.getByName("127.0.0.1"),10000);

				ds.send(dp);
				//输入over!结束通信
				if("over!".equals(line))
					break;
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("发送端失败");
		}
	}

}
