package cgb.transfer.exception;

public class InvalidUnCheckableIbanException extends InvalidIbanException{

	public InvalidUnCheckableIbanException() {
		super("IBAN is uncheckable");
	}
	
}
