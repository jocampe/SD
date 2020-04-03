package pt.tecnico.sauron.silo.Domain.Exception;

public class NoSuchCameraException extends Exception{
	
	public NoSuchCameraException() {
		super("Camera does not exist");
	}
}
