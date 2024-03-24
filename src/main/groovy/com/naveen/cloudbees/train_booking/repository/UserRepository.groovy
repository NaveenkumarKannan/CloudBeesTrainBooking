package com.naveen.cloudbees.train_booking.repository

import com.naveen.cloudbees.train_booking.model.UserDetails
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository extends JpaRepository<UserDetails,Long> {
}
