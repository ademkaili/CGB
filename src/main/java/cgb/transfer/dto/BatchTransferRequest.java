package cgb.transfer.dto;


import java.time.LocalDate;
import java.util.List;



public class BatchTransferRequest {

	private String refLot;
	     
	private String sourceAccountNumber;
	   
	private String description;

	private List<TransferRequest> listTransfers;

	public String getRefLot() {
	    return refLot;}
	public void setRefLot(String refLot) {
	    this.refLot = refLot;}
	public String getSourceAccountNumber() {
	    return sourceAccountNumber;}
	public void setSourceAccountNumber(String sourceAccountNumber) {
	    this.sourceAccountNumber = sourceAccountNumber;}
	public String getDescription() {
	    return description;}
	public void setDescription(String description) {
	    this.description = description;}
	public List<TransferRequest> getListTransfers() {
	    return listTransfers;}
	public void setListTransfer(List<TransferRequest> listTransfers) {
	    this.listTransfers = listTransfers;}

    
}
