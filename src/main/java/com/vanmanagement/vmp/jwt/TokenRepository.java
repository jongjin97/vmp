package com.vanmanagement.vmp.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<RefreshEntity, Long> {

    RefreshEntity save(RefreshEntity entity);

    RefreshEntity findByUser_Seq(Long id);
}
