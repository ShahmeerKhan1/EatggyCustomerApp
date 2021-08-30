package com.yummoidmkschinky.customerapp.utiles;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.yummoidmkschinky.customerapp.activity.HomeActivity;
import com.yummoidmkschinky.customerapp.activity.RestaurantsActivity;
import com.yummoidmkschinky.customerapp.model.MenuitemDataItem;

import java.util.ArrayList;
import java.util.List;


public class MyHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "mydatabase.db";
    public static final String TABLE_NAME = "items";
    public static final String ICOL_1 = "ID";
    public static final String ICOL_2 = "RID";
    public static final String ICOL_3 = "PID";
    public static final String ICOL_4 = "title";
    public static final String ICOL_5 = "item_img";
    public static final String ICOL_6 = "cdesc";
    public static final String ICOL_7 = "price";
    public static final String ICOL_8 = "qty";
    public static final String ICOL_9 = "is_customize";
    public static final String ICOL_10 = "is_quantity";
    public static final String ICOL_11 = "is_veg";
    public static final String ICOL_12 = "addonid";
    public static final String ICOL_13 = "addontitel";
    public static final String ICOL_14 = "addonprice";
    SessionManager sessionManager;
    Context contextA;


    public List<MenuitemDataItem> getCData() {
        List<MenuitemDataItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            Cursor c = db.rawQuery("select * from items ", null);
            if (c.getCount() != -1) { //if the row exist then return the id
                while (c.moveToNext()) {
                    MenuitemDataItem item = new MenuitemDataItem();
                    item.setRid(c.getString(1));
                    item.setId(c.getString(2));
                    item.setTitle(c.getString(3));
                    item.setItemImg(c.getString(4));
                    item.setCdesc("" + c.getDouble(5));
                    item.setPrice(c.getDouble(6));
                    item.setQty(c.getInt(7));
                    item.setIsCustomize(c.getInt(8));
                    item.setIsQuantity(c.getString(9));
                    item.setIsVeg(c.getInt(10));
                    item.setAddonID(c.getString(11));
                    item.setAddonTitel(c.getString(12));
                    item.setAddonPrice(c.getString(13));
                    list.add(item);
                }

            }
        } catch (Exception e) {
            Log.e("Error", "-->" + e.toString());

        }
        return list;
    }


    public MyHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        sessionManager = new SessionManager(context);
        contextA = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, RID TEXT , PID TEXT , title TEXT , item_img TEXT , cdesc TEXT, price Double , qty int, is_customize int , is_quantity int , is_veg int , addonid TEXT, addontitel TEXT, addonprice TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(MenuitemDataItem rModel) {

        int isV = isStore(rModel.getRid());
        if (isV != 0 && isV != Integer.parseInt(rModel.getRid())) {
            AlertDialog myQuittingDialogBox = new AlertDialog.Builder(contextA)
                    // set message, title, and icon
                    .setTitle("Items already in cart")
                    .setMessage("Your cart contains items from other restaurant. Would you like to reset your cart for adding items from this restaurant?")

                    .setPositiveButton("Yes", (dialog, whichButton) -> {
                        //your deleting code
                        deleteCard();
                        HomeActivity.getInstance().cartCounter();

                        dialog.dismiss();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .create();

            myQuittingDialogBox.show();
            return false;

        } else {
            if (getID(rModel.getId()) == -1) {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(ICOL_2, rModel.getRid());
                contentValues.put(ICOL_3, rModel.getId());
                contentValues.put(ICOL_4, rModel.getTitle());
                contentValues.put(ICOL_5, rModel.getItemImg());
                contentValues.put(ICOL_6, rModel.getCdesc());
                contentValues.put(ICOL_7, rModel.getPrice());
                contentValues.put(ICOL_8, rModel.getQty());
                contentValues.put(ICOL_9, rModel.getIsCustomize());
                contentValues.put(ICOL_10, rModel.getIsQuantity());
                contentValues.put(ICOL_11, rModel.getIsVeg());
                contentValues.put(ICOL_12, rModel.getAddonID());
                contentValues.put(ICOL_13, rModel.getAddonTitel());
                contentValues.put(ICOL_14, rModel.getAddonPrice());
                long result = db.insert(TABLE_NAME, null, contentValues);
                if (result == -1) {
                    return false;
                } else {

                    if (HomeActivity.getInstance() != null) {
                        HomeActivity.getInstance().cartCounter();

                    }
                    return true;
                }
            } else {
                if (rModel.getAddonID() != null && getAID(rModel.getAddonID()) == -1) {
                    SQLiteDatabase db = this.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ICOL_2, rModel.getRid());
                    contentValues.put(ICOL_3, rModel.getId());
                    contentValues.put(ICOL_4, rModel.getTitle());
                    contentValues.put(ICOL_5, rModel.getItemImg());
                    contentValues.put(ICOL_6, rModel.getCdesc());
                    contentValues.put(ICOL_7, rModel.getPrice());
                    contentValues.put(ICOL_8, rModel.getQty());
                    contentValues.put(ICOL_9, rModel.getIsCustomize());
                    contentValues.put(ICOL_10, rModel.getIsQuantity());
                    contentValues.put(ICOL_11, rModel.getIsVeg());
                    contentValues.put(ICOL_12, rModel.getAddonID());
                    contentValues.put(ICOL_13, rModel.getAddonTitel());
                    contentValues.put(ICOL_14, rModel.getAddonPrice());
                    long result = db.insert(TABLE_NAME, null, contentValues);
                    if (result == -1) {
                        return false;
                    } else {
                        if (HomeActivity.getInstance() != null) {
                            HomeActivity.getInstance().cartCounter();

                        }
                        return true;
                    }
                } else {
                    return updateData(rModel.getId(), rModel.getQty());

                }
            }


        }

    }

    public int isStore(String rid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_NAME, new String[]{"RID"}, "RID =? ", new String[]{rid}, null, null, null, null);
        if (c.moveToFirst()) { //if the row exist then return the id
            return c.getInt(c.getColumnIndex("RID"));
        } else {
            Cursor cursor = getAllData();
            if (cursor.getCount() != 0) {
                return -1;
            }
            return cursor.getCount();
        }
    }

    private int getID(String pid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_NAME, new String[]{"PID"}, "PID =? ", new String[]{pid}, null, null, null, null);
        if (c.moveToFirst()) //if the row exist then return the id
            return c.getInt(c.getColumnIndex("PID"));
        return -1;
    }

    public int getAID(String aid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_NAME, new String[]{"addonid"}, "addonid =? ", new String[]{aid}, null, null, null, null);
        if (c.moveToFirst()) //if the row exist then return the id
            return c.getInt(c.getColumnIndex("addonid"));
        return -1;
    }

    public int getCard(String aid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_NAME, new String[]{"qty"}, "PID =? ", new String[]{aid}, null, null, null, null);
        if (c.moveToFirst()) { //if the row exist then return the id
            return c.getInt(c.getColumnIndex("qty"));
        } else {
            return -1;
        }
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }


    public boolean updateData(String aid, int qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ICOL_8, qty);
        db.update(TABLE_NAME, contentValues, "pid = ? ", new String[]{aid});

        return true;
    }

    public void deleteCard() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
        if (RestaurantsActivity.getInstance() != null) {
            RestaurantsActivity.getInstance().update();
        }

        if (HomeActivity.getInstance() != null) {
            HomeActivity.getInstance().cartCounter();
        }


    }

    public Integer deleteRData(String aid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Integer a = db.delete(TABLE_NAME, "PID = ? ", new String[]{aid});
        if (HomeActivity.getInstance() != null) {
            HomeActivity.getInstance().cartCounter();
        }
        return a;
    }
}