package cgb.transfer.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String sourceAccountNumber;
    private String destinationAccountNumber;
    private Double amount;
    private LocalDate transferDate;
    private String description;
	private String state;
    
    // Getters and Setters with lombok
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSourceAccountNumber() {
		return sourceAccountNumber;
	}
	public void setSourceAccountNumber(String sourceAccountNumber) {
		this.sourceAccountNumber = sourceAccountNumber;
	}
	public String getDestinationAccountNumber() {
		return destinationAccountNumber;
	}
	public void setDestinationAccountNumber(String destinationAccountNumber) {
		this.destinationAccountNumber = destinationAccountNumber;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public LocalDate getTransferDate() {
		return transferDate;
	}
	public void setTransferDate(LocalDate transferDate) {
		this.transferDate = transferDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "batch_id")
    @JsonBackReference
    private BatchTransfer batch_id;

	public BatchTransfer getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(BatchTransfer batch_id) {
		this.batch_id = batch_id;
	}
    public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

}