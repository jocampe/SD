package pt.tecnico.sauron.silo;

import io.grpc.BindableService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import pt.tecnico.sauron.silo.grpc.SiloServiceGrpc;
import pt.ulisboa.tecnico.sdis.zk.ZKNaming;
import pt.ulisboa.tecnico.sdis.zk.ZKNamingException;
import pt.ulisboa.tecnico.sdis.zk.ZKRecord;
import pt.tecnico.sauron.silo.kappatask;
import pt.tecnico.sauron.silo.Domain.Operations;

import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;


public class SiloServerApp {
	
	
	
	public static void main(String[] args) throws IOException, InterruptedException, ZKNamingException {
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

		final String zooHost = args[0];
		final String zooPort = args[1];
		final String host = args[2];
		final String port = args[3];
		String path = "/grpc/sauron/silo";
		int replica = Integer.parseInt(args[4]);
		String replicaPath = path + "/" + args[4];
		Timer timer = new Timer(true);
		final BindableService impl = new ServerImpl();
		ZKNaming zkNaming = null;
		

		try {
			zkNaming = new ZKNaming(zooHost, zooPort);
			// publish
			zkNaming.rebind(replicaPath, host, port);
		
			// Create a new server to listen on port
			Server server = ServerBuilder.forPort(Integer.parseInt(port)).addService(impl).build();
			// Start the server
			server.start();
			
			// Server threads are running in the background.
			System.out.println("Server started");
			
			
			TimerTask task = new kappatask(zooHost, zooPort, replicaPath, zkNaming, replica);
			timer.schedule(task ,0 , 10000);
			
			
			
			System.out.println("<Press enter to shutdown>");
			new Scanner(System.in).nextLine();

			task.cancel();
			timer.cancel();
			timer.purge();
			server.shutdown();

			
		} finally {
			if (zkNaming != null) {
				// remove
				zkNaming.unbind(path, host, port);
			}
		}
	} 
	
}


