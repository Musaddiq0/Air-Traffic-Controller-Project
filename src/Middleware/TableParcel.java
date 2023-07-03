package Middleware;

import java.io.Serializable;
import java.util.List;

public class TableParcel  implements Serializable {
    public List<String> columns;
    public List<List<Object>> data;
    public String status;

    /**
     * this contructor sets the columns names for the table and the stores the rows of result of the query as list of objects
     * @param data  this takes in the rows gotten from the database as objects
     * @param columns  takes in the column count and the names of the columns  for the table*/

    public TableParcel(List<String> columns, List<List<Object>> data){
        this.columns=columns;
        this.data=data;
    }

    public TableParcel(List<String> columns, List<List<Object>> data, String status){
        this.columns=columns;
        this.data=data;
        this.status=status;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

