package com.example.payment_system_v3.repository;


import com.example.payment_system_v3.entity.PaymentSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentSystemRepository extends JpaRepository<PaymentSystem,Long> {
}
