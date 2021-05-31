package com.onefin.ewallet.service;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.onefin.ewallet.common.base.repository.mariadb.IBaseTransactionRepository;
import com.onefin.ewallet.common.domain.vietin.VietinEwalletTransaction;

@RepositoryRestResource(collectionResourceRel = "metaDatas", exported = false)
public interface EwalletTransactionRepository<T extends VietinEwalletTransaction>
		extends IBaseTransactionRepository<T> {

	VietinEwalletTransaction findByRequestIdAndTranStatus(String requestId, String tranStatus);

}
