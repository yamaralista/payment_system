package com.example.payment_system_v3.entity;

import com.example.payment_system_v2.entity.attribute.PaymentState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "payment_amount", nullable = false)
    private Integer paymentAmount;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_card_id", nullable = false)
    private Card senderCard;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_card_id", nullable = false)
    private Card receiverCard;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_system_id")
    private PaymentSystem paymentSystem;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "payment_state", nullable = false)
    private PaymentState paymentState;


}