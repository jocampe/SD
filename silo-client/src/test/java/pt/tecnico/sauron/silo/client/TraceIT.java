/*package pt.tecnico.sauron.silo.client;

import pt.tecnico.sauron.silo.grpc.Silo.*;
import org.junit.jupiter.api.*;

import io.grpc.StatusRuntimeException;

import static io.grpc.Status.INVALID_ARGUMENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

public class TraceIT {
	
	
	@BeforeAll
    public static void oneTimeSetUp() { }

    @AfterAll
    public static void oneTimeTearDown() { }

    
    // members

    private SiloFrontend frontend;
    //List<ObservationGrpc> _obsList = new ArrayList<>();
    //private ObservationGrpc obs;
    //private TraceRequest tRequest;
    //private TraceResponse tResponse;
	


    // initialization and clean-up for each test

    @BeforeEach
    public void setUp() {
        frontend = new SiloFrontend("localhost", "2181", "1"); 
      //ObservationGrpc obs = ObservationGrpc.newBuilder().setType("person").setId("123").setCamera("Tagus").build();

    }

    @AfterEach
    public void tearDown() {
        frontend = null;
        //obs = null;
        
    }
    
    @Test
    public void traceTest() {
		List<ObservationGrpc> _obsList = new ArrayList<>();
		ObservationGrpc obs = ObservationGrpc.newBuilder().setType("person").setId("123").setCamera("Tagus").build();
        _obsList.add(obs);
        TraceRequest tRequest = TraceRequest.newBuilder().setType("person").setId("123").build();
        TraceResponse tResponse = frontend.trace(tRequest);
        assertEquals(_obsList, tResponse.getObservationList());
    }
    
    @Test
    public void wrongTypeTest() {
		List<ObservationGrpc> _obsList = new ArrayList<>();
		ObservationGrpc obs = ObservationGrpc.newBuilder().setType("person").setId("123").setCamera("Tagus").build();
        _obsList.add(obs);
        TraceRequest tRequest = TraceRequest.newBuilder().setType("car").setId("123").build();
        TraceResponse tResponse = frontend.trace(tRequest);
        assertEquals(
                INVALID_ARGUMENT.getCode(),
                assertThrows(
                        StatusRuntimeException.class, () -> frontend.trace(tRequest))
                        .getStatus()
                        .getCode());
    }
    
    @Test
    public void wrongIdTest() {
		List<ObservationGrpc> _obsList = new ArrayList<>();
		ObservationGrpc obs = ObservationGrpc.newBuilder().setType("person").setId("123").setCamera("Tagus").build();
        _obsList.add(obs);
        TraceRequest tRequest = TraceRequest.newBuilder().setType("person").setId("345").build();
        TraceResponse tResponse = frontend.trace(tRequest);
        assertEquals(
                INVALID_ARGUMENT.getCode(),
                assertThrows(
                        StatusRuntimeException.class, () -> frontend.trace(tRequest))
                        .getStatus()
                        .getCode());
    }
    
	
	
}*/
