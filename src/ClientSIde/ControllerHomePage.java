package ClientSIde;

import Middleware.AirParcels;
import Middleware.GenericTableModel;
import Middleware.TableParcel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.TabExpander;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControllerHomePage extends JFrame {
    private JButton connectButton;
    private JLabel homepageStatusLabel;
    private JPanel mainPanel;
    private JTabbedPane MaintabbedPane1;
    private JPanel AirPortsPanel;
    private JPanel Active;
    private JTabbedPane airportOptionTab;
    private JPanel routesMainTab;
    private JTabbedPane routesOptionTab;
    private JButton viewAllAiportsButton;
    private JTextField apCountryTextf;
    private JTextField apCityTextf;
    private JPanel viewAirportsPanel;
    private JTable airportsTabTable;
    private JButton viewActiveRoutesButton;
    private JTextField airlineVal;
    private JButton viewAirlineRouteButton;
    private JPanel viewRoutesPanel;
    private JTable viewRoutesTable;
    private JTextField addRoutesAirlineTf;
    private JTextField addRsourceTf;
    private JTextField addRdstTf;
    private JTable table1;
    private JButton addRouteButton;
    private JTextField deleteRtAirlineTf;
    private JTextField delSrcApTf;
    private JTextField DelRtDstApTf;
    private JButton deleteRouteButton;
    private JLabel deleteStatusLabel;
    private JTextField addapNameText;
    private JTextField addapCityText;
    private JTextField addapCountryText;
    private JTextField addapCodeText;
    private JTextField addapOffsetText;
    private JButton btnAPadd;
    private JLabel addAirportStatLabel;
    private JTextField apDelname;
    private JButton deleteButton;
    private JLabel deleteStatLabel;
    private JTextField apCountry;
    private JTable viewAirlinesAPTable;
    private JButton viewButton;
    private JPanel addAirportPanel;
    private JPanel deleteAirportPanel;
    private JPanel airlinesRouteSearchPanel;
    private JPanel addnewRoute;
    private JPanel deleteRoutesPanel;
    private JButton btnAPview;
    private JTextField addapICAOText;
    private JTextField addapLatitudeText;
    private JTextField addapLongitudeText;
    private JTextField addapAltitudeText;
    private JTextField addapDSTText;
    private JTextField addapTZoneText;
    private JTextField apCity;
    private JScrollPane ViewAirportTable;
    private JLabel viewairportStatlabel;
    private JLabel addairportStatLabel;
    private JLabel airlineAirportLabel;
    private JLabel viewRoutesLabel;
    private JLabel addRoutesLabel;
    private JPanel ViewAirlinesTruRoutes;
    private JTextField srcAirportName;
    private JTextField destiAirportName;
    private JButton view;
    private JTable viewRArlineTable;
    private JLabel airlineRLabel;

    // object used in the client class and server communication
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;


    private Socket socket;

    public ControllerHomePage(String Title) {
        super(Title);
        intializeGUI();
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initialconnection();
            }
        });
        viewAllAiportsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAllAirport();
            }
        });
        btnAPview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                while(!apCountryTextf.getText().isBlank() || !apCityTextf.getText().isBlank()){
                    ViewAirPortUserinput();
                    apCountryTextf.setText("");
                    apCityTextf.setText("");
                }

//                else {
//                viewairportStatlabel.setText("you must provide a country atleast to be able to view the airports");

