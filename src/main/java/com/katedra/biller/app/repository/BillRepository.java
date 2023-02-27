package com.katedra.biller.app.repository;

import com.katedra.biller.app.entity.BillEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends CrudRepository<BillEntity, Long> {
}
