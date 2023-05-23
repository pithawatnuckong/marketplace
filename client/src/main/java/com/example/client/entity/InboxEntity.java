package com.example.client.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "inbox")
@Data
public class InboxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private Integer fromAccountId;

    @Column(nullable = false)
    private Integer toAccountId;

    private String message;
    private LocalDateTime createDate;
    private Boolean status;
}
