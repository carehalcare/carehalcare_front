package carehalcare.carehalcare.Feature_write.Medicine;
import com.google.gson.annotations.SerializedName;

public class Medicine_text_change {
    @SerializedName("time")
    String time = "";  //시간(아침,점심,저녁)
    @SerializedName("mealStatus")
    String mealStatus =""; //상태(공복,식전,식후)
    @SerializedName("medicine")
    String medicine=""; //약 종류

    @SerializedName("id")
    Long id;


    public Medicine_text_change(Long id, String time, String mealStatus, String medicine) {
        this.time = time;
        this.mealStatus = mealStatus;
        this.medicine = medicine;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String gettime() {
        return time;
    }

    public void settime(String medicine_time) {
        this.time = time;
    }

    public String getmealStatus() {
        return mealStatus;
    }

    public void setmealStatus(String medicine_state) {
        this.mealStatus = medicine_state;
    }

    public String getmedicine() {
        return medicine;
    }

    public void setmedicine(String medicine_name) {
        this.medicine = medicine_name;
    }

}
