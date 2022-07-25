package com.htecgroup.skynest.service;

import com.htecgroup.skynest.annotation.UniqueCompany;
import com.htecgroup.skynest.model.dto.CompanyDto;
import com.htecgroup.skynest.model.request.CompanyAddRequest;
import com.htecgroup.skynest.model.request.CompanyEditRequest;
import com.htecgroup.skynest.model.response.CompanyResponse;

import javax.validation.Valid;
import java.util.UUID;

public interface CompanyService {

  CompanyResponse addCompany(@Valid @UniqueCompany CompanyAddRequest companyAddRequest);

  CompanyResponse editCompany(CompanyEditRequest companyEditRequest);

  CompanyDto findById(UUID companyId);

  CompanyDto findByName(String name);

  CompanyResponse getMyCompany();
}
