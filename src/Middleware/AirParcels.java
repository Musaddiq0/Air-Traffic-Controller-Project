package Middleware;

import java.io.Serializable;

public class AirParcels implements Serializable {

    public enum command {ViewAllAirport};
    String userInput;
    private final command command;

    /**
     * this constructoor takes in only the command from
     * @param commandtype  this is the CommandType enum used to tell the server what action is required by the button clicked   */
    public AirParcels(command commandtype) {
        this.command= commandtype;

    }

    public AirParcels.command getCommand() {
        return command;
    }
}
