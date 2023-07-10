package Serverside;

import Middleware.AirParcels;
import Middleware.TableParcel;

import javax.swing.*;
import javax.swing.text.TabExpander;
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
        String[] words=inputWord.split("\\s");
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

                String airportQry ="select id from airports where name = ? limit 1";



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
                            String selectSqlCountryCity = "SELECT * FROM airports WHERE country=?";
                            try (Connection conn = SqlLiteConnection.getConnection();
                                 PreparedStatement prep = conn.prepareStatement(selectSqlCountryCity)) {
                                prep.setString(1,  capitalizeWord(country.toLowerCase())); //country
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
                    String[] splitUserInput = userInput.split(":");
                    String insertSQL = "INSERT INTO airports (name, city, country, code, icao, latitude, longitude, altitude, offset,dst,timezone) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
                    String apName = splitUserInput[0].trim();
                    String apCity = splitUserInput[1].trim();
                    String apCountry = splitUserInput[2].trim();
                    String apCode = splitUserInput[3].trim();
                    String apIcao = splitUserInput[4].trim();
                    String apLatitude = splitUserInput[5].trim();
                    String apLongitude = splitUserInput[6].trim();
                    String apAltitude = splitUserInput[7].trim();
                    String apOffset = splitUserInput[8].trim();
                    String apDst = splitUserInput[9].trim();
                    String apTimezone = splitUserInput[10].trim();

                    try(Connection conn = SqlLiteConnection.getConnection())
                    {
                        PreparedStatement prepStm= conn.prepareStatement(airportQry);
                        prepStm.setString(1,splitUserInput[0]);
                        ResultSet resultSet = prepStm.executeQuery();
                        // using the airport name to check if it exists on the table already
                        if (resultSet.next()){
                            airParcels.setStatus("Chosen Airport already exist");
                            objectOutputStream.writeObject(airParcels);
                            continue;
                        }

                        prepStm = conn.prepareStatement(insertSQL);
                        prepStm.setString(1,apName);
                        prepStm.setString(2,apCity);
                        prepStm.setString(3,apCountry);
                        prepStm.setString(4,apCode);
                        prepStm.setString(5,apIcao);
                        prepStm.setString(6,apLatitude);
                        prepStm.setString(7,apLongitude);
                        prepStm.setString(8,apAltitude);
                        prepStm.setString(9,apOffset);
                        prepStm.setString(10,apDst);
                        prepStm.setString(11,apTimezone);
                        int executeStat =prepStm.executeUpdate();
                        if (executeStat >0){
                            airParcels.setStatus(splitUserInput[0]+  " Airport has been added to the DB");
                            objectOutputStream.writeObject(airParcels);
                        }
                        // updating the status label and writing back to the client using the airtraffic parcel class

                    }

                }
                else if(airParcels.getCommand() == AirParcels.command.DELETEAIRPORT){
                    String deleteSql = "Delete FROM airports where name=?";
                    String checkAirportSql = "select id from airports where name = ? limit 1";
                    String airlineName = userInput.trim();

                    // checking if the typed airport exists already in the Database table
                    try(Connection conn = SqlLiteConnection.getConnection()) {
                        PreparedStatement prepStm = conn.prepareStatement(checkAirportSql);
                        prepStm.setString(1, airlineName);
                        ResultSet resultSet = prepStm.executeQuery();
                        if (!resultSet.next()) {
                            airParcels.setStatus("Chosen Airport does not exist on the table");
                            objectOutputStream.writeObject(airParcels);
                            continue;
                        }
                    }
                    try(Connection conn = SqlLiteConnection.getConnection()){
                        PreparedStatement prepStm = conn.prepareStatement(deleteSql);
                        prepStm.setString(1,userInput);
                        int sqlLStatus = prepStm.executeUpdate();
                        if (sqlLStatus >0){
                            airParcels.setStatus(userInput+ "Airport has been deleted from everywhere");
                            objectOutputStream.writeObject(airParcels);
                        }
                    }
                }
                else if(airParcels.getCommand() == AirParcels.command.VIEWAIRLINESGOINGTRUAIRPORT){
                    //steps
                    //get the user input from the GUI
                    String[] separatedUserInput = userInput.split(":");
                    //Generate the SQL query according to the user input.

                    //country and city entered
                    if (separatedUserInput.length == 2 ){
                        String sqlQuery = "SELECT airlines.name FROM airlines  JOIN routes ON (airlines.id== routes.airline_id) JOIN airports ON (airports.id== routes.source_id)  OR (airports.id== routes.dest_id)  WHERE  airports.country =? AND airports.city =? GROUP by airlines.name";
                        String country = separatedUserInput[1].trim();
                        String city = separatedUserInput[0].trim();
                        try(Connection conn = SqlLiteConnection.getConnection()){
                            PreparedStatement prepStm = conn.prepareStatement(sqlQuery);
                            prepStm.setString(1,country); //country
                            prepStm.setString(2,city); //city
                            ResultSet resultSet = prepStm.executeQuery();
                            List<List<Object>> data = new ArrayList<>();
                            while (resultSet.next()){
                                List<Object> column= new ArrayList<>();
                                column.add(resultSet.getString(1));
                                data.add(column);
                            }
                            if(data.size() > 0){
                                List<String> columns = new ArrayList<>();
                                // table columns creation
                                columns.add("Airlines");
                                TableParcel res = new TableParcel(columns, data);
                                //sending the TableResponseParcel object with the columns and data containing the results from the SQL query
                                objectOutputStream.writeObject(res);
                            }else{
                                //clearing the table if no result from the query
                                String noResultSet = "No Airlines fly through" +city+ "," +country;
                                List<String> columns = new ArrayList<>();
                                TableParcel res = new TableParcel(columns,data,noResultSet);
                                res.setStatus(noResultSet);
                                objectOutputStream.writeObject(res);

                            }

                        }
                    }
                    //country or city entered
                    else{
                        //checking if the string array contains strings in upper or lower case

                        //country entered only
                        if (isStringInCapitalLetters(Arrays.toString(separatedUserInput))){
                            String sqlQuery = "SELECT airlines.name FROM airlines  JOIN routes ON (airlines.id== routes.airline_id) JOIN airports ON (airports.id== routes.source_id)  OR (airports.id== routes.dest_id)  WHERE  airports.country =? OR airports.city ='?' GROUP by airlines.name";
                            try(Connection conn = SqlLiteConnection.getConnection()){
                                PreparedStatement prepStm = conn.prepareStatement(sqlQuery);
                                prepStm.setString(1,capitalizeWord(separatedUserInput[0].toLowerCase())); //country
                                ResultSet resultSet = prepStm.executeQuery();
                                List<List<Object>> data = new ArrayList<>();
                                while (resultSet.next()){
                                    List<Object> column= new ArrayList<>();
                                    column.add(resultSet.getString(1));
                                    data.add(column);
                                }
                                if(data.size() > 0){
                                    List<String> columns = new ArrayList<>();
                                    // table columns creation
                                    columns.add("Airlines");
                                    TableParcel res = new TableParcel(columns, data);
                                    //sending the TableResponseParcel object with the columns and data containing the results from the SQL query
                                    objectOutputStream.writeObject(res);
                                }else{
                                    //clearing the table if no result from the query
                                    String noResultSet = "No Airlines fly through "+ Arrays.toString(separatedUserInput);
                                    List<String> columns = new ArrayList<>();
                                    TableParcel res = new TableParcel(columns,data,noResultSet);
                                    res.setStatus(noResultSet);
                                    objectOutputStream.writeObject(res);

                                }

                            }

                        }

                        //city only
                        else{
                            String sqlQuery = "SELECT airlines.name FROM airlines  JOIN routes ON (airlines.id== routes.airline_id) JOIN airports ON (airports.id== routes.source_id)  OR (airports.id== routes.dest_id)  WHERE  airports.country ='?' OR airports.city =? GROUP by airlines.name";
                            capitalizeWord(separatedUserInput[0].toLowerCase());
                            try(Connection conn = SqlLiteConnection.getConnection()){
                                PreparedStatement prepStm = conn.prepareStatement(sqlQuery);
                                prepStm.setString(1,capitalizeWord(separatedUserInput[0].toLowerCase())); //city
                                ResultSet resultSet = prepStm.executeQuery();
                                List<List<Object>> data = new ArrayList<>();
                                while (resultSet.next()){
                                    List<Object> column= new ArrayList<>();
                                    column.add(resultSet.getString(1));
                                    data.add(column);
                                }
                                if(data.size() > 0){
                                    List<String> columns = new ArrayList<>();
                                    // table columns creation
                                    columns.add("Airlines");
                                    TableParcel res = new TableParcel(columns, data);
                                    //sending the TableResponseParcel object with the columns and data containing the results from the SQL query
                                    objectOutputStream.writeObject(res);
                                }else{
                                    //clearing the table if no result from the query
                                    String noResultSet = "No Airlines fly through "+ Arrays.toString(separatedUserInput);
                                    List<String> columns = new ArrayList<>();
                                    TableParcel res = new TableParcel(columns,data,noResultSet);
                                    res.setStatus(noResultSet);
                                    objectOutputStream.writeObject(res);

                                }

                            }

                        }

                    }
                }
                else if(airParcels.getCommand() == AirParcels.command.VIEWAIRLINEROUTES){

                    // create the SQL statement
                    String viewRoutes = "SELECT st.name AS source, dt.name as destination FROM routes JOIN airports st ON (st.id== routes.source_id) JOIN airports dt  ON  (dt.id == routes.dest_id) JOIN airlines ON (airlines.id== routes.airline_id) WHERE  airlines.name =? GROUP BY st.name, dt.name";
                    String airlineName = userInput.trim();
                    try(Connection conn = SqlLiteConnection.getConnection()){
                        PreparedStatement prepStm = conn.prepareStatement(viewRoutes);
                        prepStm.setString(1,airlineName);
                        ResultSet resultSet = prepStm.executeQuery();
                        List<List<Object>> data = new ArrayList<>();
                        while (resultSet.next()){
                            List<Object> column= new ArrayList<>();
                            column.add(resultSet.getString(1));
                            column.add(resultSet.getString(2));
                            data.add(column);
                        }
                        if (data.size()>0){
                            List<String> columns = new ArrayList<>();
                            columns.add("Source");
                            columns.add("Destination");
                            TableParcel res = new TableParcel(columns, data);
                            objectOutputStream.writeObject(res);
                        }else {
                            String noResultSet = userInput+ " " + "has no active route currently";
                            List<String> columns = new ArrayList<>();
                            TableParcel res = new TableParcel(columns, data, noResultSet);
                            objectOutputStream.writeObject(res);
                        }

                    }
                }
                else if (airParcels.getCommand()== AirParcels.command.VIEWACTIVEAIRLINESINDB){

                    // create the SQL query to view all the airports in the DB in ascending order
                    String viewAllAirlinesSql = "SELECT DISTINCT * FROM airlines WHERE NOT active = 'N'";

                    // open the SQL connection to execute the query
                    try (Connection conn = SqlLiteConnection.getConnection();
                         PreparedStatement prep = conn.prepareStatement(viewAllAirlinesSql)){
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
                            data.add(column);
                        }
                        List<String> columns = new ArrayList<>();
                        columns.add("ID");
                        columns.add("Name");
                        columns.add("Alias");
                        columns.add("Iata");
                        columns.add("ICAO");
                        columns.add("CallSign");
                        columns.add("Country");
                        columns.add("active");
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
//    String searchAp = "SELECT airlines.name FROM airlines  JOIN routes ON (airlines.id== routes.airline_id) JOIN airports ON (airports.id== routes.source_id)  OR (airports.id== routes.dest_id)  WHERE  airports.country =? AND airports.city =? GROUP by airlines.name";

}

