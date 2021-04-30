package com.onefin.ewallet.service;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.onefin.ewallet.model.EwalletTransaction;

@Repository
public interface EwalletTransactionRepository extends CrudRepository<EwalletTransaction, UUID> {

}
