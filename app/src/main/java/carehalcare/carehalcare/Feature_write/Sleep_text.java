package carehalcare.carehalcare.Feature_write;

public class Sleep_text {
    String sleepstate;
    String et_sleepForm;
    String sleepTodayResult;

    public Sleep_text(String sleepstate, String et_sleepForm, String sleepTodayResult) {
        this.sleepstate = sleepstate;
        this.et_sleepForm = et_sleepForm;
        this.sleepTodayResult = sleepTodayResult;
    }

    public String getSleepstate() {
        return sleepstate;
    }

    public void setSleepstate(String sleepstate) {
        this.sleepstate = sleepstate;
    }

    public String getEt_sleepForm() {
        return et_sleepForm;
    }

    public void setEt_sleepForm(String et_sleepForm) {
        this.et_sleepForm = et_sleepForm;
    }

    public String getSleepTodayResult() {
        return sleepTodayResult;
    }

    public void setSleepTodayResult(String sleepTodayResult) {
        this.sleepTodayResult = sleepTodayResult;
    }
}
