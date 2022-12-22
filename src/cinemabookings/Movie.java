package cinemabookings;
import java.util.ArrayList; // NG: library
// NG: class used to create Movie objects, based on movies.txt
public class Movie {
    String sTitle; // NG: movie title
    int iRuntime; // NG: movie runtime
    String sCertificate; // NG: movie certificate
    ArrayList<Showtime> listShowtimes = new ArrayList<>(); // NG: ArrayList of Showtime objects, based on showtimes.txt
    Showtime selectedShowtime; // NG: reference to Movie's user-selected Showtime object
    // NG: constructor
    public Movie(String title, int runtime, String certificate) {
        sTitle = title; // NG: set movie title to match passed parameter
        iRuntime = runtime; // NG: set movie runtime to match passed parameter
        sCertificate = certificate; // NG: set movie certificate to match passed parameter
    } // NG: end of constructor
    // NG: getter method(s)
    public String getTitle() {return sTitle;}
    public int getRuntime() {return iRuntime;}
    public String getCertificate() {return sCertificate;}
    public ArrayList<Showtime> getShowtimes() {return listShowtimes;}
    public Showtime getShowtime() {return selectedShowtime;}
    // NG: setter method(s)
    public void setShowtime(Showtime showtime) {selectedShowtime = showtime;}
} // NG: end of Movie class