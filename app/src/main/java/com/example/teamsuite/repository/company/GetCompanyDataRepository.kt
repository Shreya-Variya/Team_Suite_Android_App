package com.example.teamsuite.repository.company

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.company.GetCompanyDataResponse

class GetCompanyDataRepository {
    suspend fun getCompanyData(companyid: String): retrofit2.Response<GetCompanyDataResponse>{
        return ApiObject.api.getCompanyData(companyid)
    }
}