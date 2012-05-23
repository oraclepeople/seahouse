package org.hf.lang.net;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/** Copied from Java doc ExecutorService */

class NetworkService implements Runnable {
	
	private final ServerSocket serverSocket;
	private final ExecutorService pool;

	public NetworkService(int port, int poolSize)
	throws IOException {
		serverSocket = new ServerSocket(port);
		pool = Executors.newFixedThreadPool(poolSize);
	}

	public void run() { // run the service
		try {
			for (;;) {
				Handler handler = new Handler(serverSocket.accept());
				pool.execute(handler);
			}
		} catch (IOException ex) {
			pool.shutdown();
		}
	}



	void shutdownAndAwaitTermination(ExecutorService pool) {
		pool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!pool.awaitTermination(60, TimeUnit.SECONDS))
					System.err.println("Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}

	public static void main(String[] args) throws IOException {
		ExecutorService es = Executors.newCachedThreadPool();
		NetworkService networkService = new NetworkService(8000, 1);
		es.submit(networkService);
	}
}

class Handler implements Runnable {
	
	private final Socket socket;

	Handler(Socket socket) {
		this.socket = socket; 
	}

	public void run() {
		// read and service request on socket
		try {

			InetAddress client = socket.getInetAddress();
			System.out.println(client.getHostName() + " connected to server.\n");
//			BufferedReader input =  new BufferedReader(new InputStreamReader(socket. getInputStream()));

			OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
			out.write("HTTP/1.0 200 OK\r\n");
			Date now = new Date(  );
			out.write("Date: " + now + "\r\n");
			out.write("Server: JHTTP 1.0\r\n");
			//            out.write("Content-length: " + 20 + "\r\n");
			out.write("Content-type: " + "text/html" + "\r\n\r\n");
			out.flush();

			out.write("<HTML>\r\n");
			out.write("<HEAD><TITLE>Response</TITLE>\r\n");
			out.write("</HEAD>\r\n");
			out.write("<BODY>");
			out.write("<H1>This is my response at " + System.currentTimeMillis() +  " </h2>\r\n");
			out.write("</BODY></HTML>\r\n");
//			out.write("no idea");
			out.flush();

			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
