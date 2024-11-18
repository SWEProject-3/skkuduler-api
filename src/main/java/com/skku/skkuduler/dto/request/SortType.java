package com.skku.skkuduler.dto.request;

import org.springframework.data.domain.Sort;

import java.util.Optional;

public enum SortType {
    LATEST("latest"),
    LIKES("likes"),
    COMMENTS("comments");

    private final String value;

    SortType(String value){
        this.value = value;
    }
    public static Optional<SortType> of(String value){
        for(SortType sortType : SortType.values()){
            if(sortType.value.equals(value)) return Optional.of(sortType);
        }
        return Optional.empty();
    }
}
