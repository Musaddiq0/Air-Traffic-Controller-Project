package Serverside;

import Middleware.AirParcels;
import Middleware.TableParcel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadHandler implements Runnable {
    private final Socket socket;
    private static int connectionCount = 0;
    private final int connectionNumber;

    public ThreadHandler(Socket socket) throws IOException {
        this.socket = socket;
        connectionCount++;
        connectionNumber = connectionCount;
        threadSays("Connection " + connectionNumber + " established.");
    }

    private void threadSays(String s) {
        System.out.println("ClientHandlerCW" + connectionNumber + ": " + s);

    }

    @Override
    public void run() {
        try {
            threadSays("Waiting for data from client...");
            System.out.println("Server: Waiting for data from client...");
            AirParcels airParcels;

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream((socket.getInputStream()));


            while ((airParcels = (AirParcels) objectInputStream.readObject()) != null) {
                System.out.println(airParcels.getCommand());


                if (airParcels.getCommand() == AirParcels.command.ViewAllAirport) {
                    String sqlQuery = "SELECT DISTINCT * FROM airports ORDER by country";
                    // create the SQL connection from the SQL class and run the SQL query
                    try (Connection conn = SqlLiteConnection.getConnection();
                         PreparedStatement prep = conn.prepareStatement(sqlQuery)) {
                        ResultSet resultSet = prep.executeQuery();
                        // used to store the rows of resultset for table modification
                        List<List<Object>> data = new ArrayList<>();
                        while (resultSet.next()) {
                            List<Object> column = new ArrayList<>();
                            column.add(resultSet.getInt(1));
                            column.add(resultSet.getString(2));
                            column.add(resultSet.getString(3));
                            column.add(resultSet.getString(4));
                            column.add(resultSet.getString(5));
                            column.add(resultSet.getString(6));
                            column.add(resultSet.getString(7));
                            column.add(resultSet.getString(8));
                            column.add(resultSet.getString(9));
                            column.add(resultSet.getString(10));
                            column.add(resultSet.getString(11));
                            column.add(resultSet.getString(12));
                            data.add(column);
                        }
                        List<String> columns = new ArrayList<>();
                        columns.add("ID");
                        columns.add("Name");
                        columns.add("City");
                        columns.add("Country");
                        columns.add("Code");
                        columns.add("ICAO");
                        columns.add("Latitude");
                        columns.add("Longitude");
                        columns.add("Altitude");
                        columns.add("Offset");
                        columns.add("DST");
                        columns.add("Timezone");
                        TableParcel res = new TableParcel(columns, data);
                        objectOutputStream.writeObject(res);
                    }
                }

            }

        } catch (IOException ex) {
            Logger.getLogger(ThreadHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                threadSays("We have lost connection to client " + connectionNumber + ".");
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ThreadHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}

