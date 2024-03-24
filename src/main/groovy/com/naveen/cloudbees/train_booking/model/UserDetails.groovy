package com.naveen.cloudbees.train_booking.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

@Entity
class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @Email
//    @Column(unique = true)
    @NotNull(message = "Email cannot be null")
    String email

    @NotNull(message = "First name cannot be null")
    String firstName

    @NotNull(message = "Last name cannot be null")
    String lastName

    int seatNumber
    String trainSection
}
