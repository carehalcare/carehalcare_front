package carehalcare.carehalcare.Feature_write;

public class Medicine_text {
    String medicine_time;
    String medicine_state;
    String medicine_name;
    String et_medicineForm;
    String medicineTodayResult;

    public Medicine_text(String medicine_time, String medicine_state, String medicine_name, String et_medicineForm, String medicineTodayResult) {
        this.medicine_time = medicine_time;
        this.medicine_state = medicine_state;
        this.medicine_name = medicine_name;
        this.et_medicineForm = et_medicineForm;
        this.medicineTodayResult = medicineTodayResult;
    }

    public String getMedicine_time() {
        return medicine_time;
    }

    public void setMedicine_time(String medicine_time) {
        this.medicine_time = medicine_time;
    }

    public String getMedicine_state() {
        return medicine_state;
    }

    public void setMedicine_state(String medicine_state) {
        this.medicine_state = medicine_state;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public String getEt_medicineForm() {
        return et_medicineForm;
    }

    public void setEt_medicineForm(String et_medicineForm) {
        this.et_medicineForm = et_medicineForm;
    }

    public String getMedicineTodayResult() {
        return medicineTodayResult;
    }

    public void setMedicineTodayResult(String medicineTodayResult) {
        this.medicineTodayResult = medicineTodayResult;
    }
}
