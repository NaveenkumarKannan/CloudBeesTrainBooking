package com.naveen.cloudbees.train_booking.service

import com.naveen.cloudbees.train_booking.model.Receipt

import com.naveen.cloudbees.train_booking.repository.PurchaseRepository
import com.naveen.cloudbees.train_booking.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository

    @Autowired
    private UserRepository userRepository

    private static int sections = 2
    private static int berthCountBySection = 3
    private static int totalBerths = sections * berthCountBySection
    private static int[] berths = new int[totalBerths]

    def createPurchase(Receipt receipt) {
        if(receipt.fromLocation != "London" || receipt.toLocation != "France" || receipt.pricePaid != 5){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid data")
        }

        def seatNumber = bookInSectionA()
        def trainSection = "A"
        if (seatNumber == -1) {
            seatNumber = bookInSectionB()
            trainSection = "B"
        }
        if (seatNumber != -1) {
            receipt.user.seatNumber = seatNumber
            receipt.user.trainSection = trainSection
            return purchaseRepository.save(receipt)
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No berth available for the train")
        }
    }

    private static def bookInSectionA() {
        for (int i=0;i<totalBerths/sections;i++) {
            if (berths[i] == 0) {
                berths[i] = 1
                return i + 1
            }
        }
        return -1
    }
    private static def bookInSectionB() {
        for (int i=totalBerths/sections;i<totalBerths;i++) {
            if (berths[i] == 0) {
                berths[i] = 1
                return i + 1
            }
        }
        return -1

    }

    def getAllPurchases() {
        return purchaseRepository.findAll()
    }

    def getPurchaseById(Long id) {
        return purchaseRepository.findById(id)
    }

    def getAllUsers() {
        return userRepository.findAll()
    }

    def getUserById(Long id) {
        return userRepository.findById(id)
    }

    def removeUser(Long id) {
        def tUser = userRepository.findById(id)
        if (tUser) {
            def currentUser = tUser.get()
            berths[currentUser.seatNumber-1] = 0
            currentUser.seatNumber = 0
            currentUser.trainSection = ""
            userRepository.save(currentUser)
        }
        return tUser
    }

    def deleteUser(Long id) {
        def tUser = userRepository.findById(id)
        if (tUser) {
            def currentUser = tUser.get()
            Receipt receipt = purchaseRepository.findReceiptByUserId(currentUser.id)
            if(currentUser.seatNumber>0)
                berths[currentUser.seatNumber-1] = 0
            receipt.user = null
            purchaseRepository.save(receipt)
            userRepository.delete(currentUser)
        }
        return tUser
    }

    def modifyUserSeat(Long id, String trainSection) {
        def user = userRepository.findById(id)

        if (user) {
            def currentUser = user.get()
            if(currentUser.trainSection != trainSection){
                def seatNumber
                if(trainSection == "A"){
                    seatNumber = bookInSectionA()
                }else if(trainSection == "B"){
                    seatNumber = bookInSectionB()
                }else{
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                          .body("Invalid train section")
                }
                if(seatNumber!= -1){
                    berths[currentUser.seatNumber-1] = 0
                    currentUser.seatNumber = seatNumber
                    currentUser.trainSection = trainSection
                    return userRepository.save(currentUser)
                }else{
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                          .body("No seats available in $trainSection section")
                }
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User is already allocated a seat in the $trainSection section only")
            }
        } else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with id $id not found")
        }
    }

    def modifyUserSeat(Long id, int seatNumber) {
        def user = userRepository.findById(id)

        if (user) {
            def currentUser = user.get()
            if(seatNumber>0 && seatNumber<=totalBerths && currentUser.seatNumber != seatNumber){
                if(berths[seatNumber-1] == 0){
                    def trainSection
                    if(seatNumber<=berthCountBySection){
                        trainSection = "A"
                    }else {
                        trainSection = "B"
                    }
                    berths[currentUser.seatNumber-1] = 0
                    berths[seatNumber-1] = 1
                    currentUser.seatNumber = seatNumber
                    currentUser.trainSection = trainSection

                    return userRepository.save(currentUser)
                }else{
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Provided seat number $seatNumber is already allocated")
                }
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid seat number")
            }
        } else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with id $id not found")
        }
    }
}
