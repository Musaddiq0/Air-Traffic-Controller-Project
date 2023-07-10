package Middleware;

import java.io.Serializable;

public class AirParcels implements Serializable {

    private String statuslabel;

    public enum command {ViewAllAirport,VIEWAIRPORT,ADDAIRPORT,DELETEAIRPORT,VIEWAIRLINESGOINGTRUAIRPORT,VIEWAIRLINEROUTES, VIEWACTIVEAIRLINESINDB}
    private String userInput;
    private final command command;

    /**
     * this constructoor takes in only the command from
     * @param commandtype  this is the CommandType enum used to tell the server what action is required by the button clicked   */
    public AirParcels(command commandtype) {
        this.command= commandtype;

    }
    public AirParcels(command command, String userInput) {
        this.command = command;
        this.userInput = userInput;

    }

    public command getCommand() {
        return command;
    }
    public String getUserInput(){return userInput;}


    public  void setStatus(String status){this.statuslabel = status;
    }

    @Override
    public String toString() {
        return ( command  + "\"");
    }

    public String getStatus() {
        return statuslabel;
    }
}
