import React, { useEffect, useState } from "react";
import axios from "axios";

function FlightList() {
  const [flights, setFlights] = useState([]);

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/flights", {
        auth: {
          username: "user",
          password: "6348f129-ba24-4f06-8d83-722be3b46ee0"
        }
      })
      .then(response => setFlights(response.data))
      .catch(error => console.error("Error fetching flights:", error));
  }, []);

  return (
    <div>
      <h2>Available Flights</h2>
      <table border="1" cellPadding="8">
        <thead>
          <tr>
            <th>Flight Number</th>
            <th>Departure</th>
            <th>Arrival</th>
            <th>Aircraft</th>
            <th>Duration (min)</th>
            <th>Status</th>
            <th>Economy Price</th>
          </tr>
        </thead>
        <tbody>
          {flights.map(flight => (
            <tr key={flight.flightId}>
              <td>{flight.flightNumber}</td>
              <td>{flight.departureAirport.city} ({flight.departureAirport.airportCode})</td>
              <td>{flight.arrivalAirport.city} ({flight.arrivalAirport.airportCode})</td>
              <td>{flight.aircraft.aircraftName}</td>
              <td>{flight.flightDurationMinutes}</td>
              <td>{flight.status}</td>
              <td>${flight.baseEconomyPrice}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default FlightList;