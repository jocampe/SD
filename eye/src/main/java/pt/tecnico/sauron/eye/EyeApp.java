package pt.tecnico.sauron.eye;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import io.grpc.StatusRuntimeException;
import pt.tecnico.sauron.silo.client.SiloFrontend;

import pt.tecnico.sauron.silo.grpc.Silo.*;

public class EyeApp {
	private static final String SLEEP_CMD = "zzz";
	private static final String CMT_CMD = "#";
	private static final String FLUSH_CMD = "";
	private static List<ObservationGrpc> _obsList = new ArrayList<>();
	
	

	public static void main(String[] args) {
		System.out.println(EyeApp.class.getSimpleName());
		
		// receive and print arguments
		System.out.printf("Received %d arguments%n", args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.printf("arg[%d] = %s%n", i, args[i]);
		}
		
		// check arguments
		if (args.length < 4) {
			System.out.println("Argument(s) missing!");
			System.out.printf("Usage: java %s host port%n", EyeApp.class.getName());
			return;
		}
		
		final String host = args[0];
		final int port = Integer.parseInt(args[1]);
		final String target = host + ":" + port;
		final String name = args[2];
		final double latitude = Double.parseDouble(args[3]);
		final double longitude = Double.parseDouble(args[4]);
		
		try(SiloFrontend frontend = new SiloFrontend(host, port); Scanner scanner = new Scanner(System.in)) {
			  // Check if camera doesn't exist frontend.getCamera(name);
			  while(true) {
			    try {
			      String line = scanner.nextLine();
			      String[] array = line.split(",", 2);
			      //Comment
			      if(CMT_CMD.equals(array[0]))
			        continue;
			      //Make camera sleep for time milliseconds
			      else if(SLEEP_CMD.equals(array[0])) {
			        
			        int time = Integer.parseInt(array[1]);
			        //Thread.sleep(time);
			        continue;
			      }
			      
			      //add Obs to 
			      else if (FLUSH_CMD.equals(array[0])) {
			          CamInfoResponse ciResponse = frontend.camInfo(CamInfoRequest.getDefaultInstance());
			          continue;
			      }
			      else {
			        ObservationGrpc obs = ObservationGrpc.newBuilder(array[0])
			        obsList.add(obs);
			        continue;
			      }
			        
			      }
			    catch (StatusRuntimeException e) {
					System.out.println(e.getStatus().getDescription());
			    	}	
			      finally {
			        System.out.println("Connection Closed");
			        //Remove Camera from server list
			      }
			    }
			  }
		
		}
	}
	

