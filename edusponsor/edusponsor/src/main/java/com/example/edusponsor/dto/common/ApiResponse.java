package com.example.edusponsor.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private int statusCode;
    private String type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean redirect;

    private ResponseData<T> responseData;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ResponseData<T> {
        private List<T> data;
        private List<String> message;
    }

    public static <T> ApiResponse<T> success(String... messages) {
        return ApiResponse.<T>builder()
                .statusCode(200)
                .type("SUCCESS")
                .responseData(ResponseData.<T>builder()
                        .message(messages == null ? Collections.emptyList() : List.of(messages))
                        .build())
                .build();
    }

    public static <T> ApiResponse<T> mapSuccess(T data, String... messages) {
        return ApiResponse.<T>builder()
                .statusCode(200)
                .type("SUCCESS")
                .responseData(ResponseData.<T>builder()
                        .data(data == null ? Collections.emptyList() : Collections.singletonList(data))
                        .message(messages == null ? Collections.emptyList() : List.of(messages))
                        .build())
                .build();
    }

    public static <T> ApiResponse<T> listSuccess(List<T> data, String... messages) {
        return ApiResponse.<T>builder()
                .statusCode(200)
                .type("SUCCESS")
                .responseData(ResponseData.<T>builder()
                        .data(data == null ? Collections.emptyList() : data)
                        .message(messages == null ? Collections.emptyList() : List.of(messages))
                        .build())
                .build();
    }


    public static <T> ApiResponse<T> error(int code, String... messages) {
        return ApiResponse.<T>builder()
                .statusCode(code)
                .type("ERROR")
                .responseData(ResponseData.<T>builder()
                        .data(Collections.emptyList())
                        .message(messages == null ? Collections.emptyList() : List.of(messages))
                        .build())
                .build();
    }

    public static <T> ApiResponse<T> warning(int code, String... messages) {
        return ApiResponse.<T>builder()
                .statusCode(code)
                .type("WARNING")
                .responseData(ResponseData.<T>builder()
                        .data(Collections.emptyList())
                        .message(messages == null ? Collections.emptyList() : List.of(messages))
                        .build())
                .build();
    }


    public static <T> ApiResponse<T> loginError(boolean redirection, int code, String... messages) {
        return ApiResponse.<T>builder()
                .statusCode(code)
                .type("ERROR")
                .redirect(redirection)
                .responseData(ResponseData.<T>builder()
                        .data(Collections.emptyList())
                        .message(messages == null ? Collections.emptyList() : List.of(messages))
                        .build())
                .build();
    }
}