////                    viewCityCountryAirports();viewCityCountryAirports
//
//                }
            }
        });
        btnAPadd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewAirport();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAirport();
            }
        });
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchForAirlinesTruAirport();
            }
        });
        viewAirlineRouteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewRoutesForAirlines();
            }
        });
        viewActiveRoutesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAllAirlinesInDB();
            }
        });

        addRouteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRoutes();
            }
        });
        deleteRouteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRoute();
            }
        });
        view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RoutesAirline();
            }
        });
    }

    private void RoutesAirline(){
        if (objectOutputStream != null && objectInputStream != null) {
            // get the user input from the text field
            String source = srcAirportName.getText();
            if(Objects.isNull(source)||source.isBlank()){
                airlineRLabel.setText("Source airport name  is required");
                return;
            }
            String destination = destiAirportName.getText();
            if(Objects.isNull(destination)||destination.isBlank()){
                airlineRLabel.setText("Destination airport name  is required");
                return;
            }
            //concatenate the user input to a string for sending
            String userInput = String.format("%s:%s", source, destination);
            try {
                objectOutputStream.writeObject(new AirParcels(AirParcels.command.VIEWAIIRLINESTOPS, userInput));
            } catch (IOException e) {
                airlineRLabel.setText("IOException " + e);
            }
            TableParcel tableResponseParcel;
//            AirTrafficParcel airTrafficParcel ;

            try {
                tableResponseParcel = (TableParcel) objectInputStream.readObject();
                String status = tableResponseParcel.getStatus();
                if (Objects.isNull(status)||status.isBlank() ){
                    airlineRLabel.setText("The table below contains the Airlines that go from "  +source+ " airport to " +destination+ " airport");
                    viewRArlineTable.setModel(new GenericTableModel(tableResponseParcel.columns, tableResponseParcel.data));
                }
                else {
                    viewRArlineTable.setModel(new GenericTableModel(tableResponseParcel.columns, tableResponseParcel.data));
                    airlineRLabel.setText(tableResponseParcel.getStatus());
                }
            } catch (IOException ex) {
                airlineRLabel.setText("IOException " + ex);
            } catch (ClassNotFoundException ex) {
                airlineRLabel.setText("ClassNotFoundException " + ex);
            }
        } else {
            homepageStatusLabel.setText("You must connect to the server first!!");
        }
    }

    private synchronized void deleteRoute() {
        if (objectOutputStream != null && objectInputStream != null) {
            //get the user input
            String airlineName = deleteRtAirlineTf.getText();
            if(Objects.isNull(airlineName)||airlineName.isBlank()){
                deleteStatusLabel.setText("Airline name  is required");
                return;
            }
            String sourceAirport = delSrcApTf.getText();
            if(Objects.isNull(sourceAirport)||sourceAirport.isBlank()){
                deleteStatusLabel.setText("Source airport name  is required");
                return;
            }
            String destAirport = DelRtDstApTf.getText() ;
            if(Objects.isNull(destAirport)||destAirport.isBlank()){
                deleteStatusLabel.setText("Destination airport name  is required");
                return;
            }
            //joining the user input to be sent to the server(backend)
            String userInput = String.format("%s:%s:%s",airlineName,sourceAirport,destAirport);

            //parse user input and the command using the AirTrafficParcel object  to the server (backend)
            try {
                objectOutputStream.writeObject(new AirParcels(AirParcels.command.DELETEAIRLINEROUTE, userInput));
            } catch (IOException e) {
                deleteStatusLabel.setText("IOException " + e);
            }

            deleteStatusLabel.setText("Status: waiting for reply from server");
            AirParcels response = null;
            //receiving the reply from the server (backend)
            try {
                response= (AirParcels) objectInputStream.readObject();
                deleteStatusLabel.setText(response.getStatus());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }

        } else {
            homepageStatusLabel.setText("You must connect to the server first!!");
        }
    }

    private synchronized void addRoutes() {
        if (objectOutputStream != null && objectInputStream != null) {

            String source  = addRsourceTf.getText();
            if(Objects.isNull(source)||source.isBlank()){
                addRoutesLabel.setText("Source airport name  is required");
                return;
            }
            String destination = addRdstTf.getText();
            if(Objects.isNull(destination)||destination.isBlank()){
                addRoutesLabel.setText("Destination airport name  is required");
                return;
            }
            String airline = addRoutesAirlineTf.getText();
            if(Objects.isNull(airline)||airline.isBlank()){
                addRoutesLabel.setText("Airline name  is required");
                return;
            }
            String userInput  = String.format("%s:%s:%s",source,destination,airline);
            try {
                objectOutputStream.writeObject(new AirParcels(AirParcels.command.ADDNEWAIRLINEROUTE, userInput));
            } catch (IOException ex) {
                addRoutesLabel.setText("IOException " + ex);
            }
            AirParcels reply = null;

            addRoutesLabel.setText("Status: waiting for reply from server");
            try {
                reply = (AirParcels) objectInputStream.readObject();
                addRoutesLabel.setText(reply.getStatus());
            } catch (IOException ex) {
                addRoutesLabel.setText("IOException " + ex);
            } catch (ClassNotFoundException ex) {
                addRoutesLabel.setText("ClassNotFoundException " + ex);
            }
        }else{
            homepageStatusLabel.setText("You must connect to the server first!!");
        }
    }

    private synchronized void  addNewAirport() {
        if (objectOutputStream != null && objectInputStream != null) {
            //getting the user input
            String name = addapNameText.getText();
            if(Objects.isNull(name)||name.isBlank()){
                addairportStatLabel.setText("Airport Name is required");
                return;
            }
            String city = addapCityText.getText();
            if(Objects.isNull(city)||city.isBlank()){
                addairportStatLabel.setText("Airport city is required");
                return;
            }
            String country  = addapCountryText.getText();
            if(Objects.isNull(country)||country.isBlank()){
                addairportStatLabel.setText("Airport country is required");
                return;
            }
            String code  = addapCodeText.getText();
            if(Objects.isNull(code)||code.isBlank()){
                addairportStatLabel.setText("Airport code is required");
                return;
            }
            String ICAO  = addapICAOText.getText();
            if(Objects.isNull(ICAO)||ICAO.isBlank()){
                addairportStatLabel.setText("Airport ICAO is required");
                return;
            }
            String Latitude  = addapLatitudeText.getText();
            if(Objects.isNull(Latitude)||Latitude.isBlank()){
                addairportStatLabel.setText("Airport Latitude is required");
                return;
            }
            String longitude  = addapLongitudeText.getText();
            if(Objects.isNull(longitude)||longitude.isBlank()){
                addairportStatLabel.setText("Airport Name is required");
                return;
            }
            String Alti  = addapAltitudeText.getText();
            if(Objects.isNull(Alti)||Alti.isBlank()){
                addairportStatLabel.setText("Airport Altitude is required");
                return;
            }
            String offset = addapOffsetText.getText();
            if(Objects.isNull(offset)||offset.isBlank()){
                addairportStatLabel.setText("Airport Offset is required");
                return;
            }
            String dst  = addapDSTText.getText();
            if(Objects.isNull(dst)||dst.isBlank()){
                addairportStatLabel.setText("Airport DST is required");
                return;
            }
            String timezone  = addapTZoneText.getText();
            if(Objects.isNull(timezone)||timezone.isBlank()){
                addairportStatLabel.setText("Airport timezone is required");
                return;
            }
// concatenating the user input to send
            String userInput =
                    name + ":" + city + ":" + country + ":" +
                            code + ":" + ICAO + ":" + Latitude + ":" + longitude + ":" +
                            Alti + ":" + offset + ":" + dst + ":" + timezone;
            try {
                objectOutputStream.writeObject(new AirParcels(AirParcels.command.ADDAIRPORT, userInput));
            } catch (IOException ex) {
                addairportStatLabel.setText("IOException " + ex);
            }
            AirParcels reply = null;

            addairportStatLabel.setText("Status: waiting for reply from server");
            try {
                reply = (AirParcels) objectInputStream.readObject();
                addairportStatLabel.setText(reply.getStatus());

            } catch (IOException ex) {
                addairportStatLabel.setText("IOException " + ex);
            } catch (ClassNotFoundException ex) {
                addairportStatLabel.setText("ClassNotFoundException " + ex);
            }
        }else{
            addairportStatLabel.setText("You must connect to the server first!!");
        }
    }

    /**This method returns a concatenated string containing the text in the TextFields according to how they are on the GUI
     * @param textF1 First position text field identified as the part of the string in lowercase
     * @param textF2 Second position text field identified as the part of the returned string in uppercase
     * **/
    public String arrageInputUI(@org.jetbrains.annotations.NotNull JTextField textF1, @NotNull JTextField textF2){
        String text1 = textF1.getText();
        String text2 = textF2.getText();
        String word;
        if(Objects.isNull(text1)||text1.isBlank()){
            text2 = textF2.getText();
            word = text2.toLowerCase();
            textF2.setText("");
            return word;
        }
        else if(Objects.isNull(text2)||text2.isBlank()){
            text1 = textF1.getText();
            word = text1.toUpperCase();
            textF1.setText("");
            return word;
        }
        else{
            text1 = textF1.getText();
            text2 = textF2.getText();
            word = String.format("%s:%s", text1.toLowerCase(), text2.toUpperCase());
            textF1.setText("");
            textF2.setText("");
            return word;
        }

    }
