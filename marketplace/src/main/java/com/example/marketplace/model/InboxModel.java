package com.example.marketplace.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InboxModel {

    private Integer id;
    private Integer fromAccountId;
    private Integer toAccountId;
    private String message;
    private LocalDateTime createDate;
    private Boolean status;

}
