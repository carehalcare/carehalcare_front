package carehalcare.carehalcare.Feature_write.Active;

import com.google.gson.annotations.SerializedName;

public class Active_text {

    @SerializedName("userId")
    String userId = "";

    @SerializedName("puserId")
    String puserId="";

    @SerializedName("id")
    Long id;

    @SerializedName("rehabilitation")
    String rehabilitation;

    @SerializedName("walkingAssistance")
    String walkingAssistance;

    @SerializedName("position")
    String position;

    String pan;


    public Active_text(Long id,String userId, String puserId, String rehabilitation, String walkingAssistance, String position) {
        this.id = id;
        this.userId = userId;
        this.puserId = puserId;
        this.rehabilitation = rehabilitation;
        this.walkingAssistance = walkingAssistance;
        this.position = position;
    }
    public Active_text(String userId, String puserId, String rehabilitation, String walkingAssistance, String position) {
        this.userId = userId;
        this.puserId = puserId;
        this.rehabilitation = rehabilitation;
        this.walkingAssistance = walkingAssistance;
        this.position = position;
        this.pan = pan;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPuserId() {
        return puserId;
    }

    public void setPuserId(String puserId) {
        this.puserId = puserId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRehabilitation() {
        return rehabilitation;
    }

    public void setRehabilitation(String rehabilitation) {
        this.rehabilitation = rehabilitation;
    }

    public String getWalkingAssistance() {
        return walkingAssistance;
    }

    public void setWalkingAssistance(String walkingAssistance) {
        this.walkingAssistance = walkingAssistance;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
