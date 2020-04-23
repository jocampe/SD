package pt.tecnico.sauron.spotter;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import io.grpc.StatusRuntimeException;
import pt.tecnico.sauron.silo.client.SiloFrontend;
import pt.tecnico.sauron.silo.grpc.Silo.*;
import pt.ulisboa.tecnico.sdis.zk.ZKNamingException;
import pt.ulisboa.tecnico.sdis.zk.ZKRecord;

//import pt.tecnico.sauron.silo.grpc.Silo.G
import com.google.protobuf.Timestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;



public class SpotterApp {
	private static final String SPOT_CMD = "spot";
	private static final String TRAIL_CMD = "trail";
	private static final String HELP_CMD = "help";
	
	public static void main(String[] args) throws ZKNamingException {
		System.out.println(SpotterApp.class.getSimpleName());
		
		// receive and print arguments
		System.out.printf("Received %d arguments%n", args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.printf("arg[%d] = %s%n", i, args[i]);
		}
		
		//check arguments
		if(args.length < 3) {
			System.out.println("Argument(s) missing!");
			System.out.printf("Usage: java %s host port%n",
					SpotterApp.class.getName());
			return;
		}
		final String zkhost = args[0];
		final String zkport = args[1];
		final String replica;
		if (args.length == 3)
			replica = args[2];
		else
			replica = "0";
		
		SiloFrontend frontend = new SiloFrontend(zkhost, zkport, replica);
		
		try (Scanner scanner = new Scanner(System.in)){
			while (true) {
				try {
					String line = scanner.nextLine();
					String[] arrOfStr = line.split(" "); 
					
					if (HELP_CMD.equals(arrOfStr[0])) {System.out.println("Available commands: spot, trail;");}
					
					else if (SPOT_CMD.equals(arrOfStr[0])) {
						//verificar se tem *
						//Comando Spot(trackmatch)
						if(arrOfStr[2].indexOf("*") != -1) {
							TrackMatchRequest tmRequest = TrackMatchRequest.newBuilder()
									.setType(arrOfStr[1])
									.setId(arrOfStr[2])
									.build();
							TrackMatchResponse response = frontend.trackMatch(tmRequest);
							CamInfoResponse camInfoResponse;
							int size = response.getObservationCount();
							for(int i=0; i<size; i++) {
								//cam_info para ir buscar as coordenadas
								camInfoResponse = frontend.camInfo(CamInfoRequest.newBuilder().setName(response.getObservation(i).getCamera()).build());
								LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(response.getObservation(i).getTime().getSeconds()), ZoneId.systemDefault());
								System.out.println(
						/*type*/		arrOfStr[1] + "," + 
						/*Id*/			response.getObservation(i).getId() + "," + 
						/*time*/		localDateTime + "," +
						/*cam*/			response.getObservation(i).getCamera() + "," + 
						/*lat*/			camInfoResponse.getCoordinates().getLat() + "," + 
						/*lon*/			camInfoResponse.getCoordinates().getLon());
							}
						}
						//Comando Spot (track)
						else {
							TrackRequest tRequest = TrackRequest.newBuilder()
									.setType(arrOfStr[1])
									.setId(arrOfStr[2])
									.build();
							TrackResponse response = frontend.track(tRequest);
							CamInfoResponse camInfoResponse;
							camInfoResponse = frontend.camInfo(CamInfoRequest.newBuilder().setName(response.getObservation().getCamera()).build());
							LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(response.getObservation().getTime().getSeconds()), ZoneId.systemDefault());
							System.out.println(
									arrOfStr[1] + "," + 
									response.getObservation().getId() + "," + 
									localDateTime + "," +
									response.getObservation().getCamera() + "," +
									camInfoResponse.getCoordinates().getLat() + "," + 
									camInfoResponse.getCoordinates().getLon());
						}
					}
					//Comando trail (trace)
					if (TRAIL_CMD.equals(arrOfStr[0])) {
						TraceResponse response = frontend.trace(TraceRequest.newBuilder().setType(arrOfStr[1]).setId(arrOfStr[2]).build());
						CamInfoResponse camInfoResponse;
						int size = response.getObservationCount();
						for(int i=0; i<size; i++) {
							camInfoResponse = frontend.camInfo(CamInfoRequest.newBuilder().setName(response.getObservation(i).getCamera()).build());
							LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(response.getObservation(i).getTime().getSeconds()), ZoneId.systemDefault());
							System.out.println(
									arrOfStr[1] + "," + 
									response.getObservation(i).getId() + "," + 
									localDateTime + "," +
									response.getObservation(i).getCamera() + "," +
									camInfoResponse.getCoordinates().getLat() + "," + 
									camInfoResponse.getCoordinates().getLon());
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
