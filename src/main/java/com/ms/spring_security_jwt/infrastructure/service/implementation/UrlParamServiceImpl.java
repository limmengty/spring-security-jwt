package com.ms.spring_security_jwt.infrastructure.service.implementation;

import com.ms.spring_security_jwt.infrastructure.service.UrlParamService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UrlParamServiceImpl implements UrlParamService {
    private final String q;
    private final String page;
    private final String size;
    private final String isPaged;
    // can use as multiple sorts like: id:desc,createdDate:desc
    private final String sort;

    public UrlParamServiceImpl(String q, String page, String size, String isPaged, String sort) {
        this.q = q;
        this.page = page;
        this.size = size;
        this.isPaged = isPaged;
        this.sort = sort;
    }

    @Override
    public Pageable getPageable() {
        // first check is paged is false then return un-page
        boolean _isPaged = Boolean.parseBoolean(this.isPaged);
        if (!_isPaged) return Pageable.unpaged();

        // then add default page and size if no values
        int _page = this.page == null ? 0 : Integer.parseInt(this.page);
        int _size = this.size == null ? 10 : Integer.parseInt(this.size);

        // prepare sort by default
        Sort _sort = this.getSort();

        return PageRequest.of(_page, _size, _sort);
    }

    @Override
    public String getSearch() {
        return this.q;
    }

    @Override
    public Sort getSort() {
        if (this.sort != null) {
            // sort = id:desc,name:asc
            List<Sort.Order> sortList = Arrays.stream(this.sort.split(",")).filter(it -> !it.isEmpty()).map(it -> {
                // 1. id:desc
                // 2. name:asc
                Sort.Order sortByOrder;
                String[] order = it.split(":");
                // 1. order = ["id", "desc"];
                // 2. order = ["name", "asc"];
                if (order.length > 1) {
                    String key = order[0]; // "id" // "name"
                    String value = order[1]; // "desc" // "asc"
                    if (key != null && value != null) {
                        if (value.equalsIgnoreCase("desc")) {
                            sortByOrder = Sort.Order.desc(key);
                        } else {
                            sortByOrder = Sort.Order.asc(key);
                        }
                    } else {
                        sortByOrder = null;
                    }
                } else {
                    sortByOrder = null;
                }
                return sortByOrder;
            }).collect(Collectors.toList());

            return Sort.by(sortList);
        } else {
            return Sort.unsorted();
        }
    }
}
