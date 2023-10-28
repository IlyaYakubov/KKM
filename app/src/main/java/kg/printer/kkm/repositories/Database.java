package kg.printer.kkm.repositories;

import kg.printer.kkm.domains.Organization;

public interface Database {

    Organization readData();

    void updateData();

    void addData();

    void deleteData();

    int lastPosition();

}