/**This method is used to view the Airport taking the user input **/
    private void ViewAirPortUserinput() {
        if (objectOutputStream != null && objectInputStream != null) {
            // user input
            String userInput = arrageInputUI(apCountryTextf,apCityTextf);
//            if(apCityTextf.getText().isEmpty()){
//                country = apCountryTextf.getText();
//                 userInput = country.toUpperCase();
//            }
//            else if(apCountryTextf.getText().isEmpty()){
//                city = apCityTextf.getText();
//                 userInput = city.toLowerCase();
//            }
//            else {
//                country = apCountryTextf.getText();
//                city = apCityTextf.getText();
//                userInput = String.format("%s:%s", city.toLowerCase(), country.toUpperCase());
//            }
            // String to be sent using objectStreams

            try {
                objectOutputStream.writeObject(new AirParcels(AirParcels.command.VIEWAIRPORT, userInput));
            } catch (IOException ex) {
                viewairportStatlabel.setText("IOException " + ex);
            }
            TableParcel responseParcel = null;
            //receiving the reply from the server (backend)
            try {
                responseParcel = (TableParcel) objectInputStream.readObject();
                String sqlResultStat = responseParcel.getStatus();
                if(Objects.isNull(sqlResultStat)||sqlResultStat.isBlank()){
                    viewairportStatlabel.setText(responseParcel.getStatus() );
                    airportsTabTable.setModel(new GenericTableModel(responseParcel.columns, responseParcel.data));
                }else{
                    viewairportStatlabel.setText(responseParcel.getStatus());
                    airportsTabTable.setModel(new GenericTableModel(responseParcel.columns, responseParcel.data));
                }
                airportsTabTable.setModel(new GenericTableModel(responseParcel.columns, responseParcel.data));

            } catch (IOException ex) {
                viewairportStatlabel.setText("IOException " + ex);
            } catch (ClassNotFoundException ex) {
                viewairportStatlabel.setText("ClassNotFoundException " + ex);
            }

        }else{
            homepageStatusLabel.setText("You must connect to the server first!!");
        }
    }

