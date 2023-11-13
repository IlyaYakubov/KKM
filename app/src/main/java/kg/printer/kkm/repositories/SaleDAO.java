package kg.printer.kkm.repositories;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kg.printer.kkm.domains.Product;

public class SaleDAO {

    private final ArrayList<Product> products = new ArrayList<>();

    private final DatabaseDAO dbHelper;

    public SaleDAO(DatabaseDAO databaseDAO) {
        dbHelper = databaseDAO;
    }

    /**
     * @param listIndex - индекс в списке
     * @return структура с наименованием номенклатуры и ценой
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map findProductByListIndex(int listIndex) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select product
        Cursor cursor = db.rawQuery("select * from products where list_index = ?"
                , new String[]{String.valueOf(listIndex)});

        Map map = new HashMap();

        if (cursor.moveToNext()) {
            int nameColIndex = cursor.getColumnIndex("name");
            int priceColIndex = cursor.getColumnIndex("price");

            map.put("name", cursor.getString(nameColIndex));
            map.put("price", cursor.getString(priceColIndex));
        }

        cursor.close();

        return map;
    }

    /**
     * @return список номенлатуры
     */
    public ArrayList<Product> readProducts() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all products
        Cursor cursor = db.query("products", null, null, null, null, null, null);

        products.clear();

        while (cursor.moveToNext()) {
            int positionColIndex = cursor.getColumnIndex("name");
            int unitColIndex = cursor.getColumnIndex("unit");
            int unitIdColIndex = cursor.getColumnIndex("unit_id");
            int priceColIndex = cursor.getColumnIndex("price");

            products.add(new Product(cursor.getString(positionColIndex)
                    , cursor.getString(unitColIndex)
                    , cursor.getInt(unitIdColIndex)
                    , cursor.getString(priceColIndex)));
        }

        cursor.close();

        return products;
    }

}
