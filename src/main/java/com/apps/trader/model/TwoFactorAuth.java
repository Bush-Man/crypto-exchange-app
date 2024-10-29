package com.apps.trader.model;

import com.apps.trader.enums.VerificationType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TwoFactorAuth {
    
    private boolean isEnabled;
    @Enumerated(EnumType.STRING)
    private VerificationType sendTo;
}
