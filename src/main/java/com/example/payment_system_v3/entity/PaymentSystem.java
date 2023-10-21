package com.example.payment_system_v3.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "payment_system")
public class PaymentSystem {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "payment_system_name")
    private String name;

}
