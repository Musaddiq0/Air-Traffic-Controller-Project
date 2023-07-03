package ClientSIde;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
        ViewAirportTable.setSize(200,200);
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

