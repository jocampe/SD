/*package pt.tecnico.sauron.silo.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pt.tecnico.sauron.silo.grpc.Silo.CamInfoRequest;
import pt.tecnico.sauron.silo.grpc.Silo.CamInfoResponse;
import pt.tecnico.sauron.silo.grpc.Silo.CamJoinRequest;
import pt.tecnico.sauron.silo.grpc.Silo.CamJoinResponse;
import pt.tecnico.sauron.silo.grpc.Silo.CoordinatesGrpc;
import pt.tecnico.sauron.silo.grpc.Silo.ObservationGrpc;
import pt.tecnico.sauron.silo.grpc.Silo.ReportRequest;
import pt.tecnico.sauron.silo.grpc.Silo.TrackRequest;
import pt.tecnico.sauron.silo.grpc.Silo.TrackResponse;

public class TrackIT {
	@BeforeAll
    public static void oneTimeSetUp() { }

    @AfterAll
    public static void oneTimeTearDown() { }

    
    // members

    private SiloFrontend frontend;


    // initialization and clean-up for each test

    @BeforeEach
    public void setUp() {
        //frontend = new SiloFrontend("localhost", 8080);
    }

    @AfterEach
    public void tearDown() {
        frontend = null;
    }
	
	
	
	@Test
    public void TrackTest() {
		List<ObservationGrpc> _obsList = new ArrayList<>();
		ObservationGrpc obs = ObservationGrpc.newBuilder().setType("person").setId("123").setCamera("Tagus").build();
        _obsList.add(obs);
        CamJoinRequest request = CamJoinRequest.newBuilder().setName("Tagus").setCoordinates(CoordinatesGrpc.newBuilder().setLat(37.1232).setLon(-8.9841).build()).build();
        frontend.camJoin(request);
        CamInfoResponse response2  = frontend.camInfo(CamInfoRequest.newBuilder().setName("Tagus").build());
        frontend.report(ReportRequest.newBuilder().setName("Tagus").addAllObservation(_obsList).build());
        TrackRequest trkrequest = TrackRequest.newBuilder().setId("123").setType("person").build();
        
        TrackResponse trkresponse = frontend.track(trkrequest);
        CamInfoResponse camresponse  = frontend.camInfo(CamInfoRequest.newBuilder().setName("Tagus").build());
        assertEquals("person", trkresponse.getObservation().getType());
        assertEquals("123", trkresponse.getObservation().getId());
        assertEquals("Tagus", trkresponse.getObservation().getCamera());
        assertEquals(37.1232, camresponse.getCoordinates().getLat());
        assertEquals(-8.9841, camresponse.getCoordinates().getLon());

    }

}
*/