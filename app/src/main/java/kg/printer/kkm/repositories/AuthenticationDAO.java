package kg.printer.kkm.repositories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import kg.printer.kkm.domains.Administrator;
import kg.printer.kkm.domains.User;

public class AuthenticationDAO {

    private final ArrayList<User> users = new ArrayList<>();

    private final DatabaseDAO dbHelper;

    public AuthenticationDAO(DatabaseDAO databaseDAO) {
        dbHelper = databaseDAO;
    }

    /**
     * Метод создает запись пользователя
     *
     * @param user - пользователь, который будет создан в качестве записи
     */
    public void createUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("is_admin", 0);
        cv.put("password", user.getPassword());
        cv.put("position", user.getPosition());
        cv.put("surname", user.getSurname());
        cv.put("name", user.getName());
        cv.put("second_name", user.getSecondName());
        cv.put("inn", user.getInn());
        cv.put("percent_of_discount", user.getPercentOfDiscount());
        cv.put("is_backings", user.isBackings());
        cv.put("is_discounts", user.isDiscounts());
        cv.put("is_change_price", user.isChangePrice());
        cv.put("is_orders", user.isOrders());

        cv.put("position_on_list", lastIndex() + 1);

        db.insert("users", null, cv);
    }

    /**
     * @return список пользователей
     */
    public ArrayList<User> readUsers() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all users
        Cursor cursor = db.rawQuery("select * from users", new String[] { });

        users.clear();

        while (cursor.moveToNext()) {
            users.add(newUser(cursor));
        }

        cursor.close();
        return users;
    }

    /**
     * Метод изменяет запись пользователя
     *
     * @param user - изменяемый пользователь
     * @param listIndex - индекс в списке
     */
    public void updateUser(User user, int listIndex) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("is_admin", 0);
        cv.put("password", user.getPassword());
        cv.put("position", user.getPosition());
        cv.put("surname", user.getSurname());
        cv.put("name", user.getName());
        cv.put("second_name", user.getSecondName());
        cv.put("inn", user.getInn());
        cv.put("percent_of_discount", user.getPercentOfDiscount());
        cv.put("is_backings", user.isBackings());
        cv.put("is_discounts", user.isDiscounts());
        cv.put("is_change_price", user.isChangePrice());
        cv.put("is_orders", user.isOrders());

        cv.put("position_on_list", listIndex);

        db.update("users", cv, "position_on_list = ?", new String[] { String.valueOf(listIndex) });
    }

    /**
     * @param listIndex - индекс в списке
     * @return пользователь
     */
    public User findUserByListIndex(int listIndex) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select user
        Cursor cursor = db.rawQuery("select * from users where position_on_list = ?"
                , new String[] { String.valueOf(listIndex) });

        User user = null;

        if (cursor.moveToFirst()) {
            user = newUser(cursor);
        }

        cursor.close();

        return user;
    }

    /**
     * Метод проверяет, существует ли запись пользователя в хранилище
     *
     * @param user - искомый пользователь
     * @return найденный пользователь
     */
    public boolean findUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select user
        Cursor cursor = db.rawQuery("select * from users where position_on_list = ? and password = ?"
                , new String[] {String.valueOf(user.getListIndex()), user.getPassword()});

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    /**
     * @return последний индекс записи
     */
    private int lastIndex() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all users
        Cursor cursor = db.query("users", null, null, null, null, null, null);

        int lastPosition = -1;
        while (cursor.moveToNext()) {
            lastPosition++;
        }

        cursor.close();

        return lastPosition;
    }

    /**
     * Метод создает администратора
     */
    public void createAdministrator() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select administrator
        Cursor cursor = db.query("users", null, "is_admin = 1", null, null, null, null);

        if (!cursor.moveToFirst()) {
            db = dbHelper.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put("is_admin", 1);
            cv.put("position_on_list", 0);
            cv.put("position", "Администратор");
            cv.put("surname", "");
            cv.put("name", "");
            cv.put("second_name", "");

            db.insert("users", null, cv);
        }

        cursor.close();
    }

    /**
     * @return данные администратора в качестве объекта
     */
    public Administrator readAdministrator() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select just administrator
        Cursor cursor = db.query("users", null, "is_admin = 1", null, null, null, null);

        Administrator administrator = new Administrator();
        if (cursor.moveToFirst()) {
            int idColIndex = cursor.getColumnIndex("position_on_list");
            int passColIndex = cursor.getColumnIndex("password");
            int positionColIndex = cursor.getColumnIndex("position");
            int surnameColIndex = cursor.getColumnIndex("surname");
            int nameColIndex = cursor.getColumnIndex("name");
            int secondNameColIndex = cursor.getColumnIndex("second_name");

            administrator.setListIndex(cursor.getInt(idColIndex));
            administrator.setPosition(cursor.getString(positionColIndex));
            administrator.setSurname(cursor.getString(surnameColIndex));
            administrator.setName(cursor.getString(nameColIndex));
            administrator.setSecondName(cursor.getString(secondNameColIndex));
            administrator.setPassword(cursor.getString(passColIndex));
        }

        cursor.close();

        return administrator;
    }

    /**
     * Метод изменяет данные администратора
     *
     * @param administrator - текущий администратор
     */
    public void updateAdministrator(Administrator administrator) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        cv.put("is_admin", 1);
        cv.put("position_on_list", 0);
        cv.put("password", administrator.getPassword());
        cv.put("position", administrator.getPosition());
        cv.put("surname", administrator.getSurname());
        cv.put("name", administrator.getName());
        cv.put("second_name", administrator.getSecondName());

        db.update("users", cv, "position_on_list = ?", new String[] { "0" });

        dbHelper.close();
    }

    /**
     * @param cursor - выборка из базы данных
     * @return новый объект пользователя
     */
    private User newUser(Cursor cursor) {
        int idColIndex = cursor.getColumnIndex("position_on_list");
        int passColIndex = cursor.getColumnIndex("password");
        int positionColIndex = cursor.getColumnIndex("position");
        int surnameColIndex = cursor.getColumnIndex("surname");
        int nameColIndex = cursor.getColumnIndex("name");
        int secondNameColIndex = cursor.getColumnIndex("second_name");
        int innColIndex = cursor.getColumnIndex("inn");
        int percentOfDiscountColIndex = cursor.getColumnIndex("percent_of_discount");
        int isBackingsColIndex = cursor.getColumnIndex("is_backings");
        int isDiscountsColIndex = cursor.getColumnIndex("is_discounts");
        int isChangePriceColIndex = cursor.getColumnIndex("is_change_price");
        int isOrdersColIndex = cursor.getColumnIndex("is_orders");

        User user = new User();

        user.setListIndex(Integer.parseInt(cursor.getString(idColIndex)));
        user.setPassword(cursor.getString(passColIndex));
        user.setPosition(cursor.getString(positionColIndex));
        user.setSurname(cursor.getString(surnameColIndex));
        user.setName(cursor.getString(nameColIndex));
        user.setSecondName(cursor.getString(secondNameColIndex));
        user.setInn(cursor.getString(innColIndex));
        user.setPercentOfDiscount(cursor.getString(percentOfDiscountColIndex));

        if (cursor.getInt(isBackingsColIndex) == 1) user.setBackings(true);
        if (cursor.getInt(isDiscountsColIndex) == 1) user.setDiscounts(true);
        if (cursor.getInt(isChangePriceColIndex) == 1) user.setChangePrice(true);
        if (cursor.getInt(isOrdersColIndex) == 1) user.setOrders(true);

        return user;
    }

}
