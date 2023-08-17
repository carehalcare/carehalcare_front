package carehalcare.carehalcare.Feature_write.Clean;

import com.google.gson.annotations.SerializedName;

public class Clean_text_change {
    String createdDateTime;

    @SerializedName("id")
    Long id;

    @SerializedName("cleanliness")
    String cleanliness;

    @SerializedName("content")
    String content;

    public Clean_text_change(Long id, String cleanliness, String content) {
        this.id = id;
        this.cleanliness = cleanliness;
        this.content = content;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCleanliness() {
        return cleanliness;
    }

    public void setCleanliness(String cleanliness) {
        this.cleanliness = cleanliness;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
