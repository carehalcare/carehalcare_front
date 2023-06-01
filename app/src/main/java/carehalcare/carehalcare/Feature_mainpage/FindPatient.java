package carehalcare.carehalcare.Feature_mainpage;

import com.google.gson.annotations.SerializedName;

public class FindPatient {
    @SerializedName("userId") String userId;
    @SerializedName("username") String username;
    @SerializedName("puserId")String puserId;
    private String Id;

    public FindPatient(String userId, String puserId) {
        this.puserId = puserId;
        this.userId = userId;
    }

    public FindPatient(){}

    public String getUserId() {
        return userId;
    }
    public String getUsername() {
        return username;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
