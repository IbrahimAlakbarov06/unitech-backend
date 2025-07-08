package org.unitech.msaccount.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unitech.msaccount.domain.entity.Account;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserId(Long userId);

    boolean existsByCartNumber(String cartNumber);
}
