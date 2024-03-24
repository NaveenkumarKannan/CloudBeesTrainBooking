package com.naveen.cloudbees.train_booking.repository

import com.naveen.cloudbees.train_booking.model.Receipt
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PurchaseRepository extends JpaRepository<Receipt, Long> {
    @Query(value = "SELECT r FROM Receipt r WHERE r.user.id = :user_id")
    Receipt findReceiptByUserId(@Param("user_id") Long id)
}
