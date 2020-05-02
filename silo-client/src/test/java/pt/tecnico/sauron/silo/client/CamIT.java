/*package pt.tecnico.sauron.silo.client;

import pt.tecnico.sauron.silo.grpc.Silo.*;
import pt.ulisboa.tecnico.sdis.zk.ZKNamingException;

import org.junit.jupiter.api.*;

import io.grpc.StatusRuntimeException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.tecnico.sauron.silo.grpc.Silo.*;
import io.grpc.StatusRuntimeException;

import pt.tecnico.sauron.silo.grpc.Silo.*;

import static io.grpc.Status.INVALID_ARGUMENT;

public class CamIT {
	
	
	@BeforeAll
    public static void oneTimeSetUp() { }

    @AfterAll
    public static void oneTimeTearDown() { }

    
    // members

    private SiloFrontend frontend;


    // initialization and clean-up for each test

    @BeforeEach
    public void setUp() throws ZKNamingException{
    	frontend = new SiloFrontend("localhost", "2181", "1");
    	

    }

    @AfterEach
    public void tearDown() {
        frontend = null;
    }
	
	
	
	@Test
    public void CamCreationTest() {
        CamJoinRequest request = CamJoinRequest.newBuilder().setName("Tagus").setCoordinates(CoordinatesGrpc.newBuilder().setLat(37.1232).setLon(-8.9841).build()).build();
        CamJoinResponse dcresponse = frontend.camJoin(request);
        CamInfoResponse response2  = frontend.camInfo(CamInfoRequest.newBuilder().setName("Tagus").build());
        assertEquals(37.1232, response2.getCoordinates().getLat());
        assertEquals(-8.9841, response2.getCoordinates().getLon());
    }
	
	@Test
	public void camShortIdTest() {
        final CamJoinRequest request = CamJoinRequest.newBuilder().setName("Ta").setCoordinates(CoordinatesGrpc.newBuilder().setLat(37.1232).setLon(-8.9841).build()).build();
        CamJoinResponse dcresponse = frontend.camJoin(request);
        assertEquals(
                INVALID_ARGUMENT.getCode(), assertThrows(
                StatusRuntimeException.class, () -> frontend.camJoin(request)).getStatus().getCode());
        }
    
	
	@Test
	public void camLongIdTest() {
        final CamJoinRequest request = CamJoinRequest.newBuilder().setName("InstitutoSuperiorTecnico").setCoordinates(CoordinatesGrpc.newBuilder().setLat(37.1232).setLon(-8.9841).build()).build();
        CamJoinResponse dcresponse = frontend.camJoin(request);
        assertEquals(
         INVALID_ARGUMENT.getCode(),
		 assertThrows(
                StatusRuntimeException.class, () -> frontend.camJoin(request)).getStatus().getCode());
    }
	
	
	

}*/
