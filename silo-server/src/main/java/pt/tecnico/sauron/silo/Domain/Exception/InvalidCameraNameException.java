package pt.tecnico.sauron.silo.Domain.Exception;

public class InvalidCameraNameException extends Exception{
	
	public InvalidCameraNameException() {
		super("Camera name must be alphanumeric between 3 and 15 characters long");
	}
}
