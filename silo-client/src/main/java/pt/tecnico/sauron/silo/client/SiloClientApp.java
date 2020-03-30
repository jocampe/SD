package pt.tecnico.sauron.silo.client;

import pt.tecnico.sauron.silo.client.SiloFrontend;
import pt.tecnico.sauron.silo.grpc.Silo.PingRequest;
import pt.tecnico.sauron.silo.grpc.Silo.PingResponse;

public class SiloClientApp {
	
	public static void main(String[] args) {
		System.out.println(SiloClientApp.class.getSimpleName());
		
		// receive and print arguments
		System.out.printf("Received %d arguments%n", args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.printf("arg[%d] = %s%n", i, args[i]);
		}
		
		final String host = args[0];
		final int port = Integer.parseInt(args[1]);
	
		SiloFrontend frontend = new SiloFrontend(host, port);

	    PingRequest request = PingRequest.newBuilder().setText("friend").build();
	    PingResponse response = frontend.setPing(request);
	    System.out.println(response);
			
	}
}
