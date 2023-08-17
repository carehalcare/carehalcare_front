package carehalcare.carehalcare.Feature_write.Meal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Meal_text_change {
    @SerializedName("id")
    Long id;
    @SerializedName("content")
    String content;

    public Meal_text_change(Long id, String content) {
        this.id = id;
        this.content =content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
