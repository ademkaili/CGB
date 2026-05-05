package cgb.transfer.dto;


import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BatchTransferRequest {
	     
	private String sourceAccountNumber;
	   
	private String description;

	private List<TransferRequest> listTransfers;

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
