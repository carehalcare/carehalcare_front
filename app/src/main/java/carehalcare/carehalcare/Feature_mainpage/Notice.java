package carehalcare.carehalcare.Feature_mainpage;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class Notice {
    @SerializedName("content") String content;
    @SerializedName("modifiedDateTime") String modifiedDateTime;

    @SerializedName("createdDate") String createdDate;
    //@SerializedName("userId") String userId;
    public Notice(String content, String createdDate) {
        this.content = content;
        this.createdDate = createdDate;
    }

    public Notice(){}

    public String getContent() {
        return content;
    }
    public String getCreatedDate() {
        return createdDate;
    }

    public String setCreatedDate(String newDateStr) {
        this.createdDate = createdDate;
        return newDateStr;
    }

    //public String getUserId() { return userId; }

    public String getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(String modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

}