package com.ms.spring_security_jwt.infrastructure.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface UrlParamService {
    Pageable getPageable();

    String getSearch();

    Sort getSort();
}
