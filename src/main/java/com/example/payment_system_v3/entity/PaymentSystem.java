package com.example.payment_system_v3.entity;

import com.example.payment_system_v3.entity.attribute.PaymentSystemAtrbt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    @Enumerated(value = EnumType.STRING)
    @Column(name = "payment_system_name")
    private PaymentSystemAtrbt name;

}
