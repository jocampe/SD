package pt.tecnico.sauron.silo;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import pt.ulisboa.tecnico.sdis.zk.ZKNaming;
<<<<<<< HEAD

=======
import pt.ulisboa.tecnico.sdis.zk.ZKNamingException;
>>>>>>> 53c02dd17fc5c9971e04780fc9d2a194ecaa0f79

import java.io.IOException;
import java.util.Scanner;


public class SiloServerApp {
	
<<<<<<< HEAD
	public static void main(String[] args) throws Exception{
=======
	public static void main(String[] args) throws IOException, InterruptedException, ZKNamingException {
>>>>>>> 53c02dd17fc5c9971e04780fc9d2a194ecaa0f79
		System.out.println(SiloServerApp.class.getSimpleName());
		
		// receive and print arguments
		System.out.printf("Received %d arguments%n", args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.printf("arg[%d] = %s%n", i, args[i]);
		}
		
		// check arguments
		if (args.length < 5) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s path zooHost zooPort host port%n", Server.class.getName());
			return;
		}
<<<<<<< HEAD
		
		final String zooHost = args[0];
		final String zooPort = args[1];
		final String path = args[2];
		final String host = args[3];
		final String port = args[4];
		final BindableService impl = new ServerImpl();
		ZKNaming zkNaming = null;
		try {
		zkNaming = new ZKNaming(zooHost, zooPort);
		// publish
		zkNaming.rebind(path, host, port);
		// Create a new server to listen on port
		Server server = ServerBuilder.forPort(Integer.parseInt(port)).addService(impl).build();
=======

		final String zooHost = args[0];
		final String zooPort = args[1];
		final String host = args[2];
		final String port = args[3];
		final String path = args[4];
		final BindableService impl = new ServerImpl();
		ZKNaming zkNaming = null;
>>>>>>> 53c02dd17fc5c9971e04780fc9d2a194ecaa0f79

		try {

			zkNaming = new ZKNaming(zooHost, zooPort);
			// publish
			zkNaming.rebind(path, host, port);
		
			// Create a new server to listen on port
			Server server = ServerBuilder.forPort(Integer.parseInt(args[3])).addService(impl).build();
	
			// Start the server
			server.start();
	
			// Server threads are running in the background.
			System.out.println("Server started");
			
			System.out.println("<Press enter to shutdown>");
			new Scanner(System.in).nextLine();

			server.shutdown();

<<<<<<< HEAD
		// Do not exit the main thread. Wait until server is terminated.
		server.awaitTermination();
		}
		finally  {
		    if (zkNaming != null) {
		        // remove
		        zkNaming.unbind(path,host,port);
		    }
=======
			
		} finally {
			if (zkNaming != null) {
				// remove
				zkNaming.unbind(path, host, port);
			}
>>>>>>> 53c02dd17fc5c9971e04780fc9d2a194ecaa0f79
		}
	}
	
}
