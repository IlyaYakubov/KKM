package kg.printer.kkm.repositories;

public interface Database {

    void readData();

    void updateData();

    void addData();

    void deleteData();

    int lastPosition();

}
