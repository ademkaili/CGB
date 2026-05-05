package cgb.transfer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import cgb.transfer.entity.Account;
import cgb.transfer.entity.Transfer;
import cgb.transfer.exception.DateTransferException;
import cgb.transfer.exception.DeleteTransferException;
import cgb.transfer.exception.InsufficientFundsException;
import cgb.transfer.exception.InvalidAccountException;
import cgb.transfer.exception.NegativeTransferAmountException;
import cgb.transfer.repository.AccountRepository;
import cgb.transfer.repository.TransferRepository;
import cgb.transfer.service.TransferService;

@ExtendWith(MockitoExtension.class)
public class TransferServiceUnitTest {

	private static Transfer transfer;
	private static Account srcAccount;
	private static Account destAccount;

	@Mock
	private TransferRepository transferRepository;
	@Mock
	private AccountRepository accountRepository;

	@InjectMocks
	private TransferService transferService;

	@BeforeAll
	public static void init() {
		transfer = new Transfer();
		transfer.setSourceAccountNumber("123456789");
		transfer.setDestinationAccountNumber("987654321");
		transfer.setAmount(40.0);
		transfer.setTransferDate(LocalDate.now());
		transfer.setDescription("Test");

		srcAccount = new Account();
		srcAccount.setAccountNumber("123456789");
		srcAccount.setSolde(200.0);

		destAccount = new Account();
		destAccount.setAccountNumber("987654321");
		destAccount.setSolde(200.0);
	}

	@Test
	void testCreateTransfer() throws InvalidAccountException, NegativeTransferAmountException, DateTransferException,
			InsufficientFundsException {
		when(accountRepository.findById("123456789")).thenReturn(Optional.of(srcAccount));
		when(accountRepository.findById("987654321")).thenReturn(Optional.of(destAccount));

		when(transferRepository.save(Mockito.any(Transfer.class))).thenReturn(transfer);

		Transfer result = transferService.createTransfer("123456789", "987654321", 40.0, LocalDate.now(),
				"Test");

		assertEquals("123456789", result.getSourceAccountNumber());
		assertEquals("987654321", result.getDestinationAccountNumber());
		assertEquals(40.0, result.getAmount());
		assertEquals("Test", result.getDescription());
	}

	@Test
	void testDeleteTransfer() throws DeleteTransferException {
		when(transferRepository.findById(1l)).thenReturn(Optional.of(transfer));

		Transfer result = transferService.deleteTransfer(1L);

		assertEquals("123456789", result.getSourceAccountNumber());
		assertEquals("987654321", result.getDestinationAccountNumber());
		assertEquals(40.0, result.getAmount());
		assertEquals("Test", result.getDescription());
	}

}
