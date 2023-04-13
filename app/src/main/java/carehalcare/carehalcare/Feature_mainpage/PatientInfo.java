package carehalcare.carehalcare.Feature_mainpage;

import com.google.gson.annotations.SerializedName;


public class PatientInfo {

    @SerializedName("pname") String pname;
    @SerializedName("pbirthDate") String pbirthDate;
    @SerializedName("psex") String psex;
    @SerializedName("disease") String disease;
    @SerializedName("hospital") String hospital;
    @SerializedName("medicine") String medicine;
    @SerializedName("remark") String remark;

   // @SerializedName("userId") String userId; // 보호자 id
   // @SerializedName("id") int id = 0; //간병인, 보호자 식별

    public String getDisease() { return disease; }

    public String getHospital() { return hospital;}

    public String getMedicine() { return medicine;}

    public String getPbirthDate() {return pbirthDate;}

    public String getPname() {return pname;}

    public String getPsex() {return psex;}
    public String getRemark() {return remark;}


    public PatientInfo() {
        this.pname = pname;
        this.pbirthDate = pbirthDate;
        this.psex = psex;
        this.disease = disease;
        this.hospital = hospital;
        this.medicine = medicine;
        this.remark = remark;
    }

}
