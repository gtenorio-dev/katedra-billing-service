package com.katedra.biller.app.repository;

import com.katedra.biller.app.entity.SessionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends CrudRepository<SessionEntity, Long> {

    @Query("SELECT s FROM SessionEntity s WHERE s.account.cuit = :cuit")
    SessionEntity findSessionByCuit(@Param("cuit") Long cuit);

}
