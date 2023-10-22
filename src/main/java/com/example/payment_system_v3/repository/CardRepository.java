package com.example.payment_system_v3.repository;


import com.example.payment_system_v3.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
      Optional<Card> getCardByCardNumber(String cardNumber);
      Optional<Card> getCardById(Long id);
}
