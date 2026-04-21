package cgb.transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cgb.transfer.entity.Account;
import cgb.transfer.entity.Transfer;
import cgb.transfer.exception.*;
import cgb.transfer.exception.DeleteTransferException.FailureTransfert;
import cgb.transfer.repository.AccountRepository;
import cgb.transfer.repository.TransferRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
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
	public Transfer deleteTransfer(Long id) throws DeleteTransferException {
		Optional<Transfer> otranfer = transferRepository.findById(id);
		transferRepository.deleteById(id);
		if (otranfer.isEmpty())
			throw new DeleteTransferException(FailureTransfert.OBJECT_NOT_FOUND);
		return otranfer.orElse(null);
	}
}
