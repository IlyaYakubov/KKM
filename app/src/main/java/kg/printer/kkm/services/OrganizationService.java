package kg.printer.kkm.services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import kg.printer.kkm.domains.Organization;
import kg.printer.kkm.repositories.DatabaseDAO;
import kg.printer.kkm.view.OrganizationActivity;

public class OrganizationService {

    private final DatabaseDAO dbHelper;

    public OrganizationService(OrganizationActivity organizationActivity) {
        this.dbHelper = new DatabaseDAO(organizationActivity);
    }

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
            organization.setTax(cursor.getString(taxColIndex));

            organization.setName(cursor.getString(nameColIndex));
            organization.setMagazine_name(cursor.getString(magazineNameColIndex));
            organization.setInn(cursor.getString(innColIndex));
            organization.setAddress_magazine(cursor.getString(addressMagazineColIndex));
            organization.setTelephone_magazine(cursor.getString(telephoneMagazineColIndex));
        }

        cursor.close();

        return organization;
    }

    public void updateOrganization(String typeOfOwnership,
                                   String tax,
                                   String org_name,
                                   String inn,
                                   String magazine_name,
                                   String address_magazine,
                                   String telephone_magazine) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("type_of_ownership", typeOfOwnership);
        cv.put("taxation", tax);
        cv.put("name", org_name);
        cv.put("inn", inn);
        cv.put("magazine_name", magazine_name);
        cv.put("address_magazine", address_magazine);
        cv.put("telephone_magazine", telephone_magazine);

        db.update("organizations", cv, null, null);
    }

}
