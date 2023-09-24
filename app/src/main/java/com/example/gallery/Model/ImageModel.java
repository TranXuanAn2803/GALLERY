package com.example.gallery.Model;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;


public class ImageModel implements Parcelable {
    private int id;
    private Bitmap image;
    private String date;
    private String name;

    public ImageModel() {

    }
    //Constructor
    public ImageModel(int id, Bitmap img, String date) {
        this.id = id;
        this.image = img;
        this.date = date;
    }

    protected ImageModel(Parcel in) {
        id = in.readInt();
        image = in.readParcelable(Bitmap.class.getClassLoader());
        date = in.readString();
    }

    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel in) {
            return new ImageModel(in);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };
    //Lay thong tin id, bitmap, date,
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap img) {
        this.image = image;
    }


    public void setDate(String date) {
        this.date = date;
    }

    public String getName() { return name;}


    public void setName(String name) {

        this.name=name;

    }

    public  String getDate()
    {
        return date;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(image, flags);
        dest.writeString(date);
    }
}
