package kg.printer.kkm.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DatabaseDAO extends SQLiteOpenHelper {

    public DatabaseDAO(Context context) {
        super(context, "MainDataBase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table organizations ("
                + "id integer primary key autoincrement,"
                + "form_of_sobstvennosti text,"
                + "system_nalog text,"
                + "name text,"
                + "inn text,"
                + "magazine_name text,"
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
                + "procent_of_discount text,"
                + "is_backings integer,"
                + "is_discounts integer,"
                + "is_change_cost integer,"
                + "is_orders integer"
                + ");");
        db.execSQL("create table goods ("
                + "id integer primary key autoincrement,"
                + "position_on_list text,"
                + "name text,"
                + "basic_unit text,"
                + "packages text,"
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
