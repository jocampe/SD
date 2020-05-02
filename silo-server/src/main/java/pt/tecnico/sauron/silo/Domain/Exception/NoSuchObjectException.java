package pt.tecnico.sauron.silo.Domain.Exception;

public class NoSuchObjectException extends Exception{

	public NoSuchObjectException() {
		super("Object does not exist"); 
	}
}
