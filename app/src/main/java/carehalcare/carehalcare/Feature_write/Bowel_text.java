package carehalcare.carehalcare;

public class Bowel_text {
    String bowelcount;
    String bowelTodayResult;
    String et_bowelForm;

    public Bowel_text(String bowelcount, String et_bowelForm,String bowelTodayResult) {
        this.bowelcount = bowelcount;
        this.bowelTodayResult = bowelTodayResult;
        this.et_bowelForm = et_bowelForm;
    }

    public String getBowelcount() {
        return bowelcount;
    }

    public void setBowelcount(String bowelcount) {
        this.bowelcount = bowelcount;
    }

    public String getBowelTodayResult() {
        return bowelTodayResult;
    }

    public void setBowelTodayResult(String bowelTodayResult) {
        this.bowelTodayResult = bowelTodayResult;
    }

    public String getEt_bowelForm() {
        return et_bowelForm;
    }

    public void setEt_bowelForm(String et_bowelForm) {
        this.et_bowelForm = et_bowelForm;
    }
}
