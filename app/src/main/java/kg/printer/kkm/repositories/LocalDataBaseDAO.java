package kg.printer.kkm.repositories;

public interface LocalDataBaseDAO {

    void readData();

    void updateData();

    void addData();

    void deleteData();

    int lastPosition();

}
