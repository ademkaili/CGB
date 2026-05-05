package cgb.transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cgb.transfer.dto.TransferRequest;
import cgb.transfer.entity.Account;
import cgb.transfer.entity.State;
import cgb.transfer.entity.Transfer;
import cgb.transfer.exception.*;
import cgb.transfer.exception.DeleteTransferException.FailureTransfert;
import cgb.transfer.repository.AccountRepository;
import cgb.transfer.repository.TransferRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransferService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransferRepository transferRepository;

	/*
	 * Rappel du cours sur les transactions... Tout ou rien
	 */
	@Transactional
	public Transfer createTransfer(String sourceAccountNumber, String destinationAccountNumber, Double amount, LocalDate transferDate, String description) throws InvalidAccountException, DateTransferException, NegativeTransferAmountException, InsufficientFundsException {
		Transfer transfer = new Transfer();
        transfer.setSourceAccountNumber(sourceAccountNumber);
        transfer.setDestinationAccountNumber(destinationAccountNumber);
        transfer.setAmount(amount);
        transfer.setTransferDate(transferDate);
        transfer.setDescription(description);
		Optional<Account> sourceAccount = accountRepository.findById(sourceAccountNumber);
		Optional<Account> destinationAccount = accountRepository.findById(destinationAccountNumber);
		
		if (!sourceAccount.isPresent()) {
			throw new InvalidAccountException("source");
		}

		if (!destinationAccount.isPresent()) {
			throw new InvalidAccountException("destination");
		}

		if (transferDate.isBefore(LocalDate.now())) {
			throw new DateTransferException();
		} else if (amount <= 0) {
			throw new NegativeTransferAmountException();
		} else if (sourceAccount.get().getSolde().compareTo(amount) < 0) {
			throw new InsufficientFundsException();
		} else {
			sourceAccount.get().setSolde(sourceAccount.get().getSolde() - (amount));
			destinationAccount.get().setSolde(destinationAccount.get().getSolde() + (amount));
			accountRepository.save(sourceAccount.get());
			accountRepository.save(destinationAccount.get());

			return transferRepository.save(transfer);
		}
	}
	
	@Transactional
	public Transfer createTransferForBatch(String sourceAccountNumber, String destinationAccountNumber, Double amount, LocalDate transferDate, String description) throws InvalidAccountException, DateTransferException, NegativeTransferAmountException, InsufficientFundsException {
		Transfer transfer = new Transfer();
        transfer.setSourceAccountNumber(sourceAccountNumber);
        transfer.setDestinationAccountNumber(destinationAccountNumber);
        transfer.setAmount(amount);
        transfer.setTransferDate(transferDate);
        transfer.setDescription(description);
        transfer.setState(State.WAITING.getNom());
        transferRepository.save(transfer);

        Optional<Account> sourceAccount = accountRepository.findById(sourceAccountNumber);

        if (!sourceAccount.isPresent()) {
            transfer.setState(State.FAILURE.getNom());
            return transferRepository.save(transfer);
        }

        Optional<Account> destinationAccount = accountRepository.findById(destinationAccountNumber);

        if (!destinationAccount.isPresent()) {
            transfer.setState(State.FAILURE.getNom());
            return transferRepository.save(transfer);
        }
        if (transferDate.isBefore(LocalDate.now())) {
            transfer.setState(State.FAILURE.getNom());
            return transferRepository.save(transfer);
        } else if (amount < 0) {
            transfer.setState(State.FAILURE.getNom());
            return transferRepository.save(transfer);
        } else if (sourceAccount.get().getSolde().compareTo(amount) < 0) {
            transfer.setState(State.CANCELED.getNom());
            return transferRepository.save(transfer);
        } else {
            sourceAccount.get().setSolde(sourceAccount.get().getSolde() - (amount));
            destinationAccount.get().setSolde(destinationAccount.get().getSolde() + (amount));

            accountRepository.save(sourceAccount.get());
            accountRepository.save(destinationAccount.get());

            transfer.setState(State.SUCCESS.getNom());
            return transferRepository.save(transfer);
        }
	}
    public List<TransferRequest> findByRefLotAndCancelled(String refLot) {
        List<TransferRequest> trq = new ArrayList<TransferRequest>();
        List<Transfer> list = transferRepository.findByRefLotAndCancelled(refLot);
        for (Transfer t : list){
            TransferRequest temp = new TransferRequest();
            temp.setDestinationAccountNumber(t.getDestinationAccountNumber());
            temp.setAmount(t.getAmount());
            temp.setDescription(t.getDescription());
            trq.add(temp);
        }
        return trq;
    }

	@Transactional
	public Transfer deleteTransfer(Long id) throws DeleteTransferException {
		Optional<Transfer> otranfer = transferRepository.findById(id);
		transferRepository.deleteById(id);
		if (otranfer.isEmpty())
			throw new DeleteTransferException(FailureTransfert.OBJECT_NOT_FOUND);
		return otranfer.orElse(null);
	}
	public List<Transfer> getTransferFromBatch(String refLot) {
        return transferRepository.getTransferFromBatch(refLot);
    }
	public List<Transfer> findByRefLotAndNotSuccess(String refLot) {
        return transferRepository.findByRefLotAndNotSuccess(refLot);
    }

    public List<Transfer> findByDateIntervalAndNotSuccess(LocalDate start, LocalDate end) {
        return transferRepository.findByDateIntervalAndNotSuccess(start, end);
    }

    public List<Transfer> findByDestAccountAndNotSuccess(String destinationAccountNumber) {
        return transferRepository.findByDestAccountAndNotSuccess(destinationAccountNumber);
    }
    
}
