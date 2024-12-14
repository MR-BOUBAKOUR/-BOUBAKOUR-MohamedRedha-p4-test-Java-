package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        if (ticket.getParkingSpot() == null || ticket.getParkingSpot().getParkingType() == null) {
            throw new IllegalArgumentException("The Parking Type is null");
        }

        double inMillis = ticket.getInTime().getTime();
        double inHour = inMillis / (60 * 60 * 1000);

        double outMillis = ticket.getOutTime().getTime();
        double outHour = outMillis / (60 * 60 * 1000);

        double duration = outHour - inHour;

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}