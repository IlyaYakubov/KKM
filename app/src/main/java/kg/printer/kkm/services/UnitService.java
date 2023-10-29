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

    public ArrayList<String> readUnits() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all units
        Cursor cursor = db.query("units", null, null, null, null, null, null);

        units.clear();

        ArrayList<String> unitNames = new ArrayList<>();

        while (cursor.moveToNext()) {
            int positionColIndex = cursor.getColumnIndex("name");
            Unit unit = new Unit(cursor.getString(positionColIndex), "", "");
            units.add(unit);
            unitNames.add(unit.getName());
        }

        cursor.close();

        return unitNames;
    }

    public Unit findUnitByListIndex(int listIndex) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select unit
        Cursor cursor = db.rawQuery("select * from units where position_on_list = ?"
                , new String[] { String.valueOf(listIndex) });

        Unit unit = null;

        if (cursor.moveToFirst()) {
            int nameColIndex = cursor.getColumnIndex("name");
            int fuulNameColIndex = cursor.getColumnIndex("full_name");
            int codeColIndex = cursor.getColumnIndex("code");

            unit = new Unit(cursor.getString(nameColIndex), cursor.getString(fuulNameColIndex), cursor.getString(codeColIndex));
        }

        cursor.close();

        return unit;
    }

    public void createUnit(String name, String fullName, String code) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("full_name", fullName);
        cv.put("code", code);

        cv.put("position_on_list", lastIndex() + 1);

        db.insert("units", null, cv);
    }

    public void updateUnit(String name, String fullName, String code, int listIndex) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("full_name", fullName);
        cv.put("code", code);

        cv.put("position_on_list", listIndex);

        db.update("units", cv, "position_on_list = ?", new String[] { String.valueOf(listIndex) });
    }

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
