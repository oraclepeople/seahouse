package org.hf.lang.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class MyReceiveRun implements Runnable {

	private DatagramSocket ds;

	public MyReceiveRun(DatagramSocket ds)	{
		this.ds = ds;
	}

	public void run()	{
		try {
			while(true)
			{
				//定义缓冲区
				byte[] buf = new byte[1024];
				DatagramPacket dp = new DatagramPacket(buf,buf.length);

				//接收的数据存储到缓冲区
				ds.receive(dp);

				//取到数据中需要的信息
				String ip = dp.getAddress().getHostAddress();

				String data = new String(dp.getData(),0,dp.getLength());

				if("over!".equals(data))
				{
					System.out.println(ip+"....离开");
					break;
				}

				System.out.println(ip + ":" + data);
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("接收端失败");
		}
	}
}
