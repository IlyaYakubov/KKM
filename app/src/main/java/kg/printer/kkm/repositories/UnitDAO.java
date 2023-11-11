package kg.printer.kkm.repositories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import kg.printer.kkm.domains.Unit;

public class UnitDAO {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final ArrayList<Unit> units = new ArrayList<>();

    private final DatabaseDAO dbHelper;

    public UnitDAO(DatabaseDAO databaseDAO) {
        dbHelper = databaseDAO;
    }

    /**
     * Метод создает единицу измерения
     *
     * @param unit - единица измерения
     */
    public void createUnit(Unit unit) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", unit.getName());
        cv.put("full_name", unit.getFullName());
        cv.put("code", unit.getCode());

        cv.put("list_index", lastIndex() + 1);

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
     * @param unit - единица измерения
     * @param listIndex - индекс в списке
     */
    public void updateUnit(Unit unit, int listIndex) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", unit.getName());
        cv.put("full_name", unit.getFullName());
        cv.put("code", unit.getCode());

        cv.put("list_index", listIndex);

        db.update("units", cv, "list_index = ?", new String[] { String.valueOf(listIndex) });
    }

    /**
     * @param listIndex - индекс в списке
     * @return единица измерения
     */
    public Unit findUnitByListIndex(int listIndex) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select unit
        Cursor cursor = db.rawQuery("select * from units where list_index = ?"
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
    private int lastIndex() {
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
