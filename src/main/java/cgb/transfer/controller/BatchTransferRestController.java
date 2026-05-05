package cgb.transfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cgb.transfer.dto.BatchTransferRequest;
import cgb.transfer.dto.TransferRequest;
import cgb.transfer.entity.BatchTransfer;
import cgb.transfer.entity.Transfer;
import cgb.transfer.service.BatchTransferService;
import cgb.transfer.service.TransferService;
import cgb.transfer.exception.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/batch")
public class BatchTransferRestController {

	@Autowired
	private TransferService transferService;

	@Autowired
	private BatchTransferService batchTransferService;



	@PostMapping("/async")
	public ResponseEntity<?> createTransfer(@RequestBody BatchTransferRequest batchTransferRequest) throws InvalidAccountException, DateTransferException, NegativeTransferAmountException, InsufficientFundsException, IOException {
		//public ResponseEntity<Transfer> createTransfer(@RequestBody TransferRequest transferRequest) {
		try {
			String refLot = batchTransferService.generateRefLot();

			batchTransferService.createBatchTransfer(batchTransferRequest.getSourceAccountNumber(),
					batchTransferRequest.getDescription(),
					batchTransferRequest.getListTransfers());

			Map<String, Object> response = new HashMap<String, Object>();
			response.put("refLot", refLot);
			response.put("dateLancement", LocalDate.now());
			response.put("message", "Traitement lancé");
			response.put("etat", "received");

			return ResponseEntity.ok().body(response);
		} catch (RuntimeException e) {
			TransferResponse errorResponse = new TransferResponse("FAILURE", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}

	@DeleteMapping
	public ResponseEntity<?> deleteBatchTransfer(@RequestBody Long id) {
		try {
			BatchTransfer batch = batchTransferService.deleteBatchTransfer(id);
			System.out.println(batch);
			TransferResponse succesResponse = new TransferResponse("SUCCESS", batch.toString());
			return ResponseEntity.ok(succesResponse);
		}catch (RuntimeException | DeleteTransferException e) {
			TransferResponse errorResponse = new TransferResponse("FAILURE", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		} 
	}

	@GetMapping("/{refLot}")
    public ResponseEntity<?> getTransfer(@PathVariable String refLot) {
        BatchTransfer batch = batchTransferService.findBatchByRefLot(refLot);
        List<Transfer> list = transferService.getTransferFromBatch(refLot);
        batch.setListTransfers(list);

        return ResponseEntity.ok(batch);
    }

	/*
    @PostMapping
    public ResponseEntity<String> testTransfer(@RequestBody String s) {
    	System.out.println("Post reçu");
        return ResponseEntity.ok("Post bien traité: "+ s);
    } 
	 */

}