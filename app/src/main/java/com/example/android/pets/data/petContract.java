package com.example.android.pets.data;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
// data HERE IS A NEW PACKAGE!!!
// THIS CLASS SHOULDNT INHERIT
public final class petContract {
private petContract(){}

    public static final String CONTENT_AUTHORITY="com.example.android.pets";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_PETS="pets";
    public static final String PATH_ADMIN="admin";
public static final class PetEntry implements BaseColumns{

    public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_PETS);
    /**
     * The MIME type of the {@link #CONTENT_URI} for a list of pets.
     */
    public static final String CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

    /**
     * The MIME type of the {@link #CONTENT_URI} for a single pet.
     */
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;
    public final static String TABLE_NAME="pets";
    public final static String _ID=BaseColumns._ID;
    public final static String COLUMN_PET_NAME="name";
    public final static String COLUMN_PET_BREED="breed";
    public final static String COLUMN_PET_GENDER="gender";
    public final static String COLUMN_PET_WEIGHT="weight";
    public static final int GENDER_UNKNOWN=2;
    public static final int GENDER_MALE=0;
    public static final int GENDER_FEMALE=1;
    public final static Boolean IsGender(int a)
    {
        if(a==1||a==2||a==0)
            return true;
        else
            return false;
    }}
    public static final class ADMIN implements BaseColumns{
        public static final Uri CONTENT_URI_2=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_ADMIN);

        public static final String CONTENT_LIST_TYPE_2 =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ADMIN;

        public static final String CONTENT_ITEM_TYPE_2 =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ADMIN;
    public final static String TABLE_NAME_2="admin";
        public final static String COLUMN_ID_2=BaseColumns._ID;
    public final static String COLUMN_LOGIN="login";
    public final static String COLUMN_PASSWORD="password";

}


}
