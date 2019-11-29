package app.server.v2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by quantum4u1 on 11/04/18.
 */

public class Billing implements Serializable{

    @SerializedName("billing_type")
    @Expose
    public String billing_type;

    @SerializedName("priority")
    @Expose
    public String priority;

    @SerializedName("product_id")
    @Expose
    public String product_id;

    @SerializedName("product_price")
    @Expose
    public String product_price;

    @SerializedName("product_description")
    @Expose
    public String product_description;

    @SerializedName("product_offer_status")
    @Expose
    public boolean product_offer_status;

    @SerializedName("product_offer_text")
    @Expose
    public String product_offer_text;

    @SerializedName("product_offer_sub_text")
    @Expose
    public String product_offer_sub_text;

    @SerializedName("product_offer_src")
    @Expose
    public String product_offer_src;

    @SerializedName("button_text")
    @Expose
    public String button_text;

    @SerializedName("button_sub_text")
    @Expose
    public String button_sub_text;

    @SerializedName("feature_src")
    @Expose
    public String feature_src;

    @SerializedName("details_page_type")
    @Expose
    public String details_page_type;

    @SerializedName("details_src")
    @Expose
    public String details_src;

    @SerializedName("details_description")
    @Expose
    public String details_description;



}
