package cinemabookings;
// NG: libraries
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
// NG: main class, from which the program is run and the GUI is built
final class Cinema {
    ArrayList<Movie> listMovies = new ArrayList<>(); // NG: ArrayList of Movie objects, based on movies.txt
    Movie selectedMovie; // NG: reference to user-selected Movie object
    // NG: GUI variables
    JFrame frameShowtimes, frameTicketSelector, frameSeatSelector;
    JLabel lblPriceTicketTotal, lblTicketSelection, lblPricePremier, lblPriceTotal;
    JProgressBar progSeatSelection;
    ButtonDefault btnProceed, btnComplete;
    // NG: constants
    static final Color COLOR_PURPLE             = new Color( 40,   150, 190);
    static final Color COLOR_PURPLE_LIGHT       = new Color( 90,  45, 135);
    static final Color COLOR_PURPLE_DARK        = new Color( 40,   0,  80);
    static final Color COLOR_RED                = new Color(100,   0,   0);
    static final Color COLOR_GREEN              = new Color(  0, 100,  25);
    static final Color COLOR_PINK               = new Color(255, 100, 100);
    static final String FONT_DEFAULT            = "Rockwell";
    static final int OPENING_TIME               = 540; // NG: cinema opening time, in minutes (e.g. 540 = 9am)
    public static final double PRICE_ADULT      = 8;
    public static final double PRICE_CHILD      = 4.25;
    public static final double PRICE_SENIOR     = 5;
    public static final double PRICE_STUDENT    = 5.5;
    static final double PRICE_PREMIER           = 6; // NG: premier ticket price add-on
    // NG: constructor
    public Cinema() {
        createObjects(); // NG: call method to create Movie and Showtime objects
        openShowtimes(); // NG: call method to display first GUI frame
    } // NG: end of constructor
    // NG: read text files to create Movie and Showtime objects
    void createObjects() {
        String sLine; // NG: variable used to loop through text file lines
        // NG: import movies text file to get movie name, runtime, and certificate
        try( BufferedReader readMovie = new BufferedReader( new FileReader("data/movies.txt") ) ) {
            // NG: loop through text file
            while( ( sLine = readMovie.readLine() ) != null) {
                String[] sMovie = sLine.split(","); // NG: store movie from current line in holding array
                // NG: create new Movie object and add to Movie ArrayList
                listMovies.add( new Movie(sMovie[0], Integer.parseInt(sMovie[1]), sMovie[2]) );
            } // NG: end of while loop
            readMovie.close(); // NG: close reader stream
        } catch (IOException e) {System.out.println("ERROR: movies.txt can't be found!\n");}
        // NG: loop through all Movie objects
        for(Movie listMovie : listMovies) {
            // NG: import showtimes text file
            try( BufferedReader readShowtime = new BufferedReader( new FileReader("data/showtimes.txt") ) ) {
                // NG: set default start time for movie to match cinema opening time + 30 minutes
                int iTime = OPENING_TIME + 30; 
                // NG: loop through text file
                while( ( sLine = readShowtime.readLine() ) != null) {
                    String[] sShowtime = sLine.split(","); // NG: store showtime from current line in holding array
                    int[] iSeats = new int[84]; // NG: create new array to store 84 seat stats as integers
                    // NG: loop through holding array to populate seats array
                    for(int i = 2; i < sShowtime.length; i++) {iSeats[i-2] = Integer.parseInt(sShowtime[i]);}
                    // NG: if showtime record corresponds to current movie then add Showtime object to Movie object
                    if( sShowtime[1].equals( listMovie.getTitle() ) )  {
                        // NG: add Showtime, passing id, start time, and seat status stats as parameters
                        listMovie.getShowtimes().add( new Showtime(Integer.parseInt(sShowtime[0]), iTime, iSeats) );
                        // NG: add runtime plus 30% onto iTime for next potential loop iteration
                        iTime += (int)(double)listMovie.getRuntime()*1.3;
                    } // NG: end of if statement
                } // NG: end of while loop
                readShowtime.close(); // NG: close reader stream
            } catch (IOException e) {System.out.println("ERROR: showtimes.txt can't be found!\n");}
        } // NG: end of for loop
    } // NG: end of createObjects method
    // NG: create showtimes GUI frame
    void openShowtimes() {
        // NG: create frame and set title
        frameShowtimes = new JFrame();
        frameShowtimes.setTitle(" Sakshi cinema booking");
        // NG: create main scrollable panel
        JPanel pnlMain = new JPanel( new BorderLayout() );
        pnlMain.setBackground(COLOR_PURPLE);
        JScrollPane scrollMain = new JScrollPane(pnlMain, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollMain.setPreferredSize( new Dimension(1080, 900) );
        frameShowtimes.add(scrollMain);
        // NG: create app header image in label
        JPanel pnlHeader = new JPanel();
        pnlHeader.setOpaque(false);
        JLabel lblHeader = new JLabel();
        lblHeader.setIcon( new ImageIcon( new ImageIcon("images/header/cinema.png").getImage().getScaledInstance(350, -1, Image.SCALE_SMOOTH) ) );
        pnlHeader.add(lblHeader);
        pnlMain.add(pnlHeader, BorderLayout.PAGE_START);
        // NG: create grid panel to contain movie showtimes
        JPanel pnlShowtimes = new JPanel( new GridLayout(0, 1) );
        pnlShowtimes.setOpaque(false);
        pnlMain.add(pnlShowtimes, BorderLayout.CENTER);
        // NG: loop through all Movie objects
        for(Movie listMovie : listMovies) {
            // NG: create new movie panel and add to gridlayout
            JPanel pnlMovie = new JPanel( new BorderLayout() );
            pnlMovie.setBorder( BorderFactory.createCompoundBorder(new EmptyBorder(0, 10, 25, 10), new TitledBorder(
                    new LineBorder(Color.WHITE, 3, true),
                    " " + listMovie.getTitle() + " ",
                    2,
                    0,
                    new Font(FONT_DEFAULT, Font.BOLD, 32),
                    Color.WHITE
            ) ) ); // NG: end of setBorder method on movie panel
            pnlMovie.setOpaque(false);
            pnlShowtimes.add(pnlMovie);
            // NG&WS: create movie poster panel in left of movie borderlayout
            JPanel pnlPoster = new JPanel();
            pnlPoster.setBorder( BorderFactory.createCompoundBorder(new EmptyBorder(5, 5, 5, 5), new LineBorder(COLOR_PURPLE_LIGHT, 2, true) ) );
            pnlPoster.setOpaque(false);
            pnlMovie.add(pnlPoster, BorderLayout.LINE_START);
            // NG&WS: create movie poster label containing image, then add to poster panel
            JLabel lblPoster = new JLabel();
            lblPoster.setIcon( new ImageIcon( new ImageIcon("images/posters/" + listMovie.getTitle() + ".jpg").getImage().getScaledInstance(195, 285, Image.SCALE_SMOOTH) ) );
            pnlPoster.add(lblPoster);
            // NG: create panel to contain most movie info in center of movie borderlayout
            JPanel pnlCenter = new JPanel( new BorderLayout() );
            pnlCenter.setOpaque(false);
            pnlMovie.add(pnlCenter, BorderLayout.CENTER);
            // NG&WS: create movie showtimes panel in center of central borderlayout
            JPanel pnlTimes = new JPanel( new FlowLayout (FlowLayout.CENTER, 10, 10) );
            pnlTimes.setPreferredSize( new Dimension(0, 0) );
            pnlTimes.setBorder( BorderFactory.createCompoundBorder(new EmptyBorder(5, 5, 5, 5), new LineBorder(COLOR_PURPLE_LIGHT, 2, true) ) );
            pnlTimes.setOpaque(false);
            pnlCenter.add(pnlTimes, BorderLayout.CENTER);
            // NG: loop through all Showtime objects
            for( Showtime listShowtime : listMovie.getShowtimes() ) {
                // NG: if showtime has seats available, proceed to create showtime button
                if(listShowtime.countSeatsAvailable() > 0) {
                    ButtonDefault btnShowtime = new ButtonDefault( listShowtime.formatTime( listShowtime.getTime() ) );
                    btnShowtime.setPreferredSize( new Dimension(120, 65) );
                    btnShowtime.setFont(new Font(FONT_DEFAULT, Font.BOLD, 19));
                    pnlTimes.add(btnShowtime);
                    // NG: pass current Movie and Showtime objects to listener object
                    btnShowtime.addActionListener( new ShowtimeListener(listMovie, listShowtime) );
                } // NG: end of if statement
            } // NG: end of for loop
            // NG: create panel in footer of central borderlayout for runtime and certificate
            JPanel pnlFooter = new JPanel( new BorderLayout() );
            pnlFooter.setBorder( BorderFactory.createCompoundBorder(new EmptyBorder(5, 5, 5, 5), new LineBorder(COLOR_PURPLE_LIGHT, 2, true) ) );
            pnlFooter.setOpaque(false);
            pnlCenter.add(pnlFooter, BorderLayout.PAGE_END);
            // NG: create panel in left of footer panel for transparent filler image
            JPanel pnlFiller = new JPanel();
            pnlFiller.setOpaque(false);
            pnlFooter.add(pnlFiller, BorderLayout.LINE_START);
            // NG: create filler image in filler panel
            JLabel lblFiller = new JLabel();
            lblFiller.setIcon( new ImageIcon( new ImageIcon("images/certs/filler.png").getImage() ) );
            pnlFiller.add(lblFiller);
            // NG: create panel in center of footer panel for runtime
            JPanel pnlRuntime = new JPanel( new CardLayout() );
            pnlRuntime.setOpaque(false);
            pnlFooter.add(pnlRuntime, BorderLayout.CENTER);
            // NG: create runtime label in runtime panel
            JLabel lblRuntime = new JLabel("runtime: " + listMovie.getRuntime() + " minutes", SwingConstants.CENTER);
            lblRuntime.setFont(new Font(FONT_DEFAULT, Font.PLAIN, 15));
            lblRuntime.setForeground(Color.WHITE);
            pnlRuntime.add(lblRuntime);
            // NG: create panel in right of footer panel for certificate
            JPanel pnlCert = new JPanel();
            pnlCert.setOpaque(false);
            pnlFooter.add(pnlCert, BorderLayout.LINE_END);
            // NG: create certificate image in certificate panel
            JLabel lblCert = new JLabel();
            lblCert.setIcon( new ImageIcon( new ImageIcon("images/certs/" + listMovie.getCertificate() + ".png").getImage() ) );
            pnlCert.add(lblCert);
        } // NG: end of for loop
        // NG: set frame behaviour
        frameShowtimes.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // NG: set program to exit upon frame closing
        frameShowtimes.pack(); // NG: pack frame to ensure proper sizing and spacing
        frameShowtimes.setLocationRelativeTo(null); // NG: center frame on display
        frameShowtimes.setVisible(true); // NG: set frame to be visible
    } // NG: end of openShowtimes method
    // NG: create ticket selection frame
    void openTicketSelector() {
        // NG: create frame and set title and size
        frameTicketSelector = new JFrame();
        frameTicketSelector.setTitle("Ticket Selection -- Cinéma Saint-Ford");
        frameTicketSelector.setPreferredSize( new Dimension(750, 850) );
        // NG: create border panel
        JPanel pnlBorder = new JPanel( new BorderLayout() );
        pnlBorder.setBorder( new LineBorder(Color.WHITE, 8, false) );
        pnlBorder.setBackground(COLOR_PURPLE);
        frameTicketSelector.add(pnlBorder);
        // NG: create main panel
        JPanel pnlMain = new JPanel( new BorderLayout() );
        pnlMain.setBorder( new LineBorder(COLOR_PURPLE, 15, false) );
        pnlMain.setOpaque(false);
        pnlBorder.add(pnlMain, BorderLayout.CENTER);
        // NG: create title panel that'll contain title and poster
        JPanel pnlTitle = new JPanel( new BorderLayout() );
        pnlTitle.setOpaque(false);
        pnlMain.add(pnlTitle, BorderLayout.PAGE_START);
        // NG: create label for movie title and showtime
        JLabel lblTitle = new JLabel(selectedMovie.getTitle() + " @ " + selectedMovie.getShowtime().formatTime( selectedMovie.getShowtime().getTime() ), SwingConstants.CENTER);
        lblTitle.setFont( new Font(FONT_DEFAULT, Font.BOLD, 28) );
        lblTitle.setForeground(Color.WHITE);
        pnlTitle.add(lblTitle, BorderLayout.PAGE_START);
        // NG: create movie poster panel in center of title borderlayout
        JPanel pnlPoster = new JPanel();
        pnlPoster.setOpaque(false);
        pnlTitle.add(pnlPoster, BorderLayout.CENTER);
        // NG: create movie poster label containing image, then add to poster panel
        JLabel lblPoster = new JLabel("", SwingConstants.CENTER);
        lblPoster.setIcon( new ImageIcon( new ImageIcon("images/posters/" + selectedMovie.getTitle() + ".jpg").getImage().getScaledInstance(240, 350, Image.SCALE_SMOOTH) ) );
        lblPoster.setBorder( BorderFactory.createCompoundBorder(
            new EmptyBorder(25, 0, 0, 0),
            BorderFactory.createCompoundBorder( new LineBorder(COLOR_PURPLE_LIGHT, 2, true), new EmptyBorder(5, 5, 5, 5) )
        ) );
        pnlPoster.add(lblPoster);
        // NG: create central panel containing content
        JPanel pnlCenter = new JPanel( new GridLayout(0, 5, 25, 5) );
        pnlCenter.setOpaque(false);
        pnlMain.add(pnlCenter, BorderLayout.CENTER);
        // NG: create empty grid row
        pnlCenter.add( new JLabel() );
        pnlCenter.add( new JLabel() );
        pnlCenter.add( new JLabel() );
        pnlCenter.add( new JLabel() );
        pnlCenter.add( new JLabel() );
        // NG: create combo boxes and accompanying labels, passing panel, Strings, and price constants
        buildTicketBox(pnlCenter, "Adult", PRICE_ADULT);
        if( !selectedMovie.getCertificate().equals("15") && !selectedMovie.getCertificate().equals("18") ) {buildTicketBox(pnlCenter, "Child", PRICE_CHILD);}
        buildTicketBox(pnlCenter, "Senior", PRICE_SENIOR);
        buildTicketBox(pnlCenter, "Student", PRICE_STUDENT);
        // NG: create grid line for total ticket price
        pnlCenter.add( new JLabel() );
        pnlCenter.add( new JLabel() );
        lblPriceTicketTotal = new JLabel("£0.00", SwingConstants.CENTER);
        lblPriceTicketTotal.setFont( new Font(FONT_DEFAULT, Font.BOLD, 18) );
        lblPriceTicketTotal.setForeground(Color.WHITE);
        pnlCenter.add(lblPriceTicketTotal);
        // NG: create bottom panel containing seat selection label and buttons
        JPanel pnlFooter = new JPanel( new GridLayout(0, 1, 0, 5) );
        pnlFooter.setOpaque(false);
        pnlMain.add(pnlFooter, BorderLayout.PAGE_END);
        // NG: set seat selection label text and appearance
        lblTicketSelection = new JLabel(selectedMovie.getShowtime().countSeatsAvailable() + " tickets available  |  " + selectedMovie.getShowtime().getBooking().sumTicketQuantity() + " tickets selected", SwingConstants.CENTER);
        lblTicketSelection.setFont( new Font(FONT_DEFAULT, Font.PLAIN, 14) );
        lblTicketSelection.setForeground(Color.WHITE);
        pnlFooter.add(lblTicketSelection);
        // NG: create proceed button to go to next frame
        btnProceed = new ButtonDefault("Proceed to Seat Selection");
        JRootPane rootPane = frameTicketSelector.getRootPane();
        rootPane.setDefaultButton(btnProceed); // NG: set as default button
        btnProceed.addActionListener( new ButtonListener() ); // NG: pass current Movie and Showtime objects to button listener
        btnProceed.setEnabled(false); // NG: disable button by default
        pnlFooter.add(btnProceed);
        // NG: create cancel button to return to showtimes frame
        ButtonDefault btnCancel = new ButtonDefault("Return to Showtimes");
        btnCancel.addActionListener( new ButtonListener() );
        pnlFooter.add(btnCancel);
        // NG: set frame behaviour
        frameTicketSelector.setUndecorated(true); // NG: remove frame title bar
        frameTicketSelector.getRootPane().setWindowDecorationStyle(JRootPane.NONE); // NG: remove frame title bar
        frameTicketSelector.pack(); // NG: pack frame to ensure proper sizing and spacing
        frameTicketSelector.setLocationRelativeTo(null); // NG: center frame on display
        frameTicketSelector.setVisible(true); // NG: set frame to be visible
    } // NG: end of openTicketSelector method
    // NG: create seat selection frame
    void openSeatSelector() {
        selectedMovie.getShowtime().getBooking().getSelectedSeats().clear(); // NG: set number of selected seats in ArrayList to 0 by default
        selectedMovie.getShowtime().getBooking().setPremierCost(0); // NG: set premeir ticket cost to 0 by default
        selectedMovie.getShowtime().getBooking().setTotalCost( selectedMovie.getShowtime().getBooking().sumTicketCost() ); // NG: set total price to match base ticket price total by default
        // NG: create frame and set title and size
        frameSeatSelector = new JFrame();
        frameSeatSelector.setTitle("Seat Selection -- Cinéma Saint-Ford");
        frameSeatSelector.setPreferredSize( new Dimension(750, 850) );
        // NG: create border panel
        JPanel pnlBorder = new JPanel( new BorderLayout() );
        pnlBorder.setBorder( new LineBorder(Color.WHITE, 8, false) );
        pnlBorder.setBackground(COLOR_PURPLE);
        frameSeatSelector.add(pnlBorder);
        // NG: create main panel
        JPanel pnlMain = new JPanel( new BorderLayout() );
        pnlMain.setBorder( new LineBorder(COLOR_PURPLE, 15, false) );
        pnlMain.setOpaque(false);
        pnlBorder.add(pnlMain, BorderLayout.CENTER);
        // NG: create label for movie title and showtime
        JLabel lblTitle = new JLabel(selectedMovie.getTitle() + " @ " + selectedMovie.getShowtime().formatTime( selectedMovie.getShowtime().getTime() ), SwingConstants.CENTER);
        lblTitle.setFont( new Font(FONT_DEFAULT, Font.BOLD, 28) );
        lblTitle.setForeground(Color.WHITE);
        pnlMain.add(lblTitle, BorderLayout.PAGE_START);
        // NG: create central panel containing content
        JPanel pnlCenter = new JPanel( new BorderLayout() );
        pnlCenter.setBorder( BorderFactory.createCompoundBorder(new EmptyBorder(30, 50, 15, 50), new LineBorder(COLOR_PURPLE_LIGHT, 2, true) ) );
        pnlCenter.setOpaque(false);
        pnlMain.add(pnlCenter, BorderLayout.CENTER);
        // NG: create theatre panel containing seating
        JPanel pnlTheatre = new JPanel( new GridLayout(0, 16, 0, 0) );
        pnlTheatre.setBackground(COLOR_PURPLE_DARK);
        pnlCenter.add(pnlTheatre, BorderLayout.CENTER);
        // NG: create theatre seats
        for(int i = 0; i < 16; i++) {pnlTheatre.add( new JLabel() );} // NG: add 1 empty row
        // NG: add 5 rows of standard seating
        for(int i=0; i < 5; i++) {
            pnlTheatre.add( new JLabel() ); // NG: add empty space down left of theatre
            for(int j = 12*i+0; j < 12*i+3; j++) {buildSeatImage(pnlTheatre, j, selectedMovie.getShowtime().getSeats()[j], "standard");}
            pnlTheatre.add( new JLabel() ); // NG: add left aisle space
            for(int j = 12*i+3; j < 12*i+9; j++) {buildSeatImage(pnlTheatre, j, selectedMovie.getShowtime().getSeats()[j], "standard");}
            pnlTheatre.add( new JLabel() ); // NG: add right aisle space
            for(int j = 12*i+9; j < 12*i+12; j++) {buildSeatImage(pnlTheatre, j, selectedMovie.getShowtime().getSeats()[j], "standard");}
            pnlTheatre.add( new JLabel() ); // NG: add empty space down right of theatre
        } // NG: end of for loop
        for(int i = 0; i < 16; i++) {pnlTheatre.add( new JLabel() );} // NG: add 1 empty row
        // NG: add 1 row of accessbility seating
        pnlTheatre.add( new JLabel() ); // NG: add empty space down left of theatre
        for(int j = 60; j < 63; j++) {buildSeatImage(pnlTheatre, j, selectedMovie.getShowtime().getSeats()[j], "access");}
        pnlTheatre.add( new JLabel() ); // NG: add left aisle space
        for(int j = 63; j < 69; j++) {buildSeatImage(pnlTheatre, j, selectedMovie.getShowtime().getSeats()[j], "access");}
        pnlTheatre.add( new JLabel() ); // NG: add right aisle space
        for(int j = 69; j < 72; j++) {buildSeatImage(pnlTheatre, j, selectedMovie.getShowtime().getSeats()[j], "access");}
        pnlTheatre.add( new JLabel() ); // NG: add empty space down right of theatre
        for(int i = 0; i < 16; i++) {pnlTheatre.add( new JLabel() );} // NG: add 1 empty row
        // NG: add 2 rows of premier seating
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 5; j++) {pnlTheatre.add( new JLabel() );} // NG: add empty spaces in left section
            for(int j = 6*i+72; j < 6*i+78; j++) {buildSeatImage(pnlTheatre, j, selectedMovie.getShowtime().getSeats()[j], "premier");}
            for(int j = 0; j < 5; j++) {pnlTheatre.add( new JLabel() );} // NG: add empty spaces in right section
        } // NG: end of for loop
        pnlTheatre.add( new JLabel() ); // NG: add 1 empty cell to create 1 empty row
        // NG: create seat selection progress bar
        progSeatSelection = new JProgressBar( 0, selectedMovie.getShowtime().getBooking().sumTicketQuantity() );
        progSeatSelection.setStringPainted(true);
        progSeatSelection.setString("0/" + selectedMovie.getShowtime().getBooking().sumTicketQuantity() + " seats selected");
        progSeatSelection.setBackground(COLOR_PURPLE_DARK);
        progSeatSelection.setForeground(COLOR_RED);
        progSeatSelection.setBorderPainted(false);
        pnlCenter.add(progSeatSelection, BorderLayout.PAGE_END);
        // NG: create bottom panel containing seat info label and buttons
        JPanel pnlFooter = new JPanel( new GridLayout(0, 1, 0, 5) );
        pnlFooter.setOpaque(false);
        pnlMain.add(pnlFooter, BorderLayout.PAGE_END);
        // NG: create panel containing base price labels
        JPanel pnlPriceBase = new JPanel( new GridLayout(0, 2, 10, 0) );
        pnlPriceBase.setOpaque(false);
        pnlFooter.add(pnlPriceBase);
        // NG: create base price labels
        JLabel lblPriceBase = new JLabel("£" + String.format("%.2f", selectedMovie.getShowtime().getBooking().sumTicketCost() ), SwingConstants.RIGHT);
        lblPriceBase.setFont( new Font(FONT_DEFAULT, Font.PLAIN, 18) );
        lblPriceBase.setForeground(Color.WHITE);
        pnlPriceBase.add(lblPriceBase);
        JLabel lblPriceBaseRight = new JLabel("base price", SwingConstants.LEFT);
        lblPriceBaseRight.setFont( new Font(FONT_DEFAULT, Font.PLAIN, 18) );
        lblPriceBaseRight.setForeground(Color.WHITE);
        pnlPriceBase.add(lblPriceBaseRight);
        // NG: create panel containing premier seating price labels
        JPanel pnlPricePremier = new JPanel( new GridLayout(0, 2, 10, 0) );
        pnlPricePremier.setOpaque(false);
        pnlFooter.add(pnlPricePremier);
        // NG: create premier seating price labels
        lblPricePremier = new JLabel("£0.00", SwingConstants.RIGHT);
        lblPricePremier.setFont( new Font(FONT_DEFAULT, Font.PLAIN, 18) );
        lblPricePremier.setForeground(Color.WHITE);
        pnlPricePremier.add(lblPricePremier);
        JLabel lblPricePremierRight = new JLabel("premier seating", SwingConstants.LEFT);
        lblPricePremierRight.setFont( new Font(FONT_DEFAULT, Font.PLAIN, 18) );
        lblPricePremierRight.setForeground(Color.WHITE);
        pnlPricePremier.add(lblPricePremierRight);
        // NG: create panel containing total price labels
        JPanel pnlPriceTotal = new JPanel( new GridLayout(0, 2, 10, 0) );
        pnlPriceTotal.setOpaque(false);
        pnlFooter.add(pnlPriceTotal);
        // NG: create total price labels
        lblPriceTotal = new JLabel("£" + String.format("%.2f", selectedMovie.getShowtime().getBooking().sumTicketCost() ), SwingConstants.RIGHT);
        lblPriceTotal.setFont( new Font(FONT_DEFAULT, Font.BOLD, 18) );
        lblPriceTotal.setForeground(Color.WHITE);
        pnlPriceTotal.add(lblPriceTotal);
        JLabel lblPriceTotalRight = new JLabel("TOTAL PRICE", SwingConstants.LEFT);
        lblPriceTotalRight.setFont( new Font(FONT_DEFAULT, Font.BOLD, 18) );
        lblPriceTotalRight.setForeground(Color.WHITE);
        pnlPriceTotal.add(lblPriceTotalRight);
        // NG: create button to complete booking
        btnComplete = new ButtonDefault("Complete Booking");
        JRootPane rootPane = frameSeatSelector.getRootPane();
        rootPane.setDefaultButton(btnComplete); // NG: set as default button
        btnComplete.addActionListener( new ButtonListener() );
        btnComplete.setEnabled(false); // NG: disable button by default
        pnlFooter.add(btnComplete);
        // NG: create cancel button to return to ticket selection frame
        ButtonDefault btnCancel = new ButtonDefault("Return to Ticket Selection");
        btnCancel.addActionListener( new ButtonListener() );
        pnlFooter.add(btnCancel);
        // NG: set frame behaviour
        frameSeatSelector.setUndecorated(true); // NG: remove frame title bar
        frameSeatSelector.getRootPane().setWindowDecorationStyle(JRootPane.NONE); // NG: remove frame title bar
        frameSeatSelector.pack(); // NG: pack frame to ensure proper sizing and spacing
        frameSeatSelector.setLocationRelativeTo(null); // NG: center frame on display
        frameSeatSelector.setVisible(true); // NG: set frame to be visible
    }  // NG: end of openSeatSelector method
    // NG: method for creating combo boxes on ticket selection frame
    void buildTicketBox(JPanel panel, String label, double price) {
        panel.add( new JLabel() ); // NG: add empty label in left-most grid column
        // NG: add label using text from method parameter
        JLabel lblTicket = new JLabel(label, SwingConstants.RIGHT);
        lblTicket.setFont( new Font(FONT_DEFAULT, Font.BOLD, 18) );
        lblTicket.setForeground(Color.WHITE);
        panel.add(lblTicket);
        JComboBox comboTicket = new JComboBox();
        // NG: center text in combo box
        ( (JLabel) comboTicket.getRenderer() ).setHorizontalAlignment(SwingConstants.CENTER); 
        // NG: add one available ticket to combo box for every seat available
        for(int i = 0; i < selectedMovie.getShowtime().countSeatsAvailable()+1; i++) {comboTicket.addItem(i);}
        comboTicket.setFont( new Font(FONT_DEFAULT, Font.BOLD, 18) );
        comboTicket.setBackground(COLOR_PURPLE_LIGHT);
        comboTicket.setForeground(Color.WHITE);
        comboTicket.setActionCommand(label); // NG: add action command from method parameter
        comboTicket.addActionListener( new TicketListener() );
        panel.add(comboTicket);
        // NG: add price from method parameter
        JLabel lblTicketPrice = new JLabel("£" + String.format("%.2f", price), SwingConstants.LEFT);
        lblTicketPrice.setFont( new Font(FONT_DEFAULT, Font.BOLD, 18) );
        lblTicketPrice.setForeground(Color.WHITE);
        panel.add(lblTicketPrice);
        panel.add( new JLabel() ); // NG: add empty label in right-most grid column
    } // NG: end of buildTicketBox method
    // NG: method for setting seats on seat selection frame
    void buildSeatImage(JPanel panel, int seat, int occupied, String type) {
        // NG: if seat is occupied add button with listener; otherwise add label
        if(occupied == 0) {
            JButton btnSeat = new JButton();
            btnSeat.setIcon( new ImageIcon( new ImageIcon("images/seats/" + type + ".png").getImage() ) );
            btnSeat.setBackground(COLOR_PURPLE);
            btnSeat.setBorder( BorderFactory.createEmptyBorder() );
            btnSeat.setCursor( new Cursor(Cursor.HAND_CURSOR) );
            btnSeat.setFocusPainted(false);
            btnSeat.setBorderPainted(false);
            btnSeat.setContentAreaFilled(false);
            btnSeat.setActionCommand(type); // NG: pass seat type to button's action command
            btnSeat.addActionListener( new SeatListener(seat) ); // NG: pass seat number to listener object
            btnSeat.addMouseListener( new SeatHover() ); // NG: sdd seat hover effect to seat button
            panel.add(btnSeat);
        }  else if(occupied == 1) {
            JLabel lblSeat = new JLabel("", SwingConstants.CENTER);
            lblSeat.setIcon( new ImageIcon( new ImageIcon("images/seats/occupied.png").getImage() ) );
            panel.add(lblSeat);
        } // NG: end of if-else statement
    } // NG: end of buildSeatImage method
    // NG: class to set default button appearance
    class ButtonDefault extends JButton {
        // NG: constructor
        public ButtonDefault(String text) {
            super.setText(text);
            super.setPreferredSize( new Dimension(-1, 40) );
            super.setBorder( new LineBorder(Color.WHITE, 2, false) );
            super.setFont(new Font(FONT_DEFAULT, Font.BOLD, 14));
            super.setForeground(Color.WHITE);
            super.setOpaque(true);
            super.setBackground(COLOR_PURPLE_LIGHT);
            super.addMouseListener( new ButtonHover() ); // NG: add hover effect to button
        } // NG: end of constructor
        // NG: listener to create button hover highlight effect
        class ButtonHover extends MouseAdapter {
            @Override public void mouseEntered(MouseEvent e) {
                JButton btnEvent = (JButton) e.getSource();
                btnEvent.setBackground(Color.BLACK);
                btnEvent.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } // NG: end of mouseEntered method
            @Override public void mouseExited(MouseEvent e) {
                JButton btnEvent = (JButton) e.getSource();
                btnEvent.setBackground(COLOR_PURPLE_LIGHT);
            } // NG: end of mouseExited method
        } // NG: end of ButtonHover class
    } // NG: end of ButtonDefault class
    // NG: listener to create seat hover highlight effect
    class SeatHover extends MouseAdapter {
        // NG: event to set seat images on mouse enter
        @Override public void mouseEntered(MouseEvent e) {
            JButton btnEvent = (JButton) e.getSource();
            // NG: if seat hasn't been selected, change icon to "hover" icon
            // NG: otherwise if seat HAS been selected, change icon to "deselect" icon and change action command
            if( selectedMovie.getShowtime().getBooking().getSelectedSeats().size() < selectedMovie.getShowtime().getBooking().sumTicketQuantity() && ( btnEvent.getActionCommand().equals("standard") || btnEvent.getActionCommand().equals("access") || btnEvent.getActionCommand().equals("premier") ) ) {
                btnEvent.setIcon( new ImageIcon( new ImageIcon("images/seats/hover.png").getImage() ) );
            } else if( btnEvent.getActionCommand().equals("standard_selected") ) {
                btnEvent.setIcon( new ImageIcon( new ImageIcon("images/seats/deselect.png").getImage() ) );
                btnEvent.setActionCommand("standard_deselect");
            } else if( btnEvent.getActionCommand().equals("access_selected") ) {
                btnEvent.setIcon( new ImageIcon( new ImageIcon("images/seats/deselect.png").getImage() ) );
                btnEvent.setActionCommand("access_deselect");
            } else if( btnEvent.getActionCommand().equals("premier_selected") ) {
                btnEvent.setIcon( new ImageIcon( new ImageIcon("images/seats/deselect.png").getImage() ) );
                btnEvent.setActionCommand("premier_deselect");
            } // NG: end of if-else statement
        } // NG: end of mouseEntered method
        // NG: event to set seat images on mouse exit
        @Override public void mouseExited(MouseEvent e) {
            JButton btnEvent = (JButton) e.getSource();
            // NG: if seat hasn't been selected, change icon back to default
            // NG: otherwise if seat HAS been selected, change icon to "selected" icon and change action command
            if( btnEvent.getActionCommand().equals("standard") || btnEvent.getActionCommand().equals("access") || btnEvent.getActionCommand().equals("premier") ) {
                btnEvent.setIcon( new ImageIcon( new ImageIcon("images/seats/" + btnEvent.getActionCommand() + ".png").getImage() ) );
            } else if( btnEvent.getActionCommand().equals("standard_deselect") ) {
                btnEvent.setIcon( new ImageIcon( new ImageIcon("images/seats/selected.png").getImage() ) );
                btnEvent.setActionCommand("standard_selected");
            } else if( btnEvent.getActionCommand().equals("access_deselect") ) {
                btnEvent.setIcon( new ImageIcon( new ImageIcon("images/seats/selected.png").getImage() ) );
                btnEvent.setActionCommand("access_selected");
            } else if( btnEvent.getActionCommand().equals("premier_deselect") ) {
                btnEvent.setIcon( new ImageIcon( new ImageIcon("images/seats/selected.png").getImage() ) );
                btnEvent.setActionCommand("premier_selected");
            } // NG: end of if-else statement
        } // NG: end of mouseExited method
    } // NG: end of SeatHover class
    // NG: default button listener class
    class ButtonListener implements ActionListener {
        // NG: action event
        @Override public void actionPerformed(ActionEvent e) {
            JButton btnEvent = (JButton) e.getSource(); // NG: get button that was clicked
            // NG: check text on clicked button to determine action
            switch( btnEvent.getText() ) {
                case "Proceed to Seat Selection": // NG: proceed from ticket selection frame to seat selection frame
                    frameTicketSelector.setEnabled(false); // NG: disable ticket selection frame
                    frameTicketSelector.setVisible(false); // NG: set ticket selection frame as invisible
                    openSeatSelector(); // NG: open seat selection frame
                    break;
                case "Complete Booking": btnEvent.setText("Are you sure? Click again to go to payment..."); break;
                case "Are you sure? Click again to go to payment...": // NG: complete booking and return to program start
                    // NG: write new Booking object to bookings text file
                    try {selectedMovie.getShowtime().getBooking().writeBooking( selectedMovie.getShowtime() );}
                    catch (IOException ex) {Logger.getLogger(Cinema.class.getName()).log(Level.SEVERE, null, ex);}
                    // NG: fill selected seats in showtimes text file, to be re-read later
                    try {selectedMovie.getShowtime().getBooking().writeSeats( selectedMovie.getShowtime() );}
                    catch (IOException ex) {Logger.getLogger(Cinema.class.getName()).log(Level.SEVERE, null, ex);}
                    // NG: create new Cinema object to start anew, and dispose all three previous frames
                    new Cinema();
                    frameShowtimes.dispose();
                    frameTicketSelector.dispose();
                    frameSeatSelector.dispose();
                    break;
                case "Return to Showtimes": // NG: close ticket selection frame and return to showtimes frame
                    frameTicketSelector.dispose(); // NG: close current frame
                    frameShowtimes.toFront(); // NG: bring parent frame to front of screen
                    frameShowtimes.setEnabled(true); // NG: enable parent frame
                    frameShowtimes.setVisible(true); // NG: set parent frame as visible
                    break;
                case "Return to Ticket Selection": // NG: close seat selection frame and return to ticket selection frame
                    frameSeatSelector.dispose(); // NG: close current frame
                    frameShowtimes.toFront(); // NG: bring parent's parent frame to front of screen
                    frameTicketSelector.toFront(); // NG: bring parent frame to front of screen
                    frameTicketSelector.setEnabled(true); // NG: enable parent frame
                    frameTicketSelector.setVisible(true); // NG: set parent frame as visible
                    break;
            } // NG: end of switch statement
        } // NG: end of actionPerformed method
    } // NG: end of ButtonListener class
    // NG&WS: listener to open the ticket selection screen
    class ShowtimeListener implements ActionListener {
        Movie movie; // NG: inner class variable to store selected Movie object
        Showtime showtime; // NG: inner class variable to store selected Showtime object
        // NG: constructor
        public ShowtimeListener(Movie movie, Showtime showtime) {
            this.movie = movie; // NG: set inner class variable to match passed Movie object
            this.showtime = showtime; // NG: set inner class variable to match passed Showtime object
        } // NG: end of constructor
        // NG: action event calls method to open ticket selection frame
        @Override public void actionPerformed(ActionEvent e) {
            selectedMovie = movie; // NG: set selected movie variable to match selected Movie object
            showtime.setBooking( new Booking() ); // NG: create new Booking object inside selected Showtime object
            selectedMovie.setShowtime(showtime); // NG: set selected Movie object's selected Showtime object to match inner class showtime variable
            frameShowtimes.setEnabled(false); // NG: disable showtimes frame
            frameShowtimes.setVisible(false); // NG: set showtimes frame as invisible
            openTicketSelector(); // NG: open ticket selection frame
        } // NG: end of actionPerformed method
    } // NG: end of ShowtimeListener class
    // NG: listener for ticket selection combo box changes
    class TicketListener implements ActionListener {
        // NG: action event sets state of labels and buttons based on combo box selections
        @Override public void actionPerformed(ActionEvent e) {
            JComboBox comboEvent = (JComboBox) e.getSource(); // NG: get combo box that was changed
            // NG: get action command of combo box that was changed to change Booking object's ticket quantity
            selectedMovie.getShowtime().getBooking().setTicketQuantity(
                    comboEvent.getActionCommand(),
                    Integer.parseInt( comboEvent.getSelectedItem().toString() )
            ); // NG: end of setTicketQuantity method call
            // NG: calculate Booking object's total ticket cost and update label text
            lblPriceTicketTotal.setText( "£" + String.format( "%.2f", selectedMovie.getShowtime().getBooking().sumTicketCost() ) );
            // NG: if too many tickets are selected, update label with count of selected tickets, and disable proceed button
            if( selectedMovie.getShowtime().getBooking().sumTicketQuantity() > selectedMovie.getShowtime().countSeatsAvailable() ) {
                lblTicketSelection.setText(
                    selectedMovie.getShowtime().countSeatsAvailable() + " tickets available  |  "
                    + selectedMovie.getShowtime().getBooking().sumTicketQuantity() + " tickets selected"
                ); lblTicketSelection.setForeground(COLOR_PINK);
                btnProceed.setEnabled(false);
            // NG: if no tickets are selected, update label with count of selected tickets, and disable proceed button
            } else if(selectedMovie.getShowtime().getBooking().sumTicketQuantity() == 0) {
                lblTicketSelection.setText(
                    selectedMovie.getShowtime().countSeatsAvailable() + " tickets available  |  "
                    + selectedMovie.getShowtime().getBooking().sumTicketQuantity() + " tickets selected"
                ); lblTicketSelection.setForeground(Color.WHITE);
                btnProceed.setEnabled(false);
            // NG: otherwise, update label with count of selected tickets, and enable proceed button
            } else {
                lblTicketSelection.setText(
                    selectedMovie.getShowtime().countSeatsAvailable() + " tickets available  |  "
                    + selectedMovie.getShowtime().getBooking().sumTicketQuantity() + " tickets selected"
                ); lblTicketSelection.setForeground(Color.WHITE);
                btnProceed.setEnabled(true);
            } // NG: end of if-else statement
        } // NG: end of actionPerformed method
    } // NG: end of TicketListener class
    // NG: listener to select seats
    class SeatListener implements ActionListener {
        int iSeat; // NG: variable to store selected seat
        // NG: constructor
        public SeatListener(int seat) {iSeat = seat;}
        // NG: action event to react to user's seat selection choices
        @Override public void actionPerformed(ActionEvent e) {
            JButton btnEvent = (JButton) e.getSource(); // NG: get button that was clicked
            // NG: if premier seat was seleted, increment Booking object's premier price and total price variables
            if( selectedMovie.getShowtime().getBooking().getSelectedSeats().size() < selectedMovie.getShowtime().getBooking().sumTicketQuantity() && btnEvent.getActionCommand().equals("premier") ) {
                selectedMovie.getShowtime().getBooking().setPremierCost(selectedMovie.getShowtime().getBooking().getPremierCost() + PRICE_PREMIER);
                selectedMovie.getShowtime().getBooking().setTotalCost(selectedMovie.getShowtime().getBooking().getTotalCost() + PRICE_PREMIER);
            // NG: if premier seat was DESELECTED, DECREMENT Booking object's premier price and total price variables
            } else if( btnEvent.getActionCommand().equals("premier_selected") || btnEvent.getActionCommand().equals("premier_deselect") ) {
                selectedMovie.getShowtime().getBooking().setPremierCost(selectedMovie.getShowtime().getBooking().getPremierCost() - PRICE_PREMIER);
                selectedMovie.getShowtime().getBooking().setTotalCost(selectedMovie.getShowtime().getBooking().getTotalCost() - PRICE_PREMIER);
            } // NG: end of if-else statement
            // NG: update values on price labels
            lblPricePremier.setText( "£" + String.format("%.2f", selectedMovie.getShowtime().getBooking().getPremierCost() ) );
            lblPriceTotal.setText( "£" + String.format("%.2f", selectedMovie.getShowtime().getBooking().getTotalCost() ) );
            // NG: if seat has been selected, change icon to "selected" icon, and add an element to the Booking object's seats ArrayList
            if( selectedMovie.getShowtime().getBooking().getSelectedSeats().size() < selectedMovie.getShowtime().getBooking().sumTicketQuantity() && ( btnEvent.getActionCommand().equals("standard") || btnEvent.getActionCommand().equals("access") || btnEvent.getActionCommand().equals("premier") ) ) {
                btnEvent.setIcon( new ImageIcon( new ImageIcon("images/seats/selected.png").getImage() ) );
                btnEvent.setActionCommand(btnEvent.getActionCommand() + "_selected");
                selectedMovie.getShowtime().getBooking().getSelectedSeats().add(iSeat);
            // NG: if seat has been DESELECTED, change icon back to default, and remove an element from the Booking object's seats ArrayList
            } else if( btnEvent.getActionCommand().equals("standard_selected") || btnEvent.getActionCommand().equals("standard_deselect") ) {
                btnEvent.setIcon( new ImageIcon( new ImageIcon("images/seats/standard.png").getImage() ) );
                btnEvent.setActionCommand("standard");
                selectedMovie.getShowtime().getBooking().getSelectedSeats().remove( new Integer(iSeat) );
            } else if( btnEvent.getActionCommand().equals("access_selected") || btnEvent.getActionCommand().equals("access_deselect") ) {
                btnEvent.setIcon( new ImageIcon( new ImageIcon("images/seats/access.png").getImage() ) );
                btnEvent.setActionCommand("access");
                selectedMovie.getShowtime().getBooking().getSelectedSeats().remove( new Integer(iSeat) );
            } else if( btnEvent.getActionCommand().equals("premier_selected") || btnEvent.getActionCommand().equals("premier_deselect") ) {
                btnEvent.setIcon( new ImageIcon( new ImageIcon("images/seats/premier.png").getImage() ) );
                btnEvent.setActionCommand("premier");
                selectedMovie.getShowtime().getBooking().getSelectedSeats().remove( new Integer(iSeat) );
            } // NG: end of if-else statement
            // NG: update the seat selection progress bar and label
            progSeatSelection.setValue( selectedMovie.getShowtime().getBooking().getSelectedSeats().size() );
            progSeatSelection.setString(
                selectedMovie.getShowtime().getBooking().getSelectedSeats().size() + "/"
                + selectedMovie.getShowtime().getBooking().sumTicketQuantity() + " seats selected"
            ); // NG: end of setString method call
            // NG: enable or disable final button based on whether all tickets have been allocated to seats
            if(selectedMovie.getShowtime().getBooking().getSelectedSeats().size() == selectedMovie.getShowtime().getBooking().sumTicketQuantity() ) {
                progSeatSelection.setForeground(COLOR_GREEN);
                btnComplete.setEnabled(true);
            } else {
                progSeatSelection.setForeground(COLOR_RED);
                btnComplete.setText("Complete Booking");
                btnComplete.setEnabled(false);
            } // NG: end of if-else statement
        } // NG: end of actionPerformed method
    } // NG: end of SeatListener class
    // NG: main method, from which the program is run
    public static void main(String[] args) {new Cinema();}
} // NG: end of Cinema class