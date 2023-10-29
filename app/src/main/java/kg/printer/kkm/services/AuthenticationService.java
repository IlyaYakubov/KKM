package kg.printer.kkm.services;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import kg.printer.kkm.domains.Administrator;
import kg.printer.kkm.domains.User;
import kg.printer.kkm.repositories.DatabaseDAO;
import kg.printer.kkm.view.AdministratorActivity;
import kg.printer.kkm.view.AuthenticationActivity;
import kg.printer.kkm.view.UserActivity;
import kg.printer.kkm.view.UsersActivity;
import kg.printer.kkm.view.old.BasicPassFragment;

public class AuthenticationService {

    private final String[] permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private final List<String> listPermissions = new ArrayList<>();

    private final ArrayList<User> users = new ArrayList<>();

    private final DatabaseDAO dbHelper;

    public AuthenticationService(AuthenticationActivity authenticationActivity) {
        this.dbHelper = new DatabaseDAO(authenticationActivity.getApplicationContext());
    }

    public AuthenticationService(AdministratorActivity administratorActivity) {
        this.dbHelper = new DatabaseDAO(administratorActivity.getApplicationContext());
    }

    public AuthenticationService(UserActivity userActivity) {
        this.dbHelper = new DatabaseDAO(userActivity.getApplicationContext());
    }

    public AuthenticationService(UsersActivity usersActivity) {
        this.dbHelper = new DatabaseDAO(usersActivity.getApplicationContext());
    }

    public void checkAllPermissions(AuthenticationActivity authenticationActivity) {
        listPermissions.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (authenticationActivity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    listPermissions.add(permission);
                }
            }
            if (listPermissions.size() != 0) {
                authenticationActivity.requestPermissions(listPermissions.toArray(new String[0]), 0);
            }
        }
    }

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

    public void deleteUser(int listIndex) {
        /*SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("users", "position_on_list = " + listIndex, null);

        // select just users
        @SuppressLint("Recycle") Cursor cursor = db.query("users", null, "is_admin = 0", null, null, null, null);

        while (cursor.moveToNext()) {
            String sql = "update users set position_on_list = (select count(*) + 1 from users t where t.position_on_list < users.position_on_list and users.is_admin = 0) - 1";
            db.execSQL(sql);
        }*/
    }

    public boolean findUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select user
        Cursor cursor = db.rawQuery("select * from users where position_on_list = ? and password = ?", new String[] {String.valueOf(user.getListIndex()), user.getPassword()});

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public int lastIndex() {
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

    public Administrator findAdministrator(BasicPassFragment settingPasswordDialog) {
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

            settingPasswordDialog.setPassword(cursor.getString(passColIndex));
        }

        cursor.close();

        return administrator;
    }

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
