package pt.tecnico.sauron.eye;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import com.google.protobuf.Timestamp;

import io.grpc.StatusRuntimeException;
import io.grpc.Status;
import pt.tecnico.sauron.silo.client.SiloFrontend;

import pt.tecnico.sauron.silo.grpc.Silo.*;

public class EyeApp {
	private static final String SLEEP_CMD = "zzz";
	private static final String CMT_CMD = "#";
	private static final String FLUSH_CMD = "";
	private static List<ObservationGrpc> _obsList = new ArrayList<>();
	private static List<Integer> prev;	

	public static void main(String[] args) {
		System.out.println(EyeApp.class.getSimpleName());
		
		// receive and print arguments
		System.out.printf("Received %d arguments%n", args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.printf("arg[%d] = %s%n", i, args[i]);
		}
		
		// check arguments. Must be either 5 or 6.
				if (args.length < 4) {
					System.out.println("Argument(s) missing!");
					System.out.printf("Usage: java %s host port%n", EyeApp.class.getName());
					return;
				}
		
		final String zkhost = args[0];
		final String zkport = args[1];
		final String name = args[2];
		final double latitude = Double.parseDouble(args[3]);
		final double longitude = Double.parseDouble(args[4]);
		final String replica;
		if (args.length == 6)
			replica = args[5]; 
		else
			replica = "0";

		SiloFrontend frontend = new SiloFrontend(zkhost, zkport, replica);
		prev = new ArrayList<>(Arrays.asList(new Integer[frontend.getRepCount()]));
		Collections.fill(prev, 0);
		
		//cam_join -> login da camera
		try {
			CamJoinRequest request = CamJoinRequest.newBuilder()
					.setName(name)
					.setCoordinates(CoordinatesGrpc.newBuilder()
							.setLat(latitude)
							.setLon(longitude)
							.build())
					.addAllPrev(prev)
					.build();
			CamJoinResponse response = frontend.camJoin(request);
			prev = response.getNewList();

		}
		catch (StatusRuntimeException e) {
			System.out.println("Caught Exception with description" + e.getStatus().getDescription());
		}
		
		
		try(Scanner scanner = new Scanner(System.in)) {
			  // Check if camera doesn't exist frontend.getCamera(name);
			  while(true) {
			    try {
			      String line = scanner.nextLine();
			      String[] array = line.split(",", 2);
			      //Comment
			      if(array[0].indexOf("#") != -1) 
			        continue;
			      //Make camera sleep for time milliseconds
			      else if(SLEEP_CMD.equals(array[0])) {
			        
			        int time = Integer.parseInt(array[1]);
			        try {
			            Thread.sleep(time);
			        } catch(InterruptedException e) {
			            System.out.println("got interrupted for " + (float)time/1000 + "seconds");
			        }
			        continue;	
			      }
			      
			      //report -> envia observacoes para o servidor
			      else if (FLUSH_CMD.equals(array[0])) {
			    	  try {
			    	  ReportRequest rRequest = ReportRequest.newBuilder()
			    			  .setName(name)
			    			  .addAllObservation(_obsList)
			    			  .addAllPrev(prev)
			    			  .build();
			    	  ReportResponse response = frontend.report(rRequest);
			    	  //update prev
			    	  prev = response.getNewList();
			    	  _obsList.clear();
			    	  }
			    	  catch (StatusRuntimeException e) {
			    	        Status status = e.getStatus();
			    	        System.out.println(status.getDescription());
			    	   }
			    	  
			          continue;
			      }
			      
			      //guarda a observacao sem timestamp (e calculado apenas no server)
			      else {
			        ObservationGrpc obs = ObservationGrpc.newBuilder()
			        		.setType(array[0])
			        		.setId(array[1])
			        		.setCamera(name)
			        		.build();
			        _obsList.add(obs);
			        continue;
			      }
			        
			    }
			    catch (StatusRuntimeException e) {
					System.out.println(e.getStatus().getDescription());
			    	}	
			    finally {
			        
			        //Remove Camera from server list
			    }
			  }
		}
		
	}
}
	

