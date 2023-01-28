package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot=new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);
        parkingLotRepository1.save(parkingLot);
        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();
        Spot spot=new Spot();


        spot.setParkingLot(parkingLot);
        if(numberOfWheels>2 && numberOfWheels<=4)
            spot.setSpotType(SpotType.FOUR_WHEELER);
        else if(numberOfWheels>4)
            spot.setSpotType(SpotType.OTHERS);
        else
            spot.setSpotType(SpotType.TWO_WHEELER);
//
        spot.setPricePerHour(pricePerHour);
        spot.setOccupied(false);
        spot.setParkingLot(parkingLot);
        parkingLot.getSpotList().add(spot);
        parkingLotRepository1.save(parkingLot);
        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        Spot spot=spotRepository1.findById(spotId).get();
        ParkingLot parkingLot=spot.getParkingLot();
        parkingLot.getSpotList().remove(spot);
        spotRepository1.deleteById(spotId);
        parkingLotRepository1.save(parkingLot);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {

            ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();
            List<Spot> spots=parkingLot.getSpotList();
            Spot spot1=new Spot();
            for(Spot spot:spots){
                if(spot.getId()==spotId){
                    spot.setParkingLot(parkingLot);
                    spot.setPricePerHour(pricePerHour);
                    spot1=spot;
                }
            }

            parkingLot.setSpotList(spots);
            parkingLotRepository1.save(parkingLot);
            return spot1;


    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
