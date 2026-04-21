package cgb.transfer.exception;

public class NegativeTransferAmountException extends Exception{
	
	public  NegativeTransferAmountException() {
		// TODO Auto-generated constructor stub
		super("Montant du stransfert ne peut pas etre negatif");
	}
	
}
