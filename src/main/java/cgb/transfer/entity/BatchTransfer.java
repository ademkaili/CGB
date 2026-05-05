package cgb.transfer.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
public class BatchTransfer {
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRefLot() {
		return refLot;
	}

	public void setRefLot(String refLot) {
		this.refLot = refLot;
	}

	public String getSourceAccountNumber() {
		return sourceAccountNumber;
	}

	public void setSourceAccountNumber(String sourceAccountNumber) {
		this.sourceAccountNumber = sourceAccountNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<Transfer> getListTransfers() {
		return listTransfers;
	}

	public void setListTransfers(List<Transfer> listTransfers) {
		this.listTransfers = listTransfers;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//Numéro unique du lot, composé de la date de création et du n° du lot*/
	private String refLot;


	//L'identifiant unique du compte dont le transfert provient*/
	private String sourceAccountNumber;
	
	/*La description associée au lot*/
	private String description;

	//La date du lot
	private LocalDate date;
	
	private String state;

    @OneToMany(mappedBy = "batch_id", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Transfer> listTransfers = new ArrayList<Transfer>();
    
    public void addTransfer(Transfer transfer) {
        this.listTransfers.add(transfer);
    }

}