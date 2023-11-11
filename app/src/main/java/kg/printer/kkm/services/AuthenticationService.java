package kg.printer.kkm.services;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import kg.printer.kkm.domains.Administrator;
import kg.printer.kkm.domains.User;
import kg.printer.kkm.repositories.AuthenticationDAO;
import kg.printer.kkm.repositories.DatabaseDAO;
import kg.printer.kkm.view.authorization.AdministratorActivity;
import kg.printer.kkm.view.authorization.AuthenticationActivity;
import kg.printer.kkm.view.sales.SaleActivity;
import kg.printer.kkm.view.UserActivity;
import kg.printer.kkm.view.UsersActivity;

public class AuthenticationService {

    private final String[] permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private final List<String> listPermissions = new ArrayList<>();

    private final AuthenticationDAO authenticationDAO;

    public AuthenticationService(AuthenticationActivity authenticationActivity) {
        authenticationDAO = new AuthenticationDAO(new DatabaseDAO(authenticationActivity.getApplicationContext()));
    }

    public AuthenticationService(AdministratorActivity administratorActivity) {
        authenticationDAO = new AuthenticationDAO(new DatabaseDAO(administratorActivity.getApplicationContext()));
    }

    public AuthenticationService(UserActivity userActivity) {
        authenticationDAO = new AuthenticationDAO(new DatabaseDAO(userActivity.getApplicationContext()));
    }

    public AuthenticationService(UsersActivity usersActivity) {
        authenticationDAO = new AuthenticationDAO(new DatabaseDAO(usersActivity.getApplicationContext()));
    }

    public AuthenticationService(SaleActivity saleActivity) {
        authenticationDAO = new AuthenticationDAO(new DatabaseDAO(saleActivity.getApplicationContext()));
    }

    /**
     * Метод вызывает проверку разрешений
     *
     * @param authenticationActivity - контроллер аутентификации
     */
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

    /**
     * Метод создает запись пользователя
     *
     * @param user - пользователь, который будет создан в качестве записи
     */
    public void createUser(User user) {
        authenticationDAO.createUser(user);
    }

    /**
     * @return список пользователей
     */
    public ArrayList<User> readUsers() {
        return authenticationDAO.readUsers();
    }

    /**
     * Метод изменяет запись пользователя
     *
     * @param user - изменяемый пользователь
     * @param listIndex - индекс в списке
     */
    public void updateUser(User user, int listIndex) {
        authenticationDAO.updateUser(user, listIndex);
    }

    /**
     * @param listIndex - индекс в списке
     * @return пользователь
     */
    public User findUserByListIndex(int listIndex) {
        return authenticationDAO.findUserByListIndex(listIndex);
    }

    /**
     * Метод проверяет, существует ли запись пользователя в хранилище
     *
     * @param user - искомый пользователь
     * @return найденный пользователь
     */
    public boolean findUser(User user) {
        return authenticationDAO.findUser(user);
    }

    /**
     * Метод создает администратора
     */
    public void createAdministrator() {
        authenticationDAO.createAdministrator();
    }

    /**
     * @return данные администратора в качестве объекта
     */
    public Administrator readAdministrator() {
        return authenticationDAO.readAdministrator();
    }

    /**
     * Метод изменяет данные администратора
     *
     * @param administrator - текущий администратор
     */
    public void updateAdministrator(Administrator administrator) {
        authenticationDAO.updateAdministrator(administrator);
    }

}
