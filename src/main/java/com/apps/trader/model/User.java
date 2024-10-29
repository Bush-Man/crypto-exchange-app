package com.apps.trader.model;

import com.apps.trader.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users")
public class User {


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String fullName;
    private String email;

    //password ignored during db query
    @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.CUSTOMER;

    @Embedded
    private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();

}
