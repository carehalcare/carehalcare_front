package carehalcare.carehalcare.Feature_mainpage;

import com.google.gson.annotations.SerializedName;

public class Notice {
    @SerializedName("content") String content;
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

    //public String getUserId() { return userId; }

}