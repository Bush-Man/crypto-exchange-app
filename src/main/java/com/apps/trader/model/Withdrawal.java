package com.apps.trader.model;

import java.time.LocalDateTime;

import com.apps.trader.enums.WithdrawStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Withdrawal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long amount;

    @Enumerated(EnumType.STRING)
    private WithdrawStatus withdrawStatus;
    @ManyToOne
    private User user;

    private LocalDateTime date = LocalDateTime.now();

}
