package ClientSIde;

import Middleware.AirParcels;
import Middleware.GenericTableModel;
import Middleware.TableParcel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
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
                viewairportStatlabel.setText("you must provide a country atleast to be able to view the airports");

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

