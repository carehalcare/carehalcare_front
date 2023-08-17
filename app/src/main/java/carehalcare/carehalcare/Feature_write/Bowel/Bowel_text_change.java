package carehalcare.carehalcare.Feature_write.Bowel;

import com.google.gson.annotations.SerializedName;

public class Bowel_text_change {
    @SerializedName("id")
    Long id;
    @SerializedName("count")
    Long count;
    @SerializedName("content")
    String content;

    public Bowel_text_change(Long id, Long count, String content) {
        this.id = id;
        this.count = count;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
