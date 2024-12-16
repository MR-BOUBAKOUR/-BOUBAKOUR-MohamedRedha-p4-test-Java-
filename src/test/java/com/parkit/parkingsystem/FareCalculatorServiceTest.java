package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test
    public void calculateFareCarWithOneHourParkingTime(){
        // Given: A car parked for 1 hour
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        // When
        fareCalculatorService.calculateFare(ticket);

        // Then: The price should be the car rate per hour
        assertEquals(Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithOneHourParkingTime(){
        // Given: A bike parked for 1 hour
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        // When
        fareCalculatorService.calculateFare(ticket);

        // Then: The price should be the bike rate per hour
        assertEquals(Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
    }

    @Test
    public void calculateFareUnkownType(){
        // Given: A parking spot with unknown parking type (null)
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        // When: The fare calculation is attempted with an unknown parking type (null)
        // The method fareCalculatorService.calculateFare(ticket) is invoked

        // Then: An IllegalArgumentException should be thrown due to null parking type
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket), "The Parking Type is null");
    }

    @Test
    public void calculateFareBikeWithFutureInTime(){
        // Given: A bike with a future in-time (one hour from now)
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        // When: The fare calculation is attempted
        // The method fareCalculatorService.calculateFare(ticket) is invoked

        // Then: An IllegalArgumentException should be thrown due to incorrect out-time
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket), "Out time provided is incorrect:" + ticket.getOutTime().toString());
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
        // Given: A bike parked for 45 minutes
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) ); // 45 minutes parking time
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        // When
        fareCalculatorService.calculateFare(ticket);

        // Then: The price should be 3/4 of the bike rate per hour
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime(){
        // Given: A car parked for 45 minutes
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) ); // 45 minutes parking time
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        // When
        fareCalculatorService.calculateFare(ticket);

        // Then: The price should be 3/4 of the car rate per hour
        assertEquals( (0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTime(){
        // Given: A car parked for 24 hours
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) ); // 24 hours parking time
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        // When
        fareCalculatorService.calculateFare(ticket);

        // Then: The price should be 24 times the car rate per hour
        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

}