package app.rest.request;

import com.google.gson.annotations.SerializedName;

public class TopicsRequest {

    @SerializedName("topicName")
    public String topicName;

    public TopicsRequest(String topicName) {
        this.topicName = topicName;
    }
}
