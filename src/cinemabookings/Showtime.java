package cinemabookings;
// NG: class used to create Showtime objects, based on showtimes.txt
public class Showtime {
    int id; // NG: unique identifier for showtime
    int iTime; // NG: showtime start time in minutes
    int[] iSeats; // NG: showtime seat status stats
    Booking selectedBooking; // NG: reference to Showtime's selected Booking object
    // NG: constructor
    public Showtime(int id, int time, int[] seats) {
        this.id = id; // NG: set id to match passed parameter
        iTime = time; // NG: set start time to match passed parameter
        iSeats = seats; // NG: set seat stats to match passed parameter
    } // NG: end of constructor
    // NG: getter method(s)
    public int getId() {return id;}
    public int getTime() {return iTime;}
    public int[] getSeats() {return iSeats;}
    public Booking getBooking() {return selectedBooking;}
    // NG: setter method(s)
    public void setBooking(Booking booking) {selectedBooking = booking;}
    // NG: format time from minutes integer to time string
    public String formatTime(int minutes) {
        int iHours = minutes / 60; // NG: convert minutes to hours
        if(iHours > 23) {iHours -= 24;} // NG: to display start times after midnight correctly
        int iMinutes = minutes % 60; // NG: get leftover minutes after the hours
        return String.format("%d:%02d", iHours, iMinutes); // NG: return time, formatted correctly
    } // NG: end of formatTime method
    // NG: count available seats in array
    public int countSeatsAvailable() {
        int iCount = 0; // NG: variable used for counting
        // NG: loop through all seats
        for(int i = 0; i < iSeats.length; i++) {
            // NG: if seat is empty, increment the count of available seats
            if(iSeats[i] == 0) {iCount++;}
        } return iCount; // NG: after loop, return count of available seats
    } // NG: end of countSeatsAvailable method    
} // NG: end of Showtime class