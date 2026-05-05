package cgb.transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import cgb.transfer.dto.TransferRequest;
import cgb.transfer.entity.Account;
import cgb.transfer.entity.BatchTransfer;
import cgb.transfer.entity.State;
import cgb.transfer.entity.Transfer;
import cgb.transfer.exception.*;
import cgb.transfer.exception.DeleteTransferException.FailureTransfert;
import cgb.transfer.repository.AccountRepository;
import cgb.transfer.repository.BatchTransferRepository;
import cgb.transfer.repository.TransferRepository;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BatchTransferService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransferRepository transferRepository;

	@Autowired
	private BatchTransferRepository batchTransferRepository;

	@Autowired
    private TransferService transferService;
	
	@Autowired
	private LogService logger;
	
	@Autowired
    private MailService mail;
	
	/*
	 * Rappel du cours sur les transactions... Tout ou rien
	 */

	@Async
	@Transactional
	public void createBatchTransfer(String sourceAccountNumber, String description, List<TransferRequest> listTransfers) throws InvalidAccountException, NegativeTransferAmountException, DateTransferException, InsufficientFundsException, IOException {
		BatchTransfer batch = new BatchTransfer();
		batch.setRefLot(generateRefLot());
		batch.setSourceAccountNumber(sourceAccountNumber);
		batch.setDescription(description);
		batch.setDate(LocalDate.now());
		batch.setState(State.RECEIVED.getNom());
		batchTransferRepository.save(batch);
		logger.log("Batch refrence: "+ batch.getRefLot() + " | Creating Batch succeeded");
		if (!accountRepository.findById(sourceAccountNumber).isPresent()) {
            logger.log("Batch reference: "+ batch.getRefLot() + " | Invalid transfer: Source account doesn't exist");
			throw new InvalidAccountException("Source");
		}
		int successCount = 0;
        int failureCount = 0;
		for (TransferRequest transferRequest: listTransfers) {
			Transfer transfer = transferService.createTransferForBatch(sourceAccountNumber,
					transferRequest.getDestinationAccountNumber(),
					transferRequest.getAmount(),
					LocalDate.now(),
					description);
			if (transfer.getState() == "success") {
                successCount += 1;
            } else {
                failureCount += 1;
            }

			transfer.setBatch_id(batch);
			batch.addTransfer(transfer);
			transferRepository.save(transfer);
			batchTransferRepository.save(batch);
		}
		batch.setState(State.CLOSED.getNom());
		logger.log("Batch reference: "+ batch.getRefLot() + " | Batch Transfers completed");
		batchTransferRepository.save(batch);
		try {
            mail.sendBatchReport("comptable@gsb.fr", batch.getRefLot(), batch.getDate(), successCount, failureCount);
            logger.log("Batch reference: " + batch.getRefLot() + " | Notification email sent successfully");
        } catch (Exception e) {
            logger.log("WARNING: Notification email failed for batch " + batch.getRefLot() + ". Error: " + e.getMessage());
        }
	}
	
	@Transactional
    public BatchTransfer deleteBatchTransfer(long id) throws DeleteTransferException {
        Optional<BatchTransfer> oBatch     = batchTransferRepository.findById(id);
        transferRepository.deleteById(id);
        if (oBatch.isEmpty())
            throw new DeleteTransferException(FailureTransfert.OBJECT_NOT_FOUND);
        return oBatch.orElse(null);
    }
	private int countBatchTransfers(LocalDate date) {
        return batchTransferRepository.countBatchTransfers(date);
    }
    public String generateRefLot() {
        return LocalDate.now().toString() + "-" + (countBatchTransfers(LocalDate.now()) + 1);
    }
    public BatchTransfer findBatchByRefLot(String refLot) {
        return batchTransferRepository.findBatchByRefLot(refLot);
    }
}
