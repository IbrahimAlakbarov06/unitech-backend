package org.unitech.mstransfer.domain.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.unitech.mstransfer.domain.entity.Transfer;

import java.util.List;

@Repository
public interface TransferDao extends JpaRepository<Transfer, Long> {

    @Query("select t from Transfer t where t.fromAccountId = :accountId or t.toAccountId = :accountId")
    List<Transfer> getTransferByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT t FROM Transfer t WHERE t.userId = :userId ORDER BY t.createdAt DESC")
    List<Transfer> findRecentTransfersByUserId(@Param("userId") Long userId, Pageable pageable);
}
