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

    // object used in the client class and server communication
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;


    private Socket socket;
}
