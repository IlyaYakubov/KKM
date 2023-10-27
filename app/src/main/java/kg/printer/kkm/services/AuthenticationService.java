package kg.printer.kkm.services;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import kg.printer.kkm.domains.User;
import kg.printer.kkm.repositories.DatabaseDAO;
import kg.printer.kkm.view.AuthenticationActivity;

public class AuthenticationService {

    private final AuthenticationActivity authenticationActivity;
    private final ArrayList<User> listUsers = new ArrayList<>();
    private final List<String> emptyPermissions = new ArrayList<>();

    private final String[] permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private final DatabaseDAO dbHelper;

    public AuthenticationService(AuthenticationActivity authenticationActivity) {
        this.authenticationActivity = authenticationActivity;
        this.dbHelper = new DatabaseDAO(authenticationActivity.getApplicationContext());

        CheckAllPermission();
    }

    private void CheckAllPermission() {
        emptyPermissions.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (authenticationActivity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    emptyPermissions.add(permission);
                }
            }
            if (emptyPermissions.size() != 0) {
                authenticationActivity.requestPermissions(emptyPermissions.toArray(new String[0]), 0);
            }
        }
    }

    public void getAllUserFromDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all users
        Cursor cursor = db.rawQuery("select * from users", new String[] { });

        listUsers.clear();

        while (cursor.moveToNext()) {
            listUsers.add(createUser(cursor));
        }

        authenticationActivity.setListUsers(listUsers);

        cursor.close();
    }

    public boolean userExistsInDatabase(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select user
        Cursor cursor = db.rawQuery("select * from users where name = ? and password = ?", new String[] {user.getName(), user.getPassword()});

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    public User findUserInDatabase(String name, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select user
        Cursor cursor = db.rawQuery("select * from users where name = ? and password = ?", new String[] {name, password});

        User user = new User();
        if (cursor.moveToFirst()) {
            user = createUser(cursor);
        }
        cursor.close();
        return user;
    }

    private User createUser(Cursor cursor) {
        int positionColIndex = cursor.getColumnIndex("position");
        int surnameColIndex = cursor.getColumnIndex("surname");
        int nameColIndex = cursor.getColumnIndex("name");
        int secondNameColIndex = cursor.getColumnIndex("second_name");

        User user = new User();
        user.setPosition(cursor.getString(positionColIndex));
        user.setSurname(cursor.getString(surnameColIndex));
        user.setName(cursor.getString(nameColIndex));
        user.setSecondName(cursor.getString(secondNameColIndex));
        return user;
    }

}
