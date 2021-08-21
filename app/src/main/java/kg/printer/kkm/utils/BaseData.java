package kg.printer.kkm.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseData extends SQLiteOpenHelper {

    public BaseData(Context context) {
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
        db.execSQL("create table packages ("
                + "id integer primary key autoincrement,"
                + "position_on_list text,"
                + "name text,"
                + "good text,"
                + "unit text," // единица измерения упаковки
                + "set_of_packages text," // набор упаковок
                + "coefficient text" // коэффициент пересчёта
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}