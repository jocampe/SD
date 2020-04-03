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
					String[] arrOfStr = line.split(" "); 
					
					if (HELP_CMD.equals(arrOfStr[0])) {System.out.println("Available commands: spot, trail;");}
					
					else if (SPOT_CMD.equals(arrOfStr[0])) {
						//verificar se tem *
						if(arrOfStr[2].indexOf("*") != -1) {
							TrackMatchRequest tmRequest = TrackMatchRequest.newBuilder()
									.setType(arrOfStr[1])
									.setId(arrOfStr[2])
									.build();
							TrackMatchResponse response = frontend.trackMatch(tmRequest);
							int size = response.getObservationCount();
							for(int i=0; i<size; i++) {
								System.out.println(
										arrOfStr[1] + "," + 
										response.getObservation(i).getId() + "," + 
										response.getObservation(i).getTime() + "," 
										/*E PRECISO A CAMERA AQUI*/
/*ou se acrescenta cam no proto e usa se o cam_info para ir sacar coord, ou outra coisa qq*/);
							}
						}
						else {
							TrackRequest tRequest = TrackRequest.newBuilder()
									.setType(arrOfStr[1])
									.setId(arrOfStr[2])
									.build();
							TrackResponse response = frontend.track(tRequest);
							System.out.println(
									arrOfStr[1] + "," + 
									response.getObservation().getId() + "," + 
									response.getObservation().getTime() + "," 
									/*a mesma coisa para este*/);
						}
					}
					
					if (TRAIL_CMD.equals(arrOfStr[0])) {
						TraceResponse response = frontend.trace(TraceRequest.newBuilder().setType(arrOfStr[1]).setId(arrOfStr[2]).build());
						
						int size = response.getObservationCount();
						for(int i=0; i<size; i++) {
							System.out.println(
									arrOfStr[1] + "," + 
									response.getObservation(i).getId() + "," + 
									response.getObservation(i).getTime() + "," 
									/*E PRECISO A CAMERA AQUI*/
/*ou se acrescenta cam no proto e usa se o cam_info para ir sacar coord, ou outra coisa qq*/);
						}
						/*
						String id = getResponse.getObservation().getId();
						Timestamp timestamp = getResponse.getObservation().getTime();
						long data = timestamp.getSeconds();
						SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
						Date date = new Date(data);
						System.out.println(formatter.format(date));
					  */
					}
					
				 }catch (StatusRuntimeException e) {System.out.println(e.getStatus().getDescription());}
			}
		}finally {System.out.println("> Closing");}
	}

}
