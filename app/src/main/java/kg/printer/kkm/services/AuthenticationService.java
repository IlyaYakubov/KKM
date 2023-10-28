package kg.printer.kkm.services;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.widget.EditText;
import android.widget.Switch;

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

    private final List<String> listPermissions = new ArrayList<>();

    private final String[] permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

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

    public void checkAllPermission(AuthenticationActivity authenticationActivity) {
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

    private User newUser(Cursor cursor) {
        int idColIndex = cursor.getColumnIndex("position_on_list");
        int positionColIndex = cursor.getColumnIndex("position");
        int surnameColIndex = cursor.getColumnIndex("surname");
        int nameColIndex = cursor.getColumnIndex("name");
        int secondNameColIndex = cursor.getColumnIndex("second_name");

        User user = new User();
        user.setId(cursor.getInt(idColIndex));
        user.setPosition(cursor.getString(positionColIndex));
        user.setSurname(cursor.getString(surnameColIndex));
        user.setName(cursor.getString(nameColIndex));
        user.setSecondName(cursor.getString(secondNameColIndex));
        return user;
    }

    // controller
    public void fillUsers(ArrayList<User> users) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all users
        Cursor cursor = db.query("users", null, null, null, null, null, null);

        users.clear();

        while (cursor.moveToNext()) {
            users.add(newUser(cursor));
        }

        cursor.close();
    }

    // controller
    public void readUserFromDatabase(BasicPassFragment settingPasswordDialog,
                                     int position_on_list,
                                     EditText et_position,
                                     EditText et_surname,
                                     EditText et_name,
                                     EditText et_second_name,
                                     EditText et_inn,
                                     EditText et_percent_discount,
                                     Switch sw_backings,
                                     Switch sw_discounts,
                                     Switch sw_change_cost,
                                     Switch sw_orders) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select user
        Cursor cursor = db.rawQuery("select * from users where position_on_list = ?"
                , new String[] { String.valueOf(position_on_list) });

        if (cursor.moveToFirst()) {
            int passColIndex = cursor.getColumnIndex("password");
            int positionColIndex = cursor.getColumnIndex("position");
            int surnameColIndex = cursor.getColumnIndex("surname");
            int nameColIndex = cursor.getColumnIndex("name");
            int secondNameColIndex = cursor.getColumnIndex("second_name");
            int innColIndex = cursor.getColumnIndex("inn");
            int percentOfDiscountColIndex = cursor.getColumnIndex("percent_of_discount");
            int isBackingsColIndex = cursor.getColumnIndex("is_backings");
            int isDiscountsColIndex = cursor.getColumnIndex("is_discounts");
            int isChangeCostColIndex = cursor.getColumnIndex("is_change_costs");
            int isOrdersColIndex = cursor.getColumnIndex("is_orders");

            settingPasswordDialog.setPassword(cursor.getString(passColIndex));
            et_position.setText(cursor.getString(positionColIndex));
            et_surname.setText(cursor.getString(surnameColIndex));
            et_name.setText(cursor.getString(nameColIndex));
            et_second_name.setText(cursor.getString(secondNameColIndex));
            et_inn.setText(cursor.getString(innColIndex));
            et_percent_discount.setText(cursor.getString(percentOfDiscountColIndex));

            if (cursor.getInt(isBackingsColIndex) == 1) sw_backings.setChecked(true);
            if (cursor.getInt(isDiscountsColIndex) == 1) sw_discounts.setChecked(true);
            if (cursor.getInt(isChangeCostColIndex) == 1) sw_change_cost.setChecked(true);
            if (cursor.getInt(isOrdersColIndex) == 1) sw_orders.setChecked(true);
        }

        cursor.close();
    }

    // controller
    public void createOrUpdateUserData(BasicPassFragment settingPasswordDialog,
                                       int position_on_list,
                                       EditText et_position,
                                       EditText et_surname,
                                       EditText et_name,
                                       EditText et_second_name,
                                       EditText et_inn,
                                       EditText et_percent_discount,
                                       Switch sw_backings,
                                       Switch sw_discounts,
                                       Switch sw_change_cost,
                                       Switch sw_orders,
                                       int newElement) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String position = et_position.getText().toString();
        String surname = et_surname.getText().toString();
        String name = et_name.getText().toString();
        String secondName = et_second_name.getText().toString();
        String inn = et_inn.getText().toString();
        String percentOfDiscount = et_percent_discount.getText().toString();

        int isBackings = (sw_backings.isChecked())? 1 : 0;
        int isDiscounts = (sw_discounts.isChecked())? 1 : 0;
        int isChangeCost = (sw_change_cost.isChecked())? 1 : 0;
        int isOrders = (sw_orders.isChecked())? 1 : 0;

        String pass = "";
        if (settingPasswordDialog.getEtPassword() != null) {
            pass = settingPasswordDialog.getPassword();
        }

        cv.put("is_admin", 0);
        cv.put("password", pass);
        cv.put("position", position);
        cv.put("surname", surname);
        cv.put("name", name);
        cv.put("second_name", secondName);
        cv.put("inn", inn);
        cv.put("percent_of_discount", percentOfDiscount);
        cv.put("is_backings", isBackings);
        cv.put("is_discounts", isDiscounts);
        cv.put("is_change_costs", isChangeCost);
        cv.put("is_orders", isOrders);

        if (newElement == 1) {
            cv.put("position_on_list", position_on_list + 1);
            db.insert("users", null, cv);
        } else {
            cv.put("position_on_list", position_on_list);
            db.update("users", cv, "position_on_list = ?", new String[] { String.valueOf(position_on_list) });
        }
    }

    public int lastUserIdInDatabase() {
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

    public boolean userExistsInDatabase(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select user
        Cursor cursor = db.rawQuery("select * from users where position_on_list = ? and password = ?", new String[] {String.valueOf(user.getId()), user.getPassword()});

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public User findUserByIdInDatabase(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select user
        Cursor cursor = db.rawQuery("select * from users where position_on_list = ?", new String[] {String.valueOf(id)});

        User user = new User();
        if (cursor.moveToFirst()) {
            user = newUser(cursor);
        }
        cursor.close();
        return user;
    }

    public ArrayList<User> readUsersFromDatabase() {
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

    // controller
    public void deleteUserFromDatabase(int position_on_list) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("users", "position_on_list = " + position_on_list, null);

        // select users
        Cursor cursor = db.query("users", null, "is_admin = 0", null, null, null, null);

        while (cursor.moveToNext()) {
            //update t1 set id = (select count(*) + 1 from t1 t where t.id < t1.id) where id > (select min(t1.id) from t1 left join t1 next on t1.id+1 = next.id where next.id is null)
            String sql = "update users set position_on_list = (select count(*) + 1 from users t where t.position_on_list < users.position_on_list and users.is_admin = 0) - 1";
            db.execSQL(sql);
        }
    }

    public Administrator findAdministratorInDatabase(BasicPassFragment settingPasswordDialog) {
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

            administrator.setId(cursor.getInt(idColIndex));
            administrator.setPosition(cursor.getString(positionColIndex));
            administrator.setSurname(cursor.getString(surnameColIndex));
            administrator.setName(cursor.getString(nameColIndex));
            administrator.setSecondName(cursor.getString(secondNameColIndex));

            settingPasswordDialog.setPassword(cursor.getString(passColIndex));
        }

        cursor.close();

        return administrator;
    }

    // controller
    public void createAdministratorInDatabase() {
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

    // controller
    public void updateAdministratorInDatabase(Administrator administrator) {
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

}
