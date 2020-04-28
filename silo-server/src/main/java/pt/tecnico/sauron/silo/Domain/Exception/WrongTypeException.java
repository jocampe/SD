package pt.tecnico.sauron.silo.Domain.Exception;

public class WrongTypeException extends Exception{
	
	public WrongTypeException() {
		super("Object's type does not match with given type"); 
	}
}
