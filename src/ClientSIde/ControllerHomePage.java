package ClientSIde;

import Middleware.AirParcels;
import Middleware.GenericTableModel;
import Middleware.TableParcel;

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

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

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
    private JTextField apViewAirlneDepCityVal;
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
    private JTextField apDestCityVal;
    private JScrollPane ViewAirportTable;
    private JLabel viewairportStatlabel;
    private JLabel addairportStatLabel;

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

    private void ViewAirPortUserinput() {
        if (objectOutputStream != null && objectInputStream != null) {
            // user input
            String  country = apCountryTextf.getText();
            String city = apCityTextf.getText();
            // String to be sent using objectStreams
            String userInput = String.format("%s:%s", city, country);
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
                    viewairportStatlabel.setText("Below are the results from the DB of airports in " +country + " ," +city );
                    airportsTabTable.setModel(new GenericTableModel(responseParcel.columns, responseParcel.data));
                }else{
                    viewairportStatlabel.setText(responseParcel.getStatus() +country +" ," +city);
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

