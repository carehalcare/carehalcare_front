package carehalcare.carehalcare.Feature_write;

import com.google.gson.annotations.SerializedName;

public class Meal_Image {
    @SerializedName("id")
    Long id;

    @SerializedName("originalFilename")
    String originalFilename;

    @SerializedName("storeFilename")
    String storeFilename;

    @SerializedName("filePath")
    String filePath;

    @SerializedName("mealId")
    Long mealId;

    public Meal_Image(Long id, String originalFilename, String storeFilename, String filePath, Long mealId) {
        this.id = id;
        this.originalFilename = originalFilename;
        this.storeFilename = storeFilename;
        this.filePath = filePath;
        this.mealId = mealId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getStoreFilename() {
        return storeFilename;
    }

    public void setStoreFilename(String storeFilename) {
        this.storeFilename = storeFilename;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getMealId() {
        return mealId;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }
}