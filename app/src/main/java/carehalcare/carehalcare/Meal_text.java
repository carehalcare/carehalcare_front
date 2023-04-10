package carehalcare.carehalcare;

import android.graphics.Bitmap;
import android.net.Uri;

public class Meal_text {
    int image;
    Uri photouri;
    String content;
    Bitmap photobitmap;

    public Meal_text(Uri photouri, String content, String uri) {
        this.photouri = photouri;
        this.content = content;
    }
    public Meal_text(Bitmap photobitmap, String content) {
        this.photobitmap = photobitmap;
        this.content = content;
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
