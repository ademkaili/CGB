package cgb.transfer.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cgb.transfer.entity.BatchTransfer;
import cgb.transfer.entity.Transfer;

@Repository
public interface BatchTransferRepository extends JpaRepository<BatchTransfer, Long> {
	@Query("SELECT COUNT(id) FROM BatchTransfer WHERE date = :date")
    public int countBatchTransfers(@Param("date") LocalDate date);
}