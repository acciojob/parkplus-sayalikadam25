package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot=new ParkingLot(name,address);
        return parkingLotRepository1.save(parkingLot);
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();
        Spot spot=new Spot(pricePerHour);
        List<Spot> list;
        if(Objects.nonNull(parkingLot.getSpotList()) && !parkingLot.getSpotList().isEmpty())
            list = parkingLot.getSpotList();
        else
            list = new ArrayList<>();
        list.add(spot);
        parkingLot.setSpotList(list);
        spot.setParkingLot(parkingLot);
        if(numberOfWheels==2)
            spot.setSpotType(SpotType.TWO_WHEELER);
        else if(numberOfWheels==4)
            spot.setSpotType(SpotType.FOUR_WHEELER);
        else
            spot.setSpotType(SpotType.OTHERS);
//        spotRepository1.save(spot);
        spot.setOccupied(true);
        parkingLotRepository1.save(parkingLot);
        return spotRepository1.save(spot);
    }

    @Override
    public void deleteSpot(int spotId) {
        if(spotRepository1.existsById(spotId)){
            Spot spot=spotRepository1.findById(spotId).get();
            ParkingLot parkingLot=spot.getParkingLot();
            parkingLot.getSpotList().remove(spot);
            spotRepository1.deleteById(spotId);
            parkingLotRepository1.save(parkingLot);
        }
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();
        Spot spot=spotRepository1.findById(spotId).get();
        spot.setPricePerHour(pricePerHour);
        List<Spot> list = parkingLot.getSpotList();
        list.add(spot);
        parkingLot.setSpotList(list);
        parkingLotRepository1.save(parkingLot);
        return spotRepository1.save(spot);
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
