package cgb.transfer.exception;

public class InvalidIbanFormatException extends InvalidIbanException{

	public InvalidIbanFormatException() {
		super("IBAN Format is Invalid");
	}
	
}
