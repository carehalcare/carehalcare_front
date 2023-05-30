package carehalcare.carehalcare.Feature_write.Meal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Meal_ResponseDTO {
    @SerializedName("id")
    Long id;
    @SerializedName("userId")
    String userId;
    @SerializedName("puserId")
    String puserId;
    @SerializedName("content")
    String content;
    @SerializedName("images")
    List<Meal_Image> images;
    @SerializedName("createdDateTime")
    String createdDateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Meal_Image> getImages() {
        return images;
    }

    public void setImages(List<Meal_Image> images) {
        this.images = images;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
