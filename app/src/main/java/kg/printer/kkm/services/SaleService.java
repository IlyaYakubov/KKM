package kg.printer.kkm.services;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kg.printer.kkm.domains.Product;
import kg.printer.kkm.repositories.DatabaseDAO;
import kg.printer.kkm.view.CashActivity;
import kg.printer.kkm.view.ProductSelectionActivity;
import kg.printer.kkm.view.SaleActivity;

public class SaleService {

    private ArrayList<Product> products = new ArrayList<>();

    private DatabaseDAO dbHelper;

    public SaleService(ProductSelectionActivity productSelectionActivity) {
        this.dbHelper = new DatabaseDAO(productSelectionActivity);
    }

    public SaleService(CashActivity cashActivity) {
        this.dbHelper = new DatabaseDAO(cashActivity);
    }

    public SaleService(SaleActivity saleActivity) {
        this.dbHelper = new DatabaseDAO(saleActivity);
    }

    @SuppressWarnings("rawtypes")
    public Map toCount(String contributedParam, double sum) {
        double contributed = Double.parseDouble(contributedParam);
        double change = contributed - sum;

        Map result = new HashMap();
        result.put("contributed", contributed);
        result.put("change", change);

        return result;
    }

    public String toMultiply(String quantityText, String priceText) {
        double quantity = Double.parseDouble(quantityText);
        double coast = Double.parseDouble(priceText);
        double sum = quantity * coast;

        return String.format("%.2f", sum);
    }

    public Map findProductByListIndex(int listIndex) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select product
        Cursor cursor = db.rawQuery("select * from products where position_on_list = ?"
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

}
