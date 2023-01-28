package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        //Reserve a spot in the given parkingLot such that the total price is minimum. Note that the price per hour for each spot is different
        //Note that the vehicle can only be parked in a spot having a type equal to or larger than given vehicle
        //If parkingLot is not found, user is not found, or no spot is available, throw "Cannot make reservation" exception.

        if(!parkingLotRepository3.existsById(parkingLotId) || !userRepository3.existsById(userId)){
            throw new Exception("Cannot make reservation");
        }
        User user=userRepository3.findById(userId).get();
        ParkingLot parkingLot=parkingLotRepository3.findById(parkingLotId).get();
        List<Spot> spotList=parkingLot.getSpotList();
        Reservation reservation=new Reservation();

        int reservationAmount=Integer.MAX_VALUE;

        boolean spotAvailable=false;
        Spot spot1=new Spot();
        for(Spot spot:spotList){
            int fair=spot.getPricePerHour()*timeInHours;
            if(fair<reservationAmount && isParkPossible(spot,numberOfWheels)){
                spot1=spot;
                reservationAmount=fair;
                spotAvailable=true;
            }
        }
        if(!spotAvailable){
            throw new Exception("Cannot make reservation");
        }

        spot1.setOccupied(true);
        reservation.setSpot(spot1);
        reservation.setUser(user);
        reservation.setNumberOfHours(timeInHours);
        user.getReservationList().add(reservation);
        spot1.getReservationList().add(reservation);


        spotRepository3.save(spot1);
        userRepository3.save(user);
        reservationRepository3.save(reservation);
        return reservation;
    }
    public boolean isParkPossible(Spot spot,Integer numberOfWheels){
        if(!spot.getOccupied() && numberOfWheels==2 && spot.getSpotType()==SpotType.TWO_WHEELER)
            return true;
        else if(!spot.getOccupied() && (numberOfWheels>2 && numberOfWheels<=4) && spot.getSpotType()==SpotType.FOUR_WHEELER)
            return true;
        else if(!spot.getOccupied() && numberOfWheels>4 && spot.getSpotType()==SpotType.OTHERS)
            return true;
        return false;
    }
}
