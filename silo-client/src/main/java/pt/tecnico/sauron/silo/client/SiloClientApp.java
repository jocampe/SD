package pt.tecnico.sauron.silo.client;

import java.util.Scanner;

import pt.tecnico.sauron.silo.client.SiloFrontend;
import pt.tecnico.sauron.silo.grpc.Silo.ClearRequest;
import pt.tecnico.sauron.silo.grpc.Silo.PingRequest;
import pt.tecnico.sauron.silo.grpc.Silo.PingResponse;
import pt.ulisboa.tecnico.sdis.zk.ZKNamingException;

public class SiloClientApp {
	
	public static void main(String[] args) throws ZKNamingException {
		System.out.println(SiloClientApp.class.getSimpleName());
		
		// receive and print arguments
		System.out.printf("Received %d arguments%n", args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.printf("arg[%d] = %s%n", i, args[i]);
		}
		
		
		final String zooHost = args[0];
		final String zooPort = args[1];
		final String path = args[2];
		final String host = args[3];
		final int port = Integer.parseInt(args[4]);
		String input;
		
		SiloFrontend frontend = new SiloFrontend(zooHost, zooPort, path);

		try(Scanner scanner = new Scanner(System.in)) {
			do {
				System.out.println("Select a control option (ctrl_ping; ctrl_clear; ctrl_init");
				input = scanner.next();
				switch(input) {
				    case "ctrl_ping": 
				    	PingRequest request = PingRequest.newBuilder().setText("friend").build();
					    PingResponse response = frontend.setPing(request);
					    System.out.println(response);
		                break; 
		                
		            case "ctrl_clear": 
		                frontend.setClear(ClearRequest.getDefaultInstance());
		                break; 
		                
		            case "ctrl_init": 
		                //TO_DO
		                break; 
		            default: 
		                System.out.println("no match"); 
				}
			} while(true);
		}

			
	}
}
