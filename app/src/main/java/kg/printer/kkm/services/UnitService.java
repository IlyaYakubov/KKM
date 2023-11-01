package kg.printer.kkm.services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import kg.printer.kkm.domains.Unit;
import kg.printer.kkm.repositories.DatabaseDAO;
import kg.printer.kkm.view.UnitActivity;
import kg.printer.kkm.view.UnitsActivity;

public class UnitService {

    private final ArrayList<Unit> units = new ArrayList<>();

    private DatabaseDAO dbHelper;

    public UnitService(UnitsActivity unitsActivity) {
        this.dbHelper = new DatabaseDAO(unitsActivity);
    }

    public UnitService(UnitActivity unitActivity) {
        this.dbHelper = new DatabaseDAO(unitActivity);
    }

    /**
     * Метод создает единицу измерения
     *
     * @param name - наименование
     * @param fullName - полное наименование
     * @param code - международный код единицы измерения
     */
    public void createUnit(String name, String fullName, String code) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("full_name", fullName);
        cv.put("code", code);

        cv.put("position_on_list", lastIndex() + 1);

        db.insert("units", null, cv);
    }

    /**
     * @return список единиц измерения
     */
    public ArrayList<String> readUnits() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all units
        Cursor cursor = db.query("units", null, null, null, null, null, null);

        units.clear();

        ArrayList<String> unitNames = new ArrayList<>();

        while (cursor.moveToNext()) {
            int nameColIndex = cursor.getColumnIndex("name");
            int fullNameColIndex = cursor.getColumnIndex("full_name");
            int codeColIndex = cursor.getColumnIndex("code");

            Unit unit = new Unit(cursor.getString(nameColIndex), cursor.getString(fullNameColIndex), cursor.getString(codeColIndex));
            units.add(unit);
            unitNames.add(unit.getName());
        }

        cursor.close();

        return unitNames;
    }

    /**
     * Метод изменяет запись единицы измерения
     *
     * @param name - наименование
     * @param fullName - полное наименование
     * @param code - международный код
     * @param listIndex - индекс в списке
     */
    public void updateUnit(String name, String fullName, String code, int listIndex) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("full_name", fullName);
        cv.put("code", code);

        cv.put("position_on_list", listIndex);

        db.update("units", cv, "position_on_list = ?", new String[] { String.valueOf(listIndex) });
    }

    /**
     * @param listIndex - индекс в списке
     * @return единица измерения
     */
    public Unit findUnitByListIndex(int listIndex) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select unit
        Cursor cursor = db.rawQuery("select * from units where position_on_list = ?"
                , new String[] { String.valueOf(listIndex) });

        Unit unit = new Unit("", "", "");

        if (cursor.moveToFirst()) {
            int nameColIndex = cursor.getColumnIndex("name");
            int fullNameColIndex = cursor.getColumnIndex("full_name");
            int codeColIndex = cursor.getColumnIndex("code");

            unit = new Unit(cursor.getString(nameColIndex), cursor.getString(fullNameColIndex), cursor.getString(codeColIndex));
        }

        cursor.close();

        return unit;
    }

    /**
     * @return индекс последней записи единицы измерения
     */
    public int lastIndex() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all units
        Cursor cursor = db.query("units", null, null, null, null, null, null);

        int lastPosition = -1;
        while (cursor.moveToNext()) {
            lastPosition++;
        }

        cursor.close();

        return lastPosition;
    }

}
