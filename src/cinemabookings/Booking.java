package cinemabookings;
import java.io.*; // NG: library
import java.util.*; // NG: library
// NG: class used to create Booking object, that gets saved to bookings.txt
public class Booking {
    int iTicketsAdult, iTicketsChild, iTicketsSenior, iTicketsStudent; // NG: counts of different ticket types
    double dPremierCost, dTotalCost; // NG: prices contained in booking
    ArrayList<Integer> listSelectedSeats = new ArrayList<>(); // NG: ArrayList of seats selected by user
    // NG: getter method(s)
    public double getPremierCost() {return dPremierCost;}
    public double getTotalCost() {return dTotalCost;}
    public ArrayList<Integer> getSelectedSeats() {return listSelectedSeats;}
    // NG: setter method(s)
    public void setPremierCost(double cost) {dPremierCost = cost;}
    public void setTotalCost(double cost) {dTotalCost = cost;}
    // NG: setter method for the multiple ticket variables
    public void setTicketQuantity(String ticket, int quantity) {
        // NG: check ticket parameter to determine which ticket quantity to set
        switch(ticket) {
            case "Adult": iTicketsAdult = quantity; break;
            case "Child": iTicketsChild = quantity; break;
            case "Senior": iTicketsSenior = quantity; break;
            case "Student": iTicketsStudent = quantity; break;
        } // NG: end of switch statement
    } // NG: end of setTicketQuantity method
    // NG: sum the ticket variables to return total number of selected tickets
    public int sumTicketQuantity() {return iTicketsAdult + iTicketsChild + iTicketsSenior + iTicketsStudent;}
    // NG: sum selected ticket quantities and prices to return total ticket cost
    public double sumTicketCost() {return Cinema.PRICE_ADULT*iTicketsAdult + Cinema.PRICE_CHILD*iTicketsChild + Cinema.PRICE_SENIOR*iTicketsSenior + Cinema.PRICE_STUDENT*iTicketsStudent;}
    // NG: write booking to bookings text file
    public void writeBooking(Showtime showtime) throws IOException {
        // NG: import bookings text file to write booking to file
        try( BufferedWriter writeBooking = new BufferedWriter( new FileWriter("data/bookings.txt", true) ) ) {
            // NG: write showtime id, cost, and ArrayList as comma-separated string
            writeBooking.write(
                    showtime.getId() + "," + String.format("%.2f", dTotalCost) + ","
                    + listSelectedSeats.toString().replace("[", "").replace("]", "").replace(" ", "")
            ); // NG: end of write method call
            writeBooking.newLine(); // NG: start new line in file
            writeBooking.close(); // NG: close writer stream
        } // NG: end of try statement
    } // NG: end of writeBooking method
    // NG: write filled seats to showtimes text file
    public void writeSeats(Showtime showtime) throws IOException {
        String sLine; // NG: variable used to loop through text file lines
        // NG: holding ArrayList for showtimes text file for it to be rewritten
        ArrayList<String> listLines = new ArrayList<>();
        // NG: import showtimes text file to search for line that needs editing
        try( BufferedReader readShowtime = new BufferedReader( new FileReader("data/showtimes.txt") ) ) {
            // NG: loop through text file to store each line in holding ArrayList
            while( ( sLine = readShowtime.readLine() ) != null) {listLines.add(sLine);}
            // NG: loop through holding ArrayList to edit showtime corresponding to current object
            for(int i = 0; i < listLines.size(); i++) {
                // NG: store seat status stats from current line in holding array
                String[] sSeats = listLines.get(i).split(",");
                // NG: if showtime id matches Booking object's Showtime object id, proceed to edit showtime seats stats
                if( sSeats[0].equals(showtime.getId()+"") ) {
                    // NG: loop through ArrayList of booked seats to edit holding array
                    for(Integer listSelectedSeat : listSelectedSeats) {sSeats[listSelectedSeat+2] = "1";}
                    listLines.set( i, Arrays.toString(sSeats).replace("[", "").replace("]", "").replace(", ", ",") );
                } // NG: end of if statement
            } // NG: end of for loop
            readShowtime.close(); // NG: close reader stream
        } catch (IOException e) {System.out.println("ERROR: showtimes.txt can't be found!\n");}
        // NG: import showtimes text file to empty it before re-writing it
        new BufferedWriter( new FileWriter("data/showtimes.txt") );
        // NG: import showtimes text file to write holding array back to file
        try( BufferedWriter writeShowtime = new BufferedWriter( new FileWriter("data/showtimes.txt", true) ) ) {
            // NG: loop through holding ArrayList to add lines back to file
            for(String alLine : listLines) {
                writeShowtime.write(alLine); // NG: write line back to file
                writeShowtime.newLine(); // NG: start new line in file
            } // NG: end of for loop
            writeShowtime.close(); // NG: close writer stream
        } // NG: end of try statement
    } // NG: end of fillSeats method
} // NG: end of Booking class