package carehalcare.carehalcare.Feature_mainpage;

import com.google.gson.annotations.SerializedName;

public class FindPatient {
    @SerializedName("userId") String userId;
    @SerializedName("username") String username;
    private String puserId, Id;

    public FindPatient(String puserId, String userId) {
        this.puserId = userId;
        this.userId = Id;
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
