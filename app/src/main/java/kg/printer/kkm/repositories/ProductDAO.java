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
        Cursor cursor = db.rawQuery("select * from products where list_index = ?"
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
     * @param product - номенклатура
     */
    public void createProduct(Product product) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", product.getName());
        cv.put("unit", product.getUnit());
        cv.put("price", product.getPrice());

        cv.put("list_index", lastIndex() + 1);

        db.insert("products", null, cv);
    }

    /**
     * Метод изменяет запись номенклатуры
     *
     * @param product - номенклатура
     * @param listIndex - индекс номенклатуры в списке
     */
    public void updateProduct(Product product, int listIndex) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", product.getName());
        cv.put("unit", product.getUnit());
        cv.put("price", product.getPrice());

        cv.put("list_index", listIndex);

        db.update("products", cv, "list_index = ?", new String[] { String.valueOf(listIndex) });
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
