package carehalcare.carehalcare;

public class Active_text {
    private String activeTodayResult;
    private String jahal;
    private String bohang;
    private String change;

    public Active_text(String jahal, String bohang, String change,String activeTodayResult) {
        this.activeTodayResult = activeTodayResult;
        this.jahal = jahal;
        this.bohang = bohang;
        this.change = change;
    }

    public String getJahal() {
        return jahal;
    }

    public void setJahal(String jahal) {
        this.jahal = jahal;
    }

    public String getBohang() {
        return bohang;
    }

    public void setBohang(String bohang) {
        this.bohang = bohang;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getactiveTodayResult() {
        return activeTodayResult;
    }

    public void setactiveTodayResult(String id) {
        this.activeTodayResult = activeTodayResult;
    }


}
