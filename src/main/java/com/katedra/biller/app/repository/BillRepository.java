package com.katedra.biller.app.repository;

import com.katedra.biller.app.entity.BillEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BillRepository extends CrudRepository<BillEntity, Long> {

    // TODO revisar si es que sede filtrar por fechaComprobante o por fechaServicioDesde
    @Query("SELECT b FROM BillEntity b WHERE b.account.cuit = :cuit AND b.fechaComprobante >= :since AND b.fechaComprobante <= :to")
    List<BillEntity> findBillsByCuitBetweenDates(
            @Param("cuit") Long cuit,
            @Param("since") Date since,
            @Param("to") Date to);

}
