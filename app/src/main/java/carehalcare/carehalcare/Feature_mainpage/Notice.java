package carehalcare.carehalcare.Feature_mainpage;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class Notice {
    @SerializedName("content") String content;
    @SerializedName("modifiedDateTime") String modifiedDateTime;

    @SerializedName("createdDateTime") String createdDateTime;
    //@SerializedName("userId") String userId;
    public Notice(String content, String createdDateTime) {
        this.content = content;
        this.createdDateTime = createdDateTime;
    }

    public Notice(){}

    public String getContent() {
        return content;
    }
    public String getcreatedDateTime() {
        return createdDateTime;
    }

    public String setcreatedDateTime(String newDateStr) {
        this.createdDateTime = createdDateTime;
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