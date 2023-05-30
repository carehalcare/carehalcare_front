package carehalcare.carehalcare.Feature_write.Walk;
import android.graphics.Bitmap;
import android.net.Uri;

public class Walk_text {
    int image;
    Uri photouri;
    Bitmap photobitmap;

    Long id;
    String datetime;
    String filepath;

    String uripan;
    public Walk_text(Uri photouri, Long id, String uripan,String datetime,String uriuri) {
        this.photouri = photouri;
        this.id = id;
        this.datetime = datetime;
        this.uripan = uripan;
    }
    public Walk_text(Bitmap photobitmap, Long id) {
        this.photobitmap = photobitmap;
        this.id = id;
    }
    public Walk_text(String filepath, Long id,String datetime,String pathpan) {
        this.filepath = filepath;
        this.id = id;
        this.datetime = datetime;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUripan() {
        return uripan;
    }

    public void setUripan(String uripan) {
        this.uripan = uripan;
    }

    public Bitmap getPhotobitmap() {
        return photobitmap;
    }

    public void setPhotobitmap(Bitmap photobitmap) {
        this.photobitmap = photobitmap;
    }

    public Uri getPhotouri() {
        return photouri;
    }

    public void setPhotouri(Uri photouri) {
        this.photouri = photouri;
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
