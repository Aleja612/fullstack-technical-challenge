package com.code.inventory_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonApiResponse<T> {
    private Data<T> data;

    // Constructor Factory para facilitar uso
    public static <T> JsonApiResponse<T> success(String type, String id, T attributes) {
        return new JsonApiResponse<>(new Data<>(type, id, attributes));
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data<T> {
        private String type;
        private String id;
        private T attributes;
    }
}