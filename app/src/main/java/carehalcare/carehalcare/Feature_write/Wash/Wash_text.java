package carehalcare.carehalcare.Feature_write.Wash;

public class Wash_text {
    String washface;
    String washmouth;
    String nailcare;
    String haircare;
    String bodyscrub;
    String et_bodyscrub;
    String shave;
    String et_washForm;
    String washTodayResult;
    String createdDateTime;

    Long id;
    public Wash_text(String washface, String washmouth, String nailcare, String haircare, String bodyscrub,
                     String et_bodyscrub, String shave, String et_washForm, String createdDateTime) {
        this.washface = washface;
        this.washmouth = washmouth;
        this.nailcare = nailcare;
        this.haircare = haircare;
        this.bodyscrub = bodyscrub;
        this.et_bodyscrub = et_bodyscrub;
        this.shave = shave;
        this.et_washForm = et_washForm;
        this.createdDateTime = createdDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWashface() {
        return washface;
    }

    public void setWashface(String washface) {
        this.washface = washface;
    }

    public String getWashmouth() {
        return washmouth;
    }

    public void setWashmouth(String washmouth) {
        this.washmouth = washmouth;
    }

    public String getNailcare() {
        return nailcare;
    }

    public void setNailcare(String nailcare) {
        this.nailcare = nailcare;
    }

    public String getHaircare() {
        return haircare;
    }

    public void setHaircare(String haircare) {
        this.haircare = haircare;
    }

    public String getBodyscrub() {
        return bodyscrub;
    }

    public void setBodyscrub(String bodyscrub) {
        this.bodyscrub = bodyscrub;
    }

    public String getEt_bodyscrub() {
        return et_bodyscrub;
    }

    public void setEt_bodyscrub(String et_bodyscrub) {
        this.et_bodyscrub = et_bodyscrub;
    }

    public String getShave() {
        return shave;
    }

    public void setShave(String shave) {
        this.shave = shave;
    }

    public String getEt_washForm() {
        return et_washForm;
    }

    public void setEt_washForm(String et_washForm) {
        this.et_washForm = et_washForm;
    }

    public String getWashTodayResult() {
        return washTodayResult;
    }

    public void setWashTodayResult(String washTodayResult) {
        this.washTodayResult = washTodayResult;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
