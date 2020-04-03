package pt.tecnico.sauron.spotter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import io.grpc.StatusRuntimeException;
import pt.tecnico.sauron.silo.client.SiloFrontend;
import pt.tecnico.sauron.silo.grpc.Silo.*;
//import pt.tecnico.sauron.silo.grpc.Silo.G
import com.google.protobuf.Timestamp;




public class SpotterApp {
	private static final String SPOT_CMD = "spot";
	private static final String TRAIL_CMD = "trail";
	private static final String HELP_CMD = "help";
	
	public static void main(String[] args) {
		System.out.println(SpotterApp.class.getSimpleName());
		
		// receive and print arguments
		System.out.printf("Received %d arguments%n", args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.printf("arg[%d] = %s%n", i, args[i]);
		}
		
		//check arguments
		if(args.length < 1) {
			System.out.println("Argument(s) missing!");
			System.out.printf("Usage: java %s host port%n",
					SpotterApp.class.getName());
			return;
		}
		final String host = args[0];
		final int port = Integer.parseInt(args[1]);

		try (SiloFrontend frontend = new SiloFrontend(host, port); 
				Scanner scanner = new Scanner(System.in)){
			while (true) {
				try {
					String line = scanner.nextLine();
					String[] arrOfStr = line.split(" ", 2); 
					
					if (HELP_CMD.equals(arrOfStr[0])) {System.out.println("Available commands: spot, trail;");}
					
					if (SPOT_CMD.equals(arrOfStr[0])) {System.out.println("hey, u got spotted");}
					
					if (TRAIL_CMD.equals(arrOfStr[0])) {
						String input[] = arrOfStr[1].split(" ");
						TrackResponse getResponse = frontend.getTrack(TrackRequest.newBuilder().setType(input[1]).setId(input[0]).build());
						String id = getResponse.getObservation().getId();
						Timestamp timestamp = getResponse.getObservation().getTime();
						long data = timestamp.getSeconds();
						SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
						Date date = new Date(data);
						System.out.println(formatter.format(date));
					
					}
					
				 }catch (StatusRuntimeException e) {System.out.println(e.getStatus().getDescription());}
			}
		}finally {System.out.println("> Closing");}
	}

}
