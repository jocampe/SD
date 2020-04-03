package pt.tecnico.sauron.silo.client;

import pt.tecnico.sauron.silo.grpc.Silo.*;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class CamIT {
	
	
	@BeforeAll
    public static void oneTimeSetUp() { }

    @AfterAll
    public static void oneTimeTearDown() { }

    
    // members

    private SiloFrontend frontend;


    // initialization and clean-up for each test

    @BeforeEach
    public void setUp() {
        frontend = new SiloFrontend("localhost", 8080);
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

}
