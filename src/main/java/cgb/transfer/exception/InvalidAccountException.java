package cgb.transfer.exception;

public class InvalidAccountException extends Exception{

	public  InvalidAccountException(String account) {
		// TODO Auto-generated constructor stub
		super("Compte " + account + " invalide");
	}
	
}
