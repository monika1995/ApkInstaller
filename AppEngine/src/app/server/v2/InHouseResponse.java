package app.server.v2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anon on 31,August,2018
 */
public class InHouseResponse {

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("inhouseresponse")
    @Expose
    public InHouse inhouseresponse= new InHouse();
}
