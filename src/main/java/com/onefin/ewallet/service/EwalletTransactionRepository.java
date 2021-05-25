package com.onefin.ewallet.service;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onefin.ewallet.model.EwalletTransaction;

@Repository
public interface EwalletTransactionRepository extends JpaRepository<EwalletTransaction, UUID> {

	EwalletTransaction findByRequestIdAndTranStatus(String requestId, String tranStatus);

}
