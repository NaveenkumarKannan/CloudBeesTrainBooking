package com.naveen.cloudbees.train_booking.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

@Entity
class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @NotNull(message = "From location cannot be null")
    String fromLocation

    @NotNull(message = "To location cannot be null")
    String toLocation

//    @NotNull(message = "User cannot be null")
    @OneToOne(cascade = CascadeType.ALL)
    UserDetails user

    @Positive(message = "Price must be positive")
    int pricePaid

}