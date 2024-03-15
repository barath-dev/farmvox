package com.example.loginsqllite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String USER_DATABASE_NAME = "User.db";

    public static final String ORDERS_TABLE_NAME = "orders";


    public static final String CART_TABLE_NAME = "cart";





    private static final int USER_DATABASE_VERSION = 6; // Change this version number

    // Change this version number

    // User database table and columns
    public static final String USER_TABLE_NAME = "users";
    public static final String USER_COL_USERNAME = "username";
    public static final String USER_COL_PASSWORD = "password";
    public static final String USER_COL_ROLE = "Role";
    public static final String USER_COL_LATITUDE = "latitude";
    public static final String USER_COL_LONGITUDE = "longitude";

    // Updated product database table and columns
    public static final String PRODUCT_TABLE_NAME = "products";
    public static final String PRODUCT_COL_USERNAME = "username";

    public static final String ORDERS_COL_DELIERYBOY = "deliveryBoy";

    public static final String ORDER_COL_USERNAME = "username";
    public static final String ORDER_COL_ROLE = "role";
    public static final String ORDER_COL_LATITUDE_PICKUP = "latitude_pickup";
    public static final String ORDER_COL_LONGITUDE_PICKUP = "longitude_pickup";
    public static final String ORDER_COL_LATITUDE_DEST = "latitude_dest";
    public static final String ORDER_COL_LONGITUDE_DEST = "longitude_dest";
    public static final String ORDER_COL_PRODUCT_NAME = "product_name";
    public static final String ORDER_COL_PRODUCT_PRICE = "product_price";
    public static final String ORDER_COL_PRODUCT_QUANTITY = "product_quantity";

    public static final String PRODUCT_COL_LATITUDE = "latitude";
    public static final String PRODUCT_COL_LONGITUDE = "longitude";
    public static final String PRODUCT_COL_PRODUCT_NAME = "product_name";
    public static final String PRODUCT_COL_PRODUCT_PRICE = "product_price";
    public static final String PRODUCT_COL_PRODUCT_QUANTITY = "product_quantity";

    public static final String PRODUCT_COL_PRODUCT_UNIT = "product_unit";

    public static final String CART_COL_USERNAME = "username";

    public static final String CART_COL_PRODUCT_NAME = "cropName";
    public static final String CART_COL_PRODUCT_PRICE = "price";
    public static final String CART_COL_PRODUCT_QUANTITY = "quantity";

    public static final String CART_COL_PRODUCT_UNIT = "unit";

    public static  final String CART_COL_FARMER_NAME = "fName";

    public static final String DISPATCHERS_TABLE_NAME = "dispatchers";
    public static final String DISPATCHERS_COL_USERNAME = "username";
    public static final String DISPATCHERS_COL_LATITUDE = "latitude";
    public static final String DISPATCHERS_COL_LONGITUDE = "longitude";

    public static final String DISPATCHERS_COL_CURRENT_ORDER = "current_order";

    public static final String DISPATCHERS_COL_STATUS = "status";

    public static final String DISPATCHERS_COL_DELIVERY_COUNT = "delivery_count";
    private static final String ORDER_COL_F_NAME = "farmer_name";
    private static final String ORDER_COL_PRODUCT_UNIT = "unit";

    private static  final String TEM_TABLE_NAME = "tem";


    public DBHelper(Context context) {
        super(context, USER_DATABASE_NAME, null, USER_DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the users table
        db.execSQL("create Table " + USER_TABLE_NAME + " (" + USER_COL_USERNAME + " TEXT primary key, " + USER_COL_PASSWORD + " TEXT, " + USER_COL_ROLE + " TEXT, " + USER_COL_LATITUDE + " REAL, " + USER_COL_LONGITUDE + " REAL)");

        // Create the products table with updated columns
        db.execSQL("create Table " + PRODUCT_TABLE_NAME + " (" + USER_COL_USERNAME + " TEXT, "  + PRODUCT_COL_LATITUDE + " REAL, " + PRODUCT_COL_LONGITUDE + " REAL, " + PRODUCT_COL_PRODUCT_NAME + " TEXT, "+ PRODUCT_COL_PRODUCT_UNIT + " TEXT, " + PRODUCT_COL_PRODUCT_PRICE + " REAL, " + PRODUCT_COL_PRODUCT_QUANTITY + " INTEGER)");

        // Create the orders table with updated columns
        db.execSQL("create Table " + ORDERS_TABLE_NAME + " (" + ORDER_COL_USERNAME + " TEXT, " + ORDER_COL_LATITUDE_PICKUP + " REAL, " + ORDER_COL_LONGITUDE_PICKUP + " REAL, " + ORDER_COL_LATITUDE_DEST + " REAL, " + ORDER_COL_LONGITUDE_DEST + " REAL, " + ORDER_COL_PRODUCT_PRICE + " REAL, " + ORDER_COL_PRODUCT_QUANTITY + " INTEGER, " + ORDER_COL_F_NAME + " TEXT, " + ORDER_COL_PRODUCT_NAME + " TEXT, " + ORDER_COL_PRODUCT_UNIT + " TEXT)");

        db.execSQL("create Table " + CART_TABLE_NAME + " (" + CART_COL_USERNAME + " TEXT, " + CART_COL_PRODUCT_NAME + " TEXT, " + CART_COL_PRODUCT_PRICE + " REAL, " + CART_COL_FARMER_NAME + " TEXT, " + CART_COL_PRODUCT_UNIT + " REAL, " + CART_COL_PRODUCT_QUANTITY + " INTEGER)");

        db.execSQL("create Table " + DISPATCHERS_TABLE_NAME + " (" + DISPATCHERS_COL_USERNAME + " TEXT primary key, " + DISPATCHERS_COL_LATITUDE + " REAL, " + DISPATCHERS_COL_LONGITUDE + " REAL, " + DISPATCHERS_COL_CURRENT_ORDER + " TEXT, " + DISPATCHERS_COL_STATUS + " TEXT, " + DISPATCHERS_COL_DELIVERY_COUNT + " INTEGER)");

        db.execSQL("create Table " + TEM_TABLE_NAME + " (" + ORDER_COL_USERNAME + " TEXT, " + ORDER_COL_LATITUDE_PICKUP + " REAL, " + ORDER_COL_LONGITUDE_PICKUP + " REAL, " + ORDER_COL_LATITUDE_DEST + " REAL, " + ORDER_COL_LONGITUDE_DEST + " REAL, " + ORDER_COL_PRODUCT_PRICE + " REAL, " + ORDER_COL_PRODUCT_QUANTITY + " INTEGER, " + ORDER_COL_F_NAME + " TEXT, " + ORDER_COL_PRODUCT_NAME + " TEXT, " + ORDER_COL_PRODUCT_UNIT + " TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void clearTEM(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TEM_TABLE_NAME);
    }


    public boolean insertuserdata(String username, String password, String role, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL_USERNAME, username);
        contentValues.put(USER_COL_PASSWORD, password);
        contentValues.put(USER_COL_ROLE, role);
        contentValues.put(USER_COL_LATITUDE, latitude);
        contentValues.put(USER_COL_LONGITUDE, longitude);
        long result = db.insert(USER_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean checkusername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE " + USER_COL_USERNAME + " = ?", new String[]{username});
        return cursor.getCount() > 0;
    }

    public boolean checkusernamepassword(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE " + USER_COL_USERNAME + " = ? AND " + USER_COL_PASSWORD + " = ?", new String[]{username, password});
        return cursor.getCount() > 0;
    }

    public String getUserRole(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        String role;
        Cursor cursor = db.rawQuery("SELECT " + USER_COL_ROLE + " FROM " + USER_TABLE_NAME + " WHERE " + USER_COL_USERNAME + " = ?", new String[]{username});
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int roleIndex = cursor.getColumnIndex(USER_COL_ROLE);
                    if (roleIndex >= 0) {
                        role = cursor.getString(roleIndex);
                    } else {
                        role = "Role column not found";
                    }
                } else {
                    role = "No role found for this user";
                }
            }catch (SQLiteException e){
                role = "Error: " + e.getMessage();
            }
        } else {
            role = "Cursor is null";
        }
        return role;
    }

    public Cursor getuserdetails(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] columns = {
                USER_COL_USERNAME,
                USER_COL_PASSWORD,
                USER_COL_ROLE,
                USER_COL_LATITUDE,
                USER_COL_LONGITUDE
        };

        // Define the WHERE clause to find the user by username
        String selection = USER_COL_USERNAME + " = ?";
        String[] selectionArgs = {username};

        // Execute the query and return the Cursor

        return db.query(USER_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
    }

    // Product related methods

    public boolean insertproductdata(
            String username,
            double latitude,
            double longitude,
            String selectedProduct,
            String productUnit,
            double price,
            int quantity) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_COL_USERNAME, username);
        contentValues.put(PRODUCT_COL_LATITUDE, latitude);
        contentValues.put(PRODUCT_COL_PRODUCT_UNIT, productUnit);
        contentValues.put(PRODUCT_COL_LONGITUDE, longitude);
        contentValues.put(PRODUCT_COL_PRODUCT_NAME, selectedProduct);
        contentValues.put(PRODUCT_COL_PRODUCT_PRICE, price);
        contentValues.put(PRODUCT_COL_PRODUCT_QUANTITY, quantity);
        long result = db.insert(PRODUCT_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean updateproductdata(
            String username,
            String password,
            double latitude,
            double longitude,
            String selectedProduct,
            double price,
            int quantity) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_COL_USERNAME, username);
        contentValues.put(PRODUCT_COL_LATITUDE, latitude);
        contentValues.put(PRODUCT_COL_LONGITUDE, longitude);
        contentValues.put(PRODUCT_COL_PRODUCT_NAME, selectedProduct);
        contentValues.put(PRODUCT_COL_PRODUCT_PRICE, price);
        contentValues.put(PRODUCT_COL_PRODUCT_QUANTITY, quantity);
        long result = db.update(PRODUCT_TABLE_NAME, contentValues, PRODUCT_COL_USERNAME + " = ?", new String[]{username});
        return result != -1;
    }

    public boolean createOrder(
            String username,
            double latitude_dest,
            double longitude_dest,
            double latitude_source,
            double longitude_source,
            double price,
            String farmerName,
            int quantity,
            String cropName,
            String unit) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ORDER_COL_USERNAME, username);
        contentValues.put(ORDER_COL_LATITUDE_PICKUP, latitude_source);
        contentValues.put(ORDER_COL_LONGITUDE_PICKUP, longitude_source);
        contentValues.put(ORDER_COL_LATITUDE_DEST, latitude_dest);
        contentValues.put(ORDER_COL_LONGITUDE_DEST, longitude_dest);
        contentValues.put(ORDER_COL_PRODUCT_PRICE, price);
        contentValues.put(ORDER_COL_PRODUCT_QUANTITY, quantity);
        contentValues.put(ORDER_COL_F_NAME,farmerName);
        contentValues.put(ORDER_COL_PRODUCT_NAME,cropName);
        contentValues.put(ORDER_COL_PRODUCT_UNIT,unit);
        long result = db.insert(ORDERS_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean createCheckOut(
            String username,
            double latitude_dest,
            double longitude_dest,
            double latitude_source,
            double longitude_source,
            double price,
            String farmerName,
            int quantity,
            String cropName,
            String unit) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ORDER_COL_USERNAME, username);
        contentValues.put(ORDER_COL_LATITUDE_PICKUP, latitude_source);
        contentValues.put(ORDER_COL_LONGITUDE_PICKUP, longitude_source);
        contentValues.put(ORDER_COL_LATITUDE_DEST, latitude_dest);
        contentValues.put(ORDER_COL_LONGITUDE_DEST, longitude_dest);
        contentValues.put(ORDER_COL_PRODUCT_PRICE, price);
        contentValues.put(ORDER_COL_PRODUCT_QUANTITY, quantity);
        contentValues.put(ORDER_COL_F_NAME,farmerName);
        contentValues.put(ORDER_COL_PRODUCT_NAME,cropName);
        contentValues.put(ORDER_COL_PRODUCT_UNIT,unit);
        long result = db.insert(TEM_TABLE_NAME, null, contentValues);
        return result != -1;
    }


    public boolean pickupOrder(
            String username,
            String status,
            double latitude_dest,
            double longitude_dest,
            double latitude_source,
            double longitude_source,
            String deliveryBoy,
            double price,
            int quantity) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_COL_USERNAME, username);
        contentValues.put(PRODUCT_COL_LATITUDE, latitude_dest);
        contentValues.put(PRODUCT_COL_LONGITUDE, longitude_dest);
        contentValues.put(PRODUCT_COL_LATITUDE, latitude_source);
        contentValues.put(PRODUCT_COL_LONGITUDE, longitude_source);
        contentValues.put(ORDERS_COL_DELIERYBOY, deliveryBoy);
        contentValues.put(PRODUCT_COL_PRODUCT_PRICE, price);
        contentValues.put(PRODUCT_COL_PRODUCT_QUANTITY, quantity);
        long result = db.update(PRODUCT_TABLE_NAME, contentValues, PRODUCT_COL_USERNAME + " = ?", new String[]{username});
        return result != -1;
    }

    public boolean DeliverOrder(
            String username,
            String status,
            double latitude_dest,
            double longitude_dest,
            double latitude_source,
            double longitude_source,
            double price,
            int quantity) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_COL_USERNAME, username);
        contentValues.put(PRODUCT_COL_LATITUDE, latitude_dest);
        contentValues.put(PRODUCT_COL_LONGITUDE, longitude_dest);
        contentValues.put(PRODUCT_COL_LATITUDE, latitude_source);
        contentValues.put(PRODUCT_COL_LONGITUDE, longitude_source);
        contentValues.put(PRODUCT_COL_PRODUCT_PRICE, price);
        contentValues.put(PRODUCT_COL_PRODUCT_QUANTITY, quantity);
        long result = db.update(PRODUCT_TABLE_NAME, contentValues, PRODUCT_COL_USERNAME + " = ?", new String[]{username});
        return result != -1;
    }

    //get order details
    public Cursor getorderdetails(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] columns = {
                PRODUCT_COL_USERNAME,
                PRODUCT_COL_LATITUDE,
                PRODUCT_COL_LONGITUDE,
                ORDERS_COL_DELIERYBOY,
                PRODUCT_COL_PRODUCT_PRICE,
                PRODUCT_COL_PRODUCT_QUANTITY
        };

        // Define the WHERE clause to find the user by username
        String selection = PRODUCT_COL_USERNAME + " = ?";
        String[] selectionArgs = {username};

        // Execute the query and return the Cursor
        Cursor cursor = db.query(ORDERS_TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        return cursor;
    }

    public Cursor getCoordinatesOfRole(String role) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] columns = {
                USER_COL_LATITUDE,
                USER_COL_LONGITUDE
        };

        // Define the WHERE clause to find the user by username
        String selection = USER_COL_USERNAME + " = ?";
        String[] selectionArgs = {role};

        // Execute the query and return the Cursor
        Cursor cursor = db.query(USER_TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        return cursor;
    }

    public Cursor getCrops(String username){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                PRODUCT_COL_USERNAME,
                PRODUCT_COL_PRODUCT_NAME,
                PRODUCT_COL_PRODUCT_PRICE,
                PRODUCT_COL_PRODUCT_QUANTITY,
                PRODUCT_COL_PRODUCT_UNIT
        };

        String selection = PRODUCT_COL_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(PRODUCT_TABLE_NAME,columns,selection,selectionArgs,null,null,null);

        return cursor;
    }

    public String deleteCrop(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(PRODUCT_TABLE_NAME,PRODUCT_COL_USERNAME + " = ?",new String[]{username});

        if(res > 0){
            return "Deleted";
        }else{
            return String.valueOf(res);
        }
    }

    //create only if the product is not already in the cart
   public String createCart(String username, String product_name, String product_quantity, String product_price,String product_unit,String farmer_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CART_COL_USERNAME, username);
        contentValues.put(CART_COL_PRODUCT_NAME, product_name);
        contentValues.put(CART_COL_PRODUCT_PRICE, product_price);
        contentValues.put(CART_COL_PRODUCT_QUANTITY, product_quantity);
        contentValues.put(CART_COL_FARMER_NAME, farmer_name);
        contentValues.put(CART_COL_PRODUCT_UNIT, product_unit);

        //check if the product is already in the cart
        Cursor cursor = db.rawQuery("SELECT * FROM " + CART_TABLE_NAME + " WHERE " + CART_COL_USERNAME + " = ? AND " + CART_COL_PRODUCT_NAME + " = ? AND " + CART_COL_PRODUCT_QUANTITY + " = ? AND " + CART_COL_PRODUCT_PRICE + " = ?", new String[]{username, product_name, product_quantity, product_price});
        if(cursor.getCount() > 0){
            return "Product already in cart";
        }else{
            long result = db.insert(CART_TABLE_NAME, null, contentValues);
            if (result == -1) {
                return "Failed to add to cart";
            } else {
                return "Added to cart";
            }
        }


    }

    public Cursor getCart(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] columns = {
                CART_COL_USERNAME,
                CART_COL_PRODUCT_NAME,
                CART_COL_PRODUCT_PRICE,
                CART_COL_PRODUCT_QUANTITY,
                CART_COL_FARMER_NAME,
                CART_COL_PRODUCT_UNIT
        };

        // Define the WHERE clause to find the user by username
        String selection = CART_COL_USERNAME + " = ?";
        String[] selectionArgs = {username};

        try {
            // Execute the query and return the Cursor
            Cursor cursor = db.query(CART_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            return cursor;
        } catch (SQLiteException e) {
            return null;
        }
    }

    public String deleteCartItem(String username, String product_name,String product_quantity, String product_price) {
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(CART_TABLE_NAME, CART_COL_USERNAME + " = ? AND " + CART_COL_PRODUCT_NAME + " = ? AND " + CART_COL_PRODUCT_QUANTITY + " = ? AND " + CART_COL_PRODUCT_PRICE + " = ?", new String[]{username, product_name, product_quantity, product_price});
        db.close();
        if (res > 0) {
            return "Deleted";
        } else {
            return String.valueOf(res);
        }
    }

    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] columns = {
                PRODUCT_COL_USERNAME,
                PRODUCT_COL_LATITUDE,
                PRODUCT_COL_LONGITUDE,
                PRODUCT_COL_PRODUCT_NAME,
                PRODUCT_COL_PRODUCT_PRICE,
                PRODUCT_COL_PRODUCT_QUANTITY,
                PRODUCT_COL_PRODUCT_UNIT
        };


        String selection = "CAST(product_quantity AS INTEGER) > ?";
        String[] selectionArgs = {"0"};
        Cursor cursor = db.query(PRODUCT_TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }


    public void createDeliveryBoy(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DISPATCHERS_COL_USERNAME, username);
        contentValues.put(DISPATCHERS_COL_STATUS, "Available");
        contentValues.put(DISPATCHERS_COL_DELIVERY_COUNT, 0);
        long result = db.insert(DISPATCHERS_TABLE_NAME, null, contentValues);
    }

    public  Cursor getPendingOrders(String username){
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] columns = {
                ORDER_COL_USERNAME,
                ORDER_COL_ROLE,
                ORDER_COL_LATITUDE_PICKUP,
                ORDER_COL_LONGITUDE_PICKUP,
                ORDER_COL_LATITUDE_DEST,
                ORDER_COL_LONGITUDE_DEST,
                ORDER_COL_PRODUCT_NAME,
                ORDER_COL_PRODUCT_PRICE,
                ORDER_COL_PRODUCT_QUANTITY,
                ORDER_COL_PRODUCT_UNIT,
                ORDER_COL_PRODUCT_NAME
        };

        // Define the WHERE clause to find the user by username
        String selection = PRODUCT_COL_USERNAME + " = ?";
        String[] selectionArgs = {username};

        // Execute the query and return the Cursor
        Cursor cursor = db.query(ORDERS_TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        return cursor;
    }

    public LatLng getUserLocation(String username){
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] columns = {
                USER_COL_LATITUDE,
                USER_COL_LONGITUDE
        };

        // Define the WHERE clause to find the user by username
        String selection = USER_COL_USERNAME + " = ?";
        String[] selectionArgs = {username};

        // Execute the query and return the Cursor
        Cursor cursor = db.query(USER_TABLE_NAME, columns, selection, selectionArgs, null, null, null);


        if(cursor != null){
            cursor.moveToFirst();
            @SuppressLint("Range") double latitude = cursor.getDouble(cursor.getColumnIndex(USER_COL_LATITUDE));
            @SuppressLint("Range") double longitude = cursor.getDouble(cursor.getColumnIndex(USER_COL_LONGITUDE));
            return new LatLng(latitude,longitude);
        }else{
            return null;
        }
    }

    public ArrayList<LatLng> getAllFarmerLocations(){
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] columns = {
                USER_COL_LATITUDE,
                USER_COL_LONGITUDE
        };


        String selection = USER_COL_ROLE + " = ?";

        String[] selectionArgs = {"Farmer"};

        // Execute the query and return the Cursor
        Cursor cursor = db.query(USER_TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        ArrayList<LatLng> farmerLocations = new ArrayList<>();

        if(cursor != null){
            while(cursor.moveToNext()){
                @SuppressLint("Range") double latitude = cursor.getDouble(cursor.getColumnIndex(USER_COL_LATITUDE));
                @SuppressLint("Range") double longitude = cursor.getDouble(cursor.getColumnIndex(USER_COL_LONGITUDE));
                LatLng latLng = new LatLng(latitude,longitude);
                farmerLocations.add(latLng);
            }
        }
        return farmerLocations;
    }

    public ArrayList<String> getAllUsernames() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] columns = {
                USER_COL_USERNAME
        };


        // Execute the query and return the Cursor
        Cursor cursor = db.query(USER_TABLE_NAME, columns, null, null, null, null, null);

        ArrayList<String> usernames = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex(USER_COL_USERNAME));
                usernames.add(username);
            }
        }
        return usernames;
    }

    public Cursor getAllProductsForFarmer(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] columns = {
                PRODUCT_COL_USERNAME,
                PRODUCT_COL_LATITUDE,
                PRODUCT_COL_LONGITUDE,
                PRODUCT_COL_PRODUCT_NAME,
                PRODUCT_COL_PRODUCT_PRICE,
                PRODUCT_COL_PRODUCT_QUANTITY,
                PRODUCT_COL_PRODUCT_UNIT
        };

        // Define the WHERE clause to find the user by username
        String selection = PRODUCT_COL_USERNAME + " = ?";
        String[] selectionArgs = {username};

        // Execute the query and return the Cursor
        Cursor cursor = db.query(PRODUCT_TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        return cursor;
    }

    public boolean isFarmer(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE " + USER_COL_USERNAME + " = ? AND " + USER_COL_ROLE + " = ?", new String[]{username, "Farmer"});
        return cursor.getCount() > 0;
    }

    public  Cursor getTEM(String username){
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] columns = {
                ORDER_COL_USERNAME,
                ORDER_COL_LATITUDE_PICKUP,
                ORDER_COL_LONGITUDE_PICKUP,
                ORDER_COL_LATITUDE_DEST,
                ORDER_COL_LONGITUDE_DEST,
                ORDER_COL_PRODUCT_PRICE,
                ORDER_COL_PRODUCT_QUANTITY,
                ORDER_COL_F_NAME,
                ORDER_COL_PRODUCT_UNIT,
                ORDER_COL_PRODUCT_NAME
        };

        // Define the WHERE clause to find the user by username
        String selection = PRODUCT_COL_USERNAME + " = ?";
        String[] selectionArgs = {username};

        // Execute the query and return the Cursor

        return db.query(TEM_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
    }
}
