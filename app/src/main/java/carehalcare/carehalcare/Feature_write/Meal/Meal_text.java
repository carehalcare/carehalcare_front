package carehalcare.carehalcare.Feature_write.Meal;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Meal_text {
    int image;
    Uri photouri;
    String content;
    Bitmap photobitmap;
    String filepath;

    Long id;
    String datetime;
    String uripan;
    String uriuri="";
    public Meal_text(Uri photouri, String content, Long id, String uripan,String datetime,String uriuri) {
        this.photouri = photouri;
        this.content = content;
        this.id = id;
        this.uripan = uripan;
        this.datetime = datetime;
        this.uriuri = uriuri;
    }
    public Meal_text(Bitmap photobitmap, String content, Long id) {
        this.photobitmap = photobitmap;
        this.content = content;
        this.id = id;
    }
    public Meal_text(String filepath, String content, Long id,String datetime,String pathpan) {
        this.filepath = filepath;
        this.content = content;
        this.id = id;
        this.datetime = datetime;
    }

    public String getUriuri() {
        return uriuri;
    }

    public void setUriuri(String uriuri) {
        this.uriuri = uriuri;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}
