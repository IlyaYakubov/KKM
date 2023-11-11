package kg.printer.kkm.services;

import java.util.ArrayList;

import kg.printer.kkm.domains.Unit;
import kg.printer.kkm.repositories.DatabaseDAO;
import kg.printer.kkm.repositories.UnitDAO;
import kg.printer.kkm.view.UnitActivity;
import kg.printer.kkm.view.UnitsActivity;

public class UnitService {

    private final UnitDAO unitDAO;

    public UnitService(UnitsActivity unitsActivity) {
        unitDAO = new UnitDAO(new DatabaseDAO(unitsActivity));
    }

    public UnitService(UnitActivity unitActivity) {
        unitDAO = new UnitDAO(new DatabaseDAO(unitActivity));
    }

    /**
     * Метод создает единицу измерения
     *
     * @param unit - единица измерения
     */
    public void createUnit(Unit unit) {
        unitDAO.createUnit(unit);
    }

    /**
     * @return список единиц измерения
     */
    public ArrayList<String> readUnits() {
        return unitDAO.readUnits();
    }

    /**
     * Метод изменяет запись единицы измерения
     *
     * @param unit - единица измерения
     * @param listIndex - индекс в списке
     */
    public void updateUnit(Unit unit, int listIndex) {
        unitDAO.updateUnit(unit, listIndex);
    }

    /**
     * @param listIndex - индекс в списке
     * @return единица измерения
     */
    public Unit findUnitByListIndex(int listIndex) {
        return unitDAO.findUnitByListIndex(listIndex);
    }

}
