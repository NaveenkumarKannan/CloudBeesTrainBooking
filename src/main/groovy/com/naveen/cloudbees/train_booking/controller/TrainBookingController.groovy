package com.naveen.cloudbees.train_booking.controller

import com.naveen.cloudbees.train_booking.model.Receipt
import com.naveen.cloudbees.train_booking.service.PurchaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/train_booking")
class TrainBookingController {
    @Autowired
    private PurchaseService purchaseService

    @PostMapping("/purchase")
    def createPurchase(@RequestBody Receipt receipt) {
        return purchaseService.createPurchase(receipt)
    }
    @GetMapping("/purchase")
    def getAllPurchases() {
        return purchaseService.getAllPurchases()
    }

    @GetMapping("/purchase/{id}")
    def getPurchaseById(@PathVariable("id") Long id) {
        def receipt = purchaseService.getPurchaseById(id)
        if (receipt) {
            return receipt
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Purchase with id $id not found")
        }
    }

    @GetMapping("/users")
    def getAllUsers() {
        return purchaseService.getAllUsers()
    }

    @GetMapping("/users/{id}")
    def getUserById(@PathVariable("id") Long id) {
        def user = purchaseService.getUserById(id)
        if (user) {
            return user
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with id $id not found")
        }
    }

    @PatchMapping("/users/{id}")
    def removeUser(@PathVariable("id") Long id) {
        def user = purchaseService.removeUser(id)
        if (user) {
            return user
        } else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with id $id not found")
        }
    }

    @DeleteMapping("/users/{id}")
    def deleteUser(@PathVariable("id") Long id) {
        def user = purchaseService.deleteUser(id)
        if (user) {
            return "User with id $id deleted successfully"
        } else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with id $id not found")
        }
    }

    @PatchMapping("/users/{id}/modify_seat/section/{trainSection}")
    def modifyUserSeat(@PathVariable("id") Long id, @PathVariable("trainSection") String trainSection) {
        return purchaseService.modifyUserSeat(id, trainSection)
    }

    @PatchMapping("/users/{id}/modify_seat/seat_number/{seatNumber}")
    def modifyUserSeat(@PathVariable("id") Long id, @PathVariable("seatNumber") int seatNumber) {
        return purchaseService.modifyUserSeat(id, seatNumber)
    }
}
