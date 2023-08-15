package carehalcare.carehalcare.Feature_write.Active;

import com.google.gson.annotations.SerializedName;

public class Active_text_change {


    @SerializedName("id")
    Long id;

    @SerializedName("rehabilitation")
    String rehabilitation;

    @SerializedName("walkingAssistance")
    String walkingAssistance;

    @SerializedName("position")
    String position;


    public Active_text_change(Long id, String rehabilitation, String walkingAssistance, String position) {
        this.id = id;
        this.rehabilitation = rehabilitation;
        this.walkingAssistance = walkingAssistance;
        this.position = position;
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
