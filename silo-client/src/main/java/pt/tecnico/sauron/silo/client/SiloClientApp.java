package pt.tecnico.sauron.silo.client;

import java.util.Scanner;

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
		String input;
		
		SiloFrontend frontend = new SiloFrontend(host, port);

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
		                //TO_DO
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
