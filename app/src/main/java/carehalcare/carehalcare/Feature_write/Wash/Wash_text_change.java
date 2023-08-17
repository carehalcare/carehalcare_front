package carehalcare.carehalcare.Feature_write.Wash;

import com.google.gson.annotations.SerializedName;

public class Wash_text_change {

    @SerializedName("cleanliness")
    private String cleanliness;

    @SerializedName("part")
    private String part;

    @SerializedName("content")
    private String content;

    @SerializedName("id")
    Long id;


    public Wash_text_change(Long id, String cleanliness, String part, String content) {
        this.id = id;
        this.cleanliness = cleanliness;
        this.part = part;
        this.content = content;
    }


    public String getCleanliness() {
        return cleanliness;
    }

    public void setCleanliness(String cleanliness) {
        this.cleanliness = cleanliness;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
