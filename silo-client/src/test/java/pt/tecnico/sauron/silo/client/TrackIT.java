/*package pt.tecnico.sauron.silo.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.grpc.StatusRuntimeException;

import pt.tecnico.sauron.silo.grpc.Silo.CamInfoRequest;
import pt.tecnico.sauron.silo.grpc.Silo.CamInfoResponse;
import pt.tecnico.sauron.silo.grpc.Silo.CamJoinRequest;
import pt.tecnico.sauron.silo.grpc.Silo.CamJoinResponse;
import pt.tecnico.sauron.silo.grpc.Silo.CoordinatesGrpc;
import pt.tecnico.sauron.silo.grpc.Silo.ObservationGrpc;
import pt.tecnico.sauron.silo.grpc.Silo.ReportRequest;
import pt.tecnico.sauron.silo.grpc.Silo.TrackRequest;
import pt.tecnico.sauron.silo.grpc.Silo.TrackResponse;
import pt.ulisboa.tecnico.sdis.zk.ZKNamingException;
import pt.tecnico.sauron.silo.grpc.Silo.TrackMatchRequest;
import pt.tecnico.sauron.silo.grpc.Silo.TrackMatchResponse;
import pt.tecnico.sauron.silo.grpc.Silo.*;
import static io.grpc.Status.INVALID_ARGUMENT;

public class TrackIT extends BaseIT{
	@BeforeAll
    public static void oneTimeSetUp() { }

    @AfterAll
    public static void oneTimeTearDown() { }

    
    // members

    private SiloFrontend frontend;
    

    // initialization and clean-up for each test

    @BeforeEach
    public void setUp() throws ZKNamingException {
        frontend = new SiloFrontend("localhost", "2181", "1"); 
    	
    }

    @AfterEach
    public void tearDown() {
        //frontend = null;
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
	
	@Test
	public void noObjectTrackTest()  {
		List<ObservationGrpc> _obsList = new ArrayList<>();
		ObservationGrpc obs = ObservationGrpc.newBuilder().setType("person").setId("123").setCamera("Tagus").build();
        _obsList.add(obs);
        CamJoinRequest request = CamJoinRequest.newBuilder().setName("Tagus").setCoordinates(CoordinatesGrpc.newBuilder().setLat(37.1232).setLon(-8.9841).build()).build();
        frontend.camJoin(request);
        CamInfoResponse response2  = frontend.camInfo(CamInfoRequest.newBuilder().setName("Tagus").build());
        frontend.report(ReportRequest.newBuilder().setName("Tagus").addAllObservation(_obsList).build());
        
        TrackRequest trkrequest = TrackRequest.newBuilder().setId("124").setType("person").build();       
        assertEquals(INVALID_ARGUMENT.getCode(),
        		assertThrows(
                        StatusRuntimeException.class, () -> frontend.track(trkrequest))
                        .getStatus()
                        .getCode());

	}
	
	@Test
	public void wrongObjectTrackTypeTest() {
		List<ObservationGrpc> _obsList = new ArrayList<>();
		ObservationGrpc obs = ObservationGrpc.newBuilder().setType("person").setId("123").setCamera("Tagus").build();
        _obsList.add(obs);
        CamJoinRequest request = CamJoinRequest.newBuilder().setName("Tagus").setCoordinates(CoordinatesGrpc.newBuilder().setLat(37.1232).setLon(-8.9841).build()).build();
        frontend.camJoin(request);
        CamInfoResponse response2  = frontend.camInfo(CamInfoRequest.newBuilder().setName("Tagus").build());
        frontend.report(ReportRequest.newBuilder().setName("Tagus").addAllObservation(_obsList).build());
        
        TrackRequest trkrequest = TrackRequest.newBuilder().setId("123").setType("car").build();       
        assertEquals(
                INVALID_ARGUMENT.getCode(),
                assertThrows(
                        StatusRuntimeException.class, () -> frontend.track(trkrequest))
                        .getStatus()
                        .getCode());
	} 
	

	
	@Test
	public void trackMatchTest() {
		List<ObservationGrpc> _obsList = new ArrayList<>();
		ObservationGrpc obs = ObservationGrpc.newBuilder().setType("person").setId("12345678").setCamera("Tagus").build();
        _obsList.add(obs);
        CamJoinRequest request = CamJoinRequest.newBuilder().setName("Tagus").setCoordinates(CoordinatesGrpc.newBuilder().setLat(37.1232).setLon(-8.9841).build()).build();
        frontend.camJoin(request);
        CamInfoResponse response2  = frontend.camInfo(CamInfoRequest.newBuilder().setName("Tagus").build());
        frontend.report(ReportRequest.newBuilder().setName("Tagus").addAllObservation(_obsList).build());
        
        TrackMatchRequest trkmrequest = TrackMatchRequest.newBuilder().setId("123").setType("person").build();
        TrackMatchResponse trkmresponse = frontend.trackMatch(trkmrequest);
        CamInfoResponse camresponse  = frontend.camInfo(CamInfoRequest.newBuilder().setName("Tagus").build());
        assertEquals("person", trkmresponse);  //FIXME
        assertEquals("12345678", trkmresponse.getObservation().getId());  //FIXME
        assertEquals("Tagus", trkmresponse.getObservation().getCamera()); //FIXME
	}
	
	
	@Test
	public void wrongObjectTypeTrackMatchTest() {
		List<ObservationGrpc> _obsList = new ArrayList<>();
		ObservationGrpc obs = ObservationGrpc.newBuilder().setType("person").setId("12345678").setCamera("Tagus").build();
        _obsList.add(obs);
        CamJoinRequest request = CamJoinRequest.newBuilder().setName("Tagus").setCoordinates(CoordinatesGrpc.newBuilder().setLat(37.1232).setLon(-8.9841).build()).build();
        frontend.camJoin(request);
        CamInfoResponse response2  = frontend.camInfo(CamInfoRequest.newBuilder().setName("Tagus").build());
        frontend.report(ReportRequest.newBuilder().setName("Tagus").addAllObservation(_obsList).build());
        
        TrackMatchRequest trkmrequest = TrackMatchRequest.newBuilder().setId("123").setType("car").build();
        assertEquals(
                INVALID_ARGUMENT.getCode(),
                assertThrows(
                        StatusRuntimeException.class, () -> frontend.trackMatch(trkmrequest))
                        .getStatus()
                        .getCode());
	}
	
	@Test
	public void wrongObjectIdTrackMatchTest() {
		List<ObservationGrpc> _obsList = new ArrayList<>();
		ObservationGrpc obs = ObservationGrpc.newBuilder().setType("person").setId("12345678").setCamera("Tagus").build();
        _obsList.add(obs);
        CamJoinRequest request = CamJoinRequest.newBuilder().setName("Tagus").setCoordinates(CoordinatesGrpc.newBuilder().setLat(37.1232).setLon(-8.9841).build()).build();
        frontend.camJoin(request);
        CamInfoResponse response2  = frontend.camInfo(CamInfoRequest.newBuilder().setName("Tagus").build());
        frontend.report(ReportRequest.newBuilder().setName("Tagus").addAllObservation(_obsList).build());
        
        TrackMatchRequest trkmrequest = TrackMatchRequest.newBuilder().setId("9").setType("person").build();
        assertEquals(
                INVALID_ARGUMENT.getCode(),
                assertThrows(
                        StatusRuntimeException.class, () -> frontend.trackMatch(trkmrequest))
                        .getStatus()
                        .getCode());
	}

	
	

}*/
