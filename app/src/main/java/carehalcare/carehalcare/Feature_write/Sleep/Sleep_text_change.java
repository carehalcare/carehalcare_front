package carehalcare.carehalcare.Feature_write.Sleep;

import com.google.gson.annotations.SerializedName;

public class Sleep_text_change {
    @SerializedName("state")
    String state ;
    @SerializedName("content")
    String content;
    @SerializedName("id")
    Long id;



    public Sleep_text_change(Long id, String state, String content) {
        this.state = state;
        this.content = content;
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
