package kg.printer.kkm.repositories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import kg.printer.kkm.domains.Organization;

public class OrganizationDAO {

    private final DatabaseDAO dbHelper;

    public OrganizationDAO(DatabaseDAO databaseDAO) {
        dbHelper = databaseDAO;
    }

    /**
     * @return объект организация с заполненными свойствами
     */
    public Organization readOrganization() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select organization
        Cursor cursor = db.rawQuery("select * from organizations", null);

        Organization organization = new Organization();

        if (cursor.moveToFirst()) {
            int typeOfOwnershipColIndex = cursor.getColumnIndex("type_of_ownership");
            int taxColIndex = cursor.getColumnIndex("taxation");
            int nameColIndex = cursor.getColumnIndex("name");
            int innColIndex = cursor.getColumnIndex("inn");
            int magazineNameColIndex = cursor.getColumnIndex("magazine_name");
            int addressMagazineColIndex = cursor.getColumnIndex("address_magazine");
            int telephoneMagazineColIndex = cursor.getColumnIndex("telephone_magazine");

            organization.setTypeOfOwnership(cursor.getString(typeOfOwnershipColIndex));
            organization.setTaxation(cursor.getString(taxColIndex));

            organization.setName(cursor.getString(nameColIndex));
            organization.setMagazineName(cursor.getString(magazineNameColIndex));
            organization.setInn(cursor.getString(innColIndex));
            organization.setMagazineAddress(cursor.getString(addressMagazineColIndex));
            organization.setMagazineTelephone(cursor.getString(telephoneMagazineColIndex));
        }

        cursor.close();

        return organization;
    }

    /**
     * Метод обновляет данные организации
     *
     * @param organization - организация
     */
    public void updateOrganization(Organization organization) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("type_of_ownership", organization.getTypeOfOwnership());
        cv.put("taxation", organization.getTaxation());
        cv.put("name", organization.getName());
        cv.put("inn", organization.getInn());
        cv.put("magazine_name", organization.getMagazineName());
        cv.put("address_magazine", organization.getMagazineAddress());
        cv.put("telephone_magazine", organization.getMagazineTelephone());

        db.update("organizations", cv, null, null);
    }

}
