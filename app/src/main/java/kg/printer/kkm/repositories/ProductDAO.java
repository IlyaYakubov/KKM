package kg.printer.kkm.repositories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import kg.printer.kkm.domains.Product;

public class ProductDAO {

    private final ArrayList<Product> products = new ArrayList<>();
    private final ArrayList<String> listUnits = new ArrayList<>();

    private final DatabaseDAO dbHelper;

    public ProductDAO(DatabaseDAO databaseDAO) {
        dbHelper = databaseDAO;
    }

    /**
     * @return вся номенклатура
     */
    public ArrayList<Product> readProducts() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all products
        Cursor cursor = db.query("products", null, null, null, null, null, null);

        products.clear();

        while (cursor.moveToNext()) {
            int positionColIndex = cursor.getColumnIndex("name");
            int unitColIndex = cursor.getColumnIndex("unit");
            int priceColIndex = cursor.getColumnIndex("price");

            products.add(new Product(cursor.getString(positionColIndex), cursor.getString(unitColIndex), cursor.getString(priceColIndex)));
        }

        cursor.close();

        return products;
    }

    /**
     * @param listIndex - последний индекс записи номенклатуры
     * @return номенклатура
     */
    public Product findProductByListIndex(int listIndex) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select product
        Cursor cursor = db.rawQuery("select * from products where position_on_list = ?"
                , new String[] { String.valueOf(listIndex) });

        Product product = null;

        if (cursor.moveToNext()) {
            int nameColIndex = cursor.getColumnIndex("name");
            int unitColIndex = cursor.getColumnIndex("unit");
            int priceColIndex = cursor.getColumnIndex("price");

            product = new Product(cursor.getString(nameColIndex), cursor.getString(unitColIndex), cursor.getString(priceColIndex));
        }

        cursor.close();

        return product;
    }

    /**
     * Метод создает запись номенклатуры
     *
     * @param name - наименоване
     * @param unit - единица измерения
     * @param price - цена
     */
    public void createProduct(String name, String unit, String price) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("unit", unit);
        cv.put("price", price);

        cv.put("position_on_list", lastIndex() + 1);

        db.insert("products", null, cv);
    }

    /**
     * Метод изменяет запись номенклатуры
     *
     * @param name - новое наименование
     * @param unit - новая единица измерения
     * @param price - новая цена
     * @param listIndex - индекс номенклатуры в списке
     */
    public void updateProduct(String name, String unit, String price, int listIndex) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("unit", unit);
        cv.put("price", price);

        cv.put("position_on_list", listIndex);

        db.update("products", cv, "position_on_list = ?", new String[] { String.valueOf(listIndex) });
    }

    /**
     * @return последний индекс записи
     */
    private int lastIndex() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all products
        Cursor cursor = db.query("products", null, null, null, null, null, null);

        int lastPosition = -1;
        while (cursor.moveToNext()) {
            lastPosition++;
        }

        cursor.close();

        return lastPosition;
    }

    /**
     * @return единицы измерения из базы
     */
    public ArrayList<String> findAllUnits() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all units
        Cursor cursor = db.query("units", null, null, null, null, null, null);

        listUnits.clear();

        while (cursor.moveToNext()) {
            int nameColIndex = cursor.getColumnIndex("name");
            listUnits.add(cursor.getString(nameColIndex));
        }

        cursor.close();

        return listUnits;
    }

}
