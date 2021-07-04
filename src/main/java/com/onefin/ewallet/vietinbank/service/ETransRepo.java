package com.onefin.ewallet.vietinbank.service;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.onefin.ewallet.common.base.repository.mariadb.IBaseTransactionRepository;
import com.onefin.ewallet.common.domain.bank.vietin.VietinEwalletTransaction;

@RepositoryRestResource(collectionResourceRel = "metaDatas", exported = false)
public interface ETransRepo<T extends VietinEwalletTransaction>
		extends IBaseTransactionRepository<T> {

	VietinEwalletTransaction findByRequestIdAndTranStatus(String requestId, String tranStatus);

	@Modifying
	@Transactional
	@Query(value = "Update VietinEwalletTransaction Set updatedDate = CURRENT_TIMESTAMP Where id = :id")
	public void updateTransaction(@Param("id") UUID id);

}
