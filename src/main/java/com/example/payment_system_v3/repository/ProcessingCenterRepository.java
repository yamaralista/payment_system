package com.example.payment_system_v3.repository;


import com.example.payment_system_v3.entity.ProcessingCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessingCenterRepository extends JpaRepository<ProcessingCenter,Long> {
}
