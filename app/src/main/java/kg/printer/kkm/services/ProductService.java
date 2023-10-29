package kg.printer.kkm.services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import kg.printer.kkm.domains.Product;
import kg.printer.kkm.repositories.DatabaseDAO;
import kg.printer.kkm.view.ProductActivity;
import kg.printer.kkm.view.ProductsActivity;

public class ProductService {

    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<String> listUnits = new ArrayList<>();

    private DatabaseDAO dbHelper;

    public ProductService(ProductsActivity productsActivity) {
        this.dbHelper = new DatabaseDAO(productsActivity);
    }

    public ProductService(ProductActivity productActivity) {
        this.dbHelper = new DatabaseDAO(productActivity);
    }

    public ArrayList<Product> readProducts() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all products
        Cursor cursor = db.query("products", null, null, null, null, null, null);

        products.clear();

        while (cursor.moveToNext()) {
            int positionColIndex = cursor.getColumnIndex("name");
            int coastColIndex = cursor.getColumnIndex("price");
            int unitColIndex = cursor.getColumnIndex("unit");
            products.add(new Product(cursor.getString(positionColIndex), cursor.getString(coastColIndex), cursor.getString(unitColIndex)));
        }

        cursor.close();

        return products;
    }

    public Product findProductByListIndex(int listIndex) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select product
        Cursor cursor = db.rawQuery("select * from products where position_on_list = ?"
                , new String[] { String.valueOf(listIndex) });

        Product product = null;

        if (cursor.moveToNext()) {
            int nameColIndex = cursor.getColumnIndex("name");
            int basicUnitColIndex = cursor.getColumnIndex("unit");
            int coastColIndex = cursor.getColumnIndex("price");

            product = new Product(cursor.getString(nameColIndex), cursor.getString(basicUnitColIndex), cursor.getString(coastColIndex));
        }

        cursor.close();

        return product;
    }

    public void createProduct(String name, String unit, String price) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("unit", unit);
        cv.put("price", price);

        cv.put("position_on_list", lastIndex() + 1);

        db.insert("products", null, cv);
    }

    public void updateProduct(String name, String basicUnit, String coast, int position_on_list) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("unit", basicUnit);
        cv.put("price", coast);

        cv.put("position_on_list", position_on_list);
        db.update("products", cv, "position_on_list = ?", new String[] { String.valueOf(position_on_list) });
    }

    public int lastIndex() {
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
     *
     * @return единицы измерения из базы
     */
    public ArrayList<String> findAllUnits() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all units
        Cursor cursor = db.query("units", null, null, null, null, null, null);

        listUnits.clear();

        while (cursor.moveToNext()) {
            int positionColIndex = cursor.getColumnIndex("name");
            listUnits.add(cursor.getString(positionColIndex));
        }

        cursor.close();

        return listUnits;
    }

}
