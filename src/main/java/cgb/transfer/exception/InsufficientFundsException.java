package cgb.transfer.exception;

import cgb.transfer.exception.DeleteTransferException.FailureTransfert;

public class InsufficientFundsException extends Exception {
	

	public  InsufficientFundsException() {
		// TODO Auto-generated constructor stub
		super("Fonds insuffisant");
	}

}
