package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        calculateFare(ticket, false);
    }

    public void calculateFare(Ticket ticket, Boolean discount) {
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

        if (duration <= 0.5) {
            ticket.setPrice(0);
            return;
        }

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                if (!discount) ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                else ticket.setPrice(Fare.DISCOUNT * (duration * Fare.CAR_RATE_PER_HOUR));
                break;
            }
            case BIKE: {
                if (!discount) ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                else ticket.setPrice(Fare.DISCOUNT * (duration * Fare.BIKE_RATE_PER_HOUR));
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}