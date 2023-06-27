package carehalcare.carehalcare.Feature_write.Walk;
import android.graphics.Bitmap;
import android.net.Uri;

public class Walk_text {
    int image;
    Uri photouri;
    Bitmap photobitmap;

    Long id;
    String createdDateTime;
    String filepath;
    String uriuri ="";

    String uripan;
    public Walk_text(Uri photouri, Long id, String uripan,String datetime,String uriuri) {
        this.photouri = photouri;
        this.id = id;
        this.createdDateTime = createdDateTime;
        this.uripan = uripan;
        this.uriuri = uriuri;
    }
    public Walk_text(Bitmap photobitmap, Long id) {
        this.photobitmap = photobitmap;
        this.id = id;
    }
    public Walk_text(String filepath, Long id,String datetime,String pathpan) {
        this.filepath = filepath;
        this.id = id;
        this.createdDateTime = createdDateTime;
    }

    public String getUriuri() {
        return uriuri;
    }

    public void setUriuri(String uriuri) {
        this.uriuri = uriuri;
    }

    public String getcreatedDateTime() {
        return createdDateTime;
    }

    public void setcreatedDateTime(String datetime) {
        this.createdDateTime = datetime;
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
