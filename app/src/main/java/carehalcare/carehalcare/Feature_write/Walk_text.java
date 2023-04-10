package carehalcare.carehalcare;
import android.graphics.Bitmap;
import android.net.Uri;

public class Walk_text {
    int image_walk;
    Uri photouri_walk;
    String content_walk;
    Bitmap photobitmap_walk;

    public Walk_text(Uri photouri_walk, String content_walk, String uri_walk) {
        this.photouri_walk = photouri_walk;
        this.content_walk = content_walk;
    }
    public Walk_text(Bitmap photobitmap_walk, String content_walk) {
        this.photobitmap_walk = photobitmap_walk;
        this.content_walk = content_walk;
    }

    public Bitmap getPhotobitmap_walk() {
        return photobitmap_walk;
    }

    public void setPhotobitmap_walk(Bitmap photobitmap_walk) {
        this.photobitmap_walk = photobitmap_walk;
    }

    public Uri getPhotouri_walk() {
        return photouri_walk;
    }

    public void setPhotouri_walk(Uri photouri_walk) {
        this.photouri_walk = photouri_walk;
    }

    public String getContent_walk() {
        return content_walk;
    }

    public void setContent_walk(String content_walk) {
        this.content_walk = content_walk;
    }

    public int getImage_walk() {
        return image_walk;
    }

    public void setImage_walk(int image_walk) {
        this.image_walk = image_walk;
    }
}
