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
import java.util.Arrays;
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

    /**This method takes in a string and returns true if the string is in uppercase**/
    public static boolean isStringInCapitalLetters(String inputString) {
        String uppercaseString = inputString.toUpperCase();
        return inputString.equals(uppercaseString);
    }
    public String capitalizeWord(String inputWord){
        String words[]=inputWord.split("\\s");
        String capitalizeWord="";
        for(String w:words){
            String first=w.substring(0,1);
            String afterfirst=w.substring(1);
            capitalizeWord+=first.toUpperCase()+afterfirst+" ";
        }
        return capitalizeWord.trim();
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
                String userInput = airParcels.getUserInput();


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
                else if (airParcels.getCommand() == AirParcels.command.VIEWAIRPORT) {
                    String[] splitUserInput = userInput.split(":");
                    // TODO: 09/07/2023 separate the city and country using the case of the sent data
                    //checking if the city is blank to return all airport in the country typed
                    if (splitUserInput.length == 2) {
                        String city = splitUserInput[0].trim();
                        String country = splitUserInput[1].trim();
                        String selectSqlCountryCity = "SELECT * FROM airports WHERE city=? AND country=?";
                        try (Connection conn = SqlLiteConnection.getConnection();
                             PreparedStatement prep = conn.prepareStatement(selectSqlCountryCity)) {
                            prep.setString(1, city); //city
                            prep.setString(2, country); //country
                            ResultSet resultSet = prep.executeQuery();
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
                            if(data.size()> 0 ){
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
                                res.setStatus("Please are the results from the DB for Airports in " +city+ " ," +country);
                                objectOutputStream.writeObject(res);
                            }else{
                                String noResultStat = "No Airports in "+city+ " ," +country;
                                List<String> columns = new ArrayList<>();
                                TableParcel res = new TableParcel(columns, data,noResultStat);
                                res.setStatus(noResultStat);
                                objectOutputStream.writeObject(res);
                            }
                        }
                    }
                    else {
                        // steps
                        // check for country or city
                        if(isStringInCapitalLetters(Arrays.toString(splitUserInput))){
                            // only country entered
                            String country = splitUserInput[0].trim();
                            capitalizeWord(country);
                            String selectSqlCountryCity = "SELECT * FROM airports WHERE country=?";
                            try (Connection conn = SqlLiteConnection.getConnection();
                                 PreparedStatement prep = conn.prepareStatement(selectSqlCountryCity)) {
                                prep.setString(1, country); //country
                                ResultSet resultSet = prep.executeQuery();
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
                                if(data.size()> 0 ){
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
                                    res.setStatus("Please are the results from the DB for Airports in " +country);
                                    objectOutputStream.writeObject(res);
                                }else{
                                    String noResultStat = "No Airports in " +country;
                                    List<String> columns = new ArrayList<>();
                                    TableParcel res = new TableParcel(columns, data,noResultStat);
                                    res.setStatus(noResultStat);
                                    objectOutputStream.writeObject(res);
                                }
                            }

                        }
                         else{
                            //check for the ctiy only
                            String city = splitUserInput[0].trim();
                            city = capitalizeWord(city);
                            String selectSqlCountryCity = "SELECT * FROM airports WHERE city=?";
                            try (Connection conn = SqlLiteConnection.getConnection();
                                 PreparedStatement prep = conn.prepareStatement(selectSqlCountryCity)) {
                                prep.setString(1, city); //city
                                ResultSet resultSet = prep.executeQuery();
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
                                if(data.size()> 0 ){
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
                                    res.setStatus("Please are the results from the DB for Airports in " +city);
                                    objectOutputStream.writeObject(res);
                                }else{
                                    String noResultStat = "No Airports for " +city;
                                    List<String> columns = new ArrayList<>();
                                    TableParcel res = new TableParcel(columns, data,noResultStat);
                                    res.setStatus(noResultStat);
                                    objectOutputStream.writeObject(res);
                                }
                            }

                        }

                    }
                }
                else if (airParcels.getCommand() == AirParcels.command.ADDAIRPORT){

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

