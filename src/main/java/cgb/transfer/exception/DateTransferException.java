package cgb.transfer.exception;

import cgb.transfer.exception.DeleteTransferException.FailureTransfert;

public class DateTransferException extends Exception{
	
	public  DateTransferException() {
		// TODO Auto-generated constructor stub
		super("Date anterieur a aujourd'hui");
	}
}