/**This method responds to the action whereby the whole active airports are displayed**/
    private void viewAllAirport() {
        if (objectOutputStream != null && objectInputStream != null) {
            // Parse the command type no input required from the user
            try {
                objectOutputStream.writeObject(new AirParcels(AirParcels.command.ViewAllAirport));
            } catch (IOException ex) {
                viewairportStatlabel.setText("IOException " + ex);
            }
            TableParcel responseParcel = null;
            //receiving the reply from the server (backend)
            try {
                responseParcel = (TableParcel) objectInputStream.readObject();
                airportsTabTable.setSize(200,200);
                airportsTabTable.setModel(new GenericTableModel(responseParcel.columns, responseParcel.data));
                viewairportStatlabel.setText("Showing all the Airports on the Database");

            } catch (IOException ex) {
                viewairportStatlabel.setText("IOException " + ex);
            } catch (ClassNotFoundException ex) {
                viewairportStatlabel.setText("ClassNotFoundException " + ex);
            }
        }else{
            homepageStatusLabel.setText("You must connect to the server first!!");
        }
    }
    public synchronized void deleteAirport(){
        if (objectOutputStream !=null && objectInputStream != null){
            //Steps
            // get the user Input ie the name of the Airport to delete
            String userIO = apDelname.getText();
            if(Objects.isNull(userIO)||userIO.isBlank()){
                deleteStatLabel.setText("Airport name  is required");
                return;
            }
            // use the AirTrafficParcel object to send the data to the server
            try {
                objectOutputStream.writeObject(new AirParcels(AirParcels.command.DELETEAIRPORT, userIO));
            } catch (IOException e) {
                deleteStatLabel.setText("IOException " + e);
            }
            AirParcels serverReply = null;
            // get the reply from the server
            try {
                serverReply = (AirParcels) objectInputStream.readObject();
                deleteStatLabel.setText(serverReply.getStatus());

            } catch (IOException e) {
                deleteStatLabel.setText("IOException " + e);
            } catch (ClassNotFoundException e) {
                deleteStatLabel.setText("Class not found exception " + e);
            }
        }
        else {
            homepageStatusLabel.setText("Please connect to the server before you start");
        }

    }
    public  void searchForAirlinesTruAirport(){
        // gets the user input
//        String city = apCountry.getText();
//        if(Objects.isNull(city)||city.isBlank()){
//            airlineAirportLabel.setText("City name  is required");
//            return;
//        }
//        //destination Airport city
//        String country = apCity.getText();
//        if(Objects.isNull(country)||country.isBlank()){
//            airlineAirportLabel.setText("city name  is required");
//            return;
//        }
        // concatenate the Strings to send
        String userInput = arrageInputUI(apCountry,apCity);


        //send the data using the Air traffic parcel
        try {
            objectOutputStream.writeObject(new AirParcels(AirParcels.command.VIEWAIRLINESGOINGTRUAIRPORT, userInput));
        } catch (IOException e) {
            airlineAirportLabel.setText("IOException " + e);
        }
        TableParcel responseParcel ;
        //receiving the reply from the server (backend)
        try {
            responseParcel= (TableParcel) objectInputStream.readObject();
            String status = responseParcel.getStatus();
            if(Objects.isNull(status)||status.isBlank()){
                airlineAirportLabel.setText(responseParcel.getStatus());
                viewAirlinesAPTable.setModel(new GenericTableModel(responseParcel.columns, responseParcel.data));
            }else{
                airlineAirportLabel.setText(responseParcel.getStatus());
                viewAirlinesAPTable.setModel(new GenericTableModel(responseParcel.columns, responseParcel.data));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    public void viewRoutesForAirlines() {
        if (objectOutputStream != null && objectInputStream != null) {
            //get the user input
            String userInput = airlineVal.getText();
            if(Objects.isNull(userInput)||userInput.isBlank()){
                viewRoutesLabel.setText("Airline name  is required");
                return;
            }

            //parse user input and the command using the AirTrafficParcel object  to the server (backend)
            try {
                objectOutputStream.writeObject(new AirParcels(AirParcels.command.VIEWAIRLINEROUTES, userInput));
            } catch (IOException e) {
                viewRoutesLabel.setText("IOException " + e);
            }

            viewRoutesLabel.setText("Status: waiting for reply from server");
            TableParcel responseParcel = null;
            //receiving the reply from the server (backend)
            try {
                responseParcel= (TableParcel) objectInputStream.readObject();
                String status = responseParcel.getStatus();
                if(Objects.isNull(status)||status.isBlank()){
                    viewRoutesLabel.setText(responseParcel.getStatus());
                    viewRoutesTable.setModel(new GenericTableModel(responseParcel.columns, responseParcel.data));
                }else{
                    viewRoutesLabel.setText(responseParcel.getStatus());
                    viewRoutesTable.setModel(new GenericTableModel(responseParcel.columns, responseParcel.data));
                }
                viewRoutesTable.setModel(new GenericTableModel(responseParcel.columns, responseParcel.data));

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }

        } else {
            homepageStatusLabel.setText("You must connect to the server first!!");
        }
    }
    private void viewAllAirlinesInDB() {
        if (objectOutputStream != null && objectInputStream != null) {
            // Parse the command type no input required from the user
            try {
                objectOutputStream.writeObject(new AirParcels(AirParcels.command.VIEWACTIVEAIRLINESINDB));
            } catch (IOException ex) {
                viewRoutesLabel.setText("IOException " + ex);
            }
            TableParcel responseParcel = null;
            //receiving the reply from the server (backend)
            try {
                responseParcel = (TableParcel) objectInputStream.readObject();
                viewRoutesTable.setModel(new GenericTableModel(responseParcel.columns, responseParcel.data));
                viewRoutesLabel.setText("Showing all the active Airlines on the Database");

            } catch (IOException ex) {
                viewRoutesLabel.setText("IOException " + ex);
            } catch (ClassNotFoundException ex) {
                viewRoutesLabel.setText("ClassNotFoundException " + ex);
            }
        }else{
            homepageStatusLabel.setText("You must connect to the server first!!");
        }

    }

    private void closeConnection() {
        if (socket != null) {
            homepageStatusLabel.setText("Status: closing connection");
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ControllerHomePage.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                socket = null;
            }
        }
    }

    private void intializeGUI() {
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(250, 150);
        this.setVisible(true);
        this.pack();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e); //To change body of generated methods, choose Tools | Templates.
                closeConnection();
                System.exit(0);
            }
        });
    }


    private void initialconnection() {
        closeConnection();
        homepageStatusLabel.setText("Status: Attempting connection to server");
        try {
            socket = new Socket("127.0.0.1", 2000);

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new  ObjectInputStream(socket.getInputStream());
            homepageStatusLabel.setText("Status: Connected to server");
        } catch (IOException ex) {
            Logger.getLogger(ControllerHomePage.class.getName()).log(Level.SEVERE, null, ex);
            homepageStatusLabel.setText(ex.toString()); // connection failed
        }

    }

    public static void main(String[] args) {

        ControllerHomePage app = new ControllerHomePage("Demo");



    }
}

