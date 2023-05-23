package com.example.marketplace.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseModel<T> {
    private String description;
    private Integer status;
    private LocalDateTime timestamp;
    private T data;

    public ResponseModel(){
        this.timestamp = LocalDateTime.now();
    }
}
