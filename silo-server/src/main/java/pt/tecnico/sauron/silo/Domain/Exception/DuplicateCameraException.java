package pt.tecnico.sauron.silo.Domain.Exception;

public class DuplicateCameraException extends Exception{
	
	public DuplicateCameraException() {
		super("Camera does not exist");
	}
}