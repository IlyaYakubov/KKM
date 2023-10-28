package kg.printer.kkm.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DatabaseDAO extends SQLiteOpenHelper {

    public DatabaseDAO(Context context) {
        super(context, "MainDataBase", null, 1);

        if (!administratorIsExists()) {
            createAdministrator();
        }
    }

    private boolean administratorIsExists() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where is_admin = 1", new String[] { });

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    private void createAdministrator() {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        cv.put("is_admin", 1);
        cv.put("position_on_list", 0);
        cv.put("password", "");
        cv.put("position", "Администратор");
        cv.put("surname", "");
        cv.put("name", "Администратор");
        cv.put("second_name", "");

        db.insert("users", null, cv);

        close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table organizations ("
                + "id integer primary key autoincrement,"
                + "type_of_ownership text," // форма собственности
                + "taxation text," // система налогооблажения
                + "name text,"
                + "inn text," // ИНН
                + "magazine_name text," // торговая точка
                + "adress_magazine text,"
                + "telephone_magazine text"
                + ");");
        db.execSQL("create table users ("
                + "id integer primary key autoincrement,"
                + "position_on_list integer,"
                + "is_admin integer,"
                + "password text,"
                + "position text,"
                + "surname text,"
                + "name text,"
                + "second_name text,"
                + "inn text,"
                + "percent_of_discount text," // максимальный процент скидки
                + "is_backings integer," // возвраты
                + "is_discounts integer," // скидки
                + "is_change_costs integer," // изменять цену
                + "is_orders integer" // заказы клиента
                + ");");
        db.execSQL("create table products ("
                + "id integer primary key autoincrement,"
                + "position_on_list text,"
                + "name text,"
                + "unit text,"
                + "tnved text,"
                + "coast text"
                + ");");
        db.execSQL("create table units ("
                + "id integer primary key autoincrement,"
                + "position_on_list text,"
                + "name text,"
                + "full_name text,"
                + "code text"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static class BaseEnums {

        public static final int NONE = -1;
        public static final int CMD_ESC = 1, CMD_TSC = 2, CMD_CPCL = 3, CMD_ZPL = 4, CMD_PIN = 5;
        public static final int CON_BLUETOOTH = 1, CON_WIFI = 3, CON_USB = 4, CON_COM = 5;

        @IntDef({CMD_ESC, CMD_TSC, CMD_CPCL, CMD_ZPL, CMD_PIN})
        @Retention(RetentionPolicy.SOURCE)
        public @interface CmdType {
        }

        @IntDef({CON_BLUETOOTH, CON_WIFI, CON_USB, CON_COM, NONE})
        @Retention(RetentionPolicy.SOURCE)
        public @interface ConnectType {
        }

    }
}
