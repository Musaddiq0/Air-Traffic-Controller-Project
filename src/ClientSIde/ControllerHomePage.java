package ClientSIde;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ControllerHomePage {
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
    private JTextField countryVal;
    private JTextField cityVal;
    private JPanel viewAirportsPanel;
    private JTable airportsTabTable;
    private JButton viewActiveRoutesButton;
    private JTextField airlineVal;
    private JButton viewAirlineRouteButton;
    private JPanel viewRoutesPanel;
    private JTable viewRoutesTable;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTable table1;
    private JButton addRouteButton;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JButton deleteRouteButton;
    private JLabel deleteStatusLabel;
    private JTextField textField7;
    private JTextField textField8;
    private JTextField textField9;
    private JTextField textField10;
    private JTextField textField11;
    private JButton addAirportButton;
    private JLabel addAirportStatLabel;
    private JTextField textField12;
    private JButton button1;
    private JLabel deleteStatLabel;
    private JTextField textField13;
    private JTable viewAirlinesAPTable;
    private JButton viewButton;

    // object used in the client class and server communication
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;


    private Socket socket;
}
