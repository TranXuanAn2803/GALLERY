package com.example.gallery.Model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "newAlbum.db";

    private static final int DB_VERSION = 1;

    private static final String ALBUM_TABLE_NAME = "Album";

    private static final String ALBUM_TABLE_ID_COL = "id";

    private static final String ALBUM_TABLE_NAME_COL = "name";

    private static final String ALBUM_TABLE_PASSWORD_COL = "password";

    private static final String IMAGE_TABLE_NAME = "Image";

    private static final String IMAGE_TABLE_ID_COL = "id";

    private static final String IMAGE_TABLE_FILE_NAME_COL = "FileName";

    private static final String IMAGE_TABLE_DIRECTORY_COL = "Directory";
    private static final String IMAGE_TABLE_DATE_COL = "date";

    private static final String ALBUM_IMAGE_TABLE_NAME = "Album_Image";

    private static final String ALBUM_IMAGE_TABLE_ID_COL = "id";

    private static final String ALBUM_IMAGE_TABLE_ALBUM_ID_COL = "album_id";

    private static final String ALBUM_IMAGE_TABLE_IMAGE_ID_COL = "image_id";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryAlbum = "CREATE TABLE " + ALBUM_TABLE_NAME + "("
                + ALBUM_TABLE_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ALBUM_TABLE_NAME_COL + " TEXT,"
                + ALBUM_TABLE_PASSWORD_COL + " TEXT" + ")";

        String queryImage = "CREATE TABLE " + IMAGE_TABLE_NAME + " ("
                + IMAGE_TABLE_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + IMAGE_TABLE_FILE_NAME_COL + " TEXT,"
                + IMAGE_TABLE_DIRECTORY_COL + " TEXT,"
                + IMAGE_TABLE_DATE_COL + " TEXT" + ")";
        String queryALbumImage = "CREATE TABLE " + ALBUM_IMAGE_TABLE_NAME + " ("
                + ALBUM_IMAGE_TABLE_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ALBUM_IMAGE_TABLE_ALBUM_ID_COL + " INTEGER REFERENCES " + ALBUM_TABLE_NAME + ","
                + ALBUM_IMAGE_TABLE_IMAGE_ID_COL + " INTEGER REFERENCES " + IMAGE_TABLE_NAME + ")";
        db.execSQL(queryAlbum);
        db.execSQL(queryImage);
        db.execSQL(queryALbumImage);
    }

    //Them mot album
    public void addAlbum(String albumName, String albumPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ALBUM_TABLE_NAME_COL, albumName);
        values.put(ALBUM_TABLE_PASSWORD_COL, albumPassword);
        db.insert(ALBUM_TABLE_NAME, null, values);
        db.close();
    }

    //Cap nhat ten album
    public void updateAlbumName(long albumId, String albumName) {
        // calling a method to get writable database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String albumIdStr = Long.toString(albumId);
        // on below line we are passing all values
        // along with its key and value pair.
        values.put(ALBUM_TABLE_NAME_COL, albumName);
        db.update(ALBUM_TABLE_NAME, values, "id=?", new String[]{albumIdStr});
        db.close();
    }

    //Xoa album
    public void deleteAlbum(long albumId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String albumIdStr = Long.toString(albumId);
        db.delete(ALBUM_TABLE_NAME, "id=?", new String[]{albumIdStr});
        db.close();
    }

    //Them mot anh
    public long addImage(String fileName, String directory, String date) {
        //1 anh duoc luu gom thong tin ten file, ten duong dan va ngay them vao co so du lieu

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IMAGE_TABLE_FILE_NAME_COL, fileName);
        values.put(IMAGE_TABLE_DIRECTORY_COL, directory);
        values.put(IMAGE_TABLE_DATE_COL, date);
        //them thong tin moi cua anh vao database
        long imageId = db.insert(IMAGE_TABLE_NAME, null, values);
        return imageId;
    }

    //Them mot album_image
    public void addAlbumImage(long albumId, long imageId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ALBUM_IMAGE_TABLE_ALBUM_ID_COL, albumId);
        values.put(ALBUM_IMAGE_TABLE_IMAGE_ID_COL, imageId);
        db.insert(ALBUM_IMAGE_TABLE_NAME, null, values);
        db.close();
    }

    //cap nhat anh
    public void updateImage(long imageId, String fileName, String directory)
    {
        // calling a method to get writable database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // on below line we are passing all values
        // along with its key and value pair.
        String imageIdStr = Long.toString(imageId);
        values.put(IMAGE_TABLE_FILE_NAME_COL, fileName);
        values.put(IMAGE_TABLE_DIRECTORY_COL,directory);
        db.update(IMAGE_TABLE_NAME, values, "id=?", new String[]{imageIdStr});
        db.close();
    }
    //xoa mot anh
    public void deleteImage(long imageId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String imageIdStr = Long.toString(imageId);
        //xóa thêm id album
        db.delete(IMAGE_TABLE_NAME, "id=?", new String[]{imageIdStr});
        db.close();
    }
    //lay so luong anh cua mot album
    public int getAlbumImageCount(long albumId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorImages = db.rawQuery("SELECT * FROM "
                + ALBUM_IMAGE_TABLE_NAME + " alim WHERE alim." + ALBUM_IMAGE_TABLE_ALBUM_ID_COL + " = " + albumId, null);
        int count = cursorImages.getCount();
        return count;
    }
    //lay anh dau tien
    public ImageModel getFirstAlbumImage(long albumId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorImages = db.rawQuery("SELECT * FROM " + IMAGE_TABLE_NAME + " im, "
                + ALBUM_IMAGE_TABLE_NAME + " alim WHERE im." + IMAGE_TABLE_ID_COL + " = alim." + ALBUM_IMAGE_TABLE_IMAGE_ID_COL
                + " AND alim." + ALBUM_IMAGE_TABLE_ALBUM_ID_COL + " = " + albumId, null);
        ImageModel newImage = new ImageModel();
        if (cursorImages.moveToFirst()) {
            //doc cot directory va cot filename
            Bitmap bitmapImage = loadImageFromStorage(cursorImages.getString(2), cursorImages.getString(1));
            //doc cot image_id, bitmap va date
            newImage = new ImageModel(cursorImages.getInt(6),
                    bitmapImage, cursorImages.getString(3));
        }
        cursorImages.close();
        return newImage;
    }
    //lay so luong album
    public int getAlbumCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorImages = db.rawQuery("SELECT * FROM " + ALBUM_TABLE_NAME, null);
        int count = cursorImages.getCount();
        return count;
    }

    //Tao album timeline
    public void addAlbumTimelineAndFavorites() {
        long count = getAlbumCount();
        if (count == 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values1 = new ContentValues();
            values1.put(ALBUM_TABLE_NAME_COL, "Recents");
            values1.put(ALBUM_TABLE_PASSWORD_COL, "");
            ContentValues values2 = new ContentValues();
            values2.put(ALBUM_TABLE_NAME_COL, "Favorites");
            values2.put(ALBUM_TABLE_PASSWORD_COL, "");
            db.insert(ALBUM_TABLE_NAME, null, values1);
            db.insert(ALBUM_TABLE_NAME, null, values2);
            db.close();
        }
    }

    public ArrayList<AlbumModel> readAlbums() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorAlbums = db.rawQuery("SELECT * FROM " + ALBUM_TABLE_NAME, null);

        ArrayList<AlbumModel> albumModalArrayList = new ArrayList<>();

        if (cursorAlbums.moveToFirst()) {
            do {
                AlbumModel newAlbum = new AlbumModel(cursorAlbums.getInt(0),
                        cursorAlbums.getString(1), cursorAlbums.getString(2));
                albumModalArrayList.add(newAlbum);
                System.out.println(newAlbum.getName());
            } while (cursorAlbums.moveToNext());

        }
        cursorAlbums.close();
        return albumModalArrayList;
    }

    public ArrayList<ImageModel> readAlbumImage(long albumId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ImageModel> imageModelArrayList;
        Cursor cursorImages = db.rawQuery("SELECT * FROM " + IMAGE_TABLE_NAME + " im, "
                + ALBUM_IMAGE_TABLE_NAME + " alim WHERE im." + IMAGE_TABLE_ID_COL + " = alim." + ALBUM_IMAGE_TABLE_IMAGE_ID_COL
                + " AND alim." + ALBUM_IMAGE_TABLE_ALBUM_ID_COL + " = " + albumId, null);
        imageModelArrayList = new ArrayList<>();
        if (cursorImages.moveToFirst()) {
            do {
                //doc cot directory va cot filename
                Bitmap bitmapImage = loadImageFromStorage(cursorImages.getString(2), cursorImages.getString(1));
                //doc cot image_id, bitmap va date
                ImageModel newImage = new ImageModel(cursorImages.getInt(6),
                        bitmapImage, cursorImages.getString(3));
                imageModelArrayList.add(newImage);
            } while (cursorImages.moveToNext());
        }
        cursorImages.close();
        return imageModelArrayList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + ALBUM_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + IMAGE_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ALBUM_IMAGE_TABLE_NAME);
            onCreate(db);
        }
    }
    //Load image len tu duong danh thanh dang bitmap
    private Bitmap loadImageFromStorage(String directory, String fileName) {
        Bitmap b = null;
        try {
            File f = new File(directory, fileName);
            //load len va decode thanh bitmap
            b = BitmapFactory.decodeStream(new FileInputStream(f));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return b;

    }
    //Them mot anh vao timeline
    public boolean addNewImageToTimeline(String fileName, String directory, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(IMAGE_TABLE_FILE_NAME_COL, fileName);
        cv.put(IMAGE_TABLE_DIRECTORY_COL, directory);
        cv.put(IMAGE_TABLE_DATE_COL, date);
        long insert = db.insert(IMAGE_TABLE_NAME, null, cv);
        db.close();
        if (insert == -1) return false;
        else return true;

    }
    //Doc thong tin anh trong thu muc timeine len
    public ArrayList<ImageModel> readTimeline() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorImage = db.rawQuery("SELECT * FROM " + IMAGE_TABLE_NAME, null);

        ArrayList<ImageModel> albumModalArrayList = new ArrayList<>();

        if (cursorImage.moveToFirst()) {
            //Load anh tu duong dan len thanh dang bitmap
            do {
                Bitmap bitmapImage = loadImageFromStorage(cursorImage.getString(2), cursorImage.getString(1));
                ImageModel newAlbum = new ImageModel(cursorImage.getInt(0),
                        bitmapImage, cursorImage.getString(3));
                albumModalArrayList.add(newAlbum);
            } while (cursorImage.moveToNext());

        }
        cursorImage.close();
        return albumModalArrayList;
    }
    //update thong tin anh cua thu muc timeline
    public boolean updateImageInTimeline(String id, String fileName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(IMAGE_TABLE_FILE_NAME_COL, fileName);
        long update = db.update(IMAGE_TABLE_NAME, cv, IMAGE_TABLE_ID_COL + " = ?", new String[]{id});
        db.close();
        if (update == -1) return false;
        else return true;

    }

    public ArrayList<ImageModel> readTimeline(int offset, int limit, ArrayList<ImageModel> listCurrent) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorImage = db.rawQuery("SELECT * FROM " + IMAGE_TABLE_NAME + " LIMIT " + limit + " OFFSET " + offset, null);

        if (cursorImage.moveToFirst()) {
            do {
                Bitmap bitmapImage = loadImageFromStorage(cursorImage.getString(2), cursorImage.getString(1));
                ImageModel newAlbum = new ImageModel(cursorImage.getInt(0),
                        bitmapImage, cursorImage.getString(2));
                listCurrent.add(newAlbum);
            } while (cursorImage.moveToNext());
        }
        cursorImage.close();
        return listCurrent;
    }
    //dem so luong anh trong thu muc timeline
    public int getCountTimeLine() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + IMAGE_TABLE_NAME, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

}