package pt.tecnico.sauron.spotter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import io.grpc.StatusRuntimeException;
import io.grpc.Status;
import pt.tecnico.sauron.silo.client.SiloFrontend;
import pt.tecnico.sauron.silo.grpc.Silo.*;
//import pt.tecnico.sauron.silo.grpc.Silo.G

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;



public class SpotterApp {
	private static final String SPOT_CMD = "spot";
	private static final String TRAIL_CMD = "trail";
	private static final String HELP_CMD = "help";
	private static List<Integer> prev;
	
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
		

		final String zooHost = args[0];
		final String zooPort = args[1];
		final String path = args[2];
		SiloFrontend frontend = new SiloFrontend(zooHost, zooPort, path);
		prev = new ArrayList<>(Arrays.asList(new Integer[frontend.getRepCount()]));
		Collections.fill(prev, 0);
		
		try (Scanner scanner = new Scanner(System.in)){
			while (true) {
				try {
					String line = scanner.nextLine();
					String[] arrOfStr = line.split(" "); 
					//Help command
					if (HELP_CMD.equals(arrOfStr[0])) {System.out.println("Available commands: spot, trail;");}
					//Spot command
					else if (SPOT_CMD.equals(arrOfStr[0])) {
						
						//verificar se tem *
						//Comando Spot(trackmatch)
						if(arrOfStr[2].indexOf("*") != -1) {
							try {
								TrackMatchRequest tmRequest = TrackMatchRequest.newBuilder()
										.setType(arrOfStr[1])
										.setId(arrOfStr[2])
										.addAllPrev(prev)
										.build();
								TrackMatchResponse response = frontend.trackMatch(tmRequest);
								prev = response.getNewList();
								CamInfoResponse camInfoResponse;
								int size = response.getObservationCount();
								for(int i=0; i<size; i++) {
									//cam_info para ir buscar as coordenadas
									camInfoResponse = frontend.camInfo(CamInfoRequest.newBuilder().setName(response.getObservation(i).getCamera()).addAllPrev(prev).build());
									prev = response.getNewList();
									LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(response.getObservation(i).getTime().getSeconds()), ZoneId.systemDefault());
									System.out.println(
							/*type*/		arrOfStr[1] + "," + 
							/*Id*/			response.getObservation(i).getId() + "," + 
							/*time*/		localDateTime + "," +
							/*cam*/			response.getObservation(i).getCamera() + "," + 
							/*lat*/			camInfoResponse.getCoordinates().getLat() + "," + 
							/*lon*/			camInfoResponse.getCoordinates().getLon());
								}
						  }		catch (StatusRuntimeException e) {
				    	        Status status = e.getStatus();
				    	        System.out.println(status.getDescription());
				    	   		}
						}
						//Comando Spot (track)
						else {
							try {
							TrackRequest tRequest = TrackRequest.newBuilder()
									.setType(arrOfStr[1])
									.setId(arrOfStr[2])
									.addAllPrev(prev)
									.build();
							TrackResponse response = frontend.track(tRequest);
							prev = response.getNewList();
							CamInfoResponse camInfoResponse;
							camInfoResponse = frontend.camInfo(CamInfoRequest.newBuilder().setName(response.getObservation().getCamera()).addAllPrev(prev).build());
							prev = camInfoResponse.getNewList();
							LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(response.getObservation().getTime().getSeconds()), ZoneId.systemDefault());
							System.out.println(
									arrOfStr[1] + "," + 
									response.getObservation().getId() + "," + 
									localDateTime + "," +
									response.getObservation().getCamera() + "," +
									camInfoResponse.getCoordinates().getLat() + "," + 
									camInfoResponse.getCoordinates().getLon());
							}	catch (StatusRuntimeException e) {
				    	        Status status = e.getStatus();
				    	        System.out.println(status.getDescription());
				    	   }
							
						}
					}

					//Comando trail (trace)
					if (TRAIL_CMD.equals(arrOfStr[0])) {
						try {
							TraceResponse response = frontend.trace(TraceRequest.newBuilder()
									.setType(arrOfStr[1])
									.setId(arrOfStr[2])
									.addAllPrev(prev)
									.build());
							prev = response.getNewList();
							CamInfoResponse camInfoResponse;
							int size = response.getObservationCount();
							for(int i=0; i<size; i++) {
								camInfoResponse = frontend.camInfo(CamInfoRequest.newBuilder().setName(response.getObservation(i).getCamera()).addAllPrev(prev).build());
								prev = camInfoResponse.getNewList();
								LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(response.getObservation(i).getTime().getSeconds()), ZoneId.systemDefault());
								System.out.println(
										arrOfStr[1] + "," + 
										response.getObservation(i).getId() + "," + 
										localDateTime + "," +
										response.getObservation(i).getCamera() + "," +
										camInfoResponse.getCoordinates().getLat() + "," + 
										camInfoResponse.getCoordinates().getLon());

						}
						} catch (StatusRuntimeException e) {
			    	        Status status = e.getStatus();
			    	        System.out.println(status.getDescription());
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
