package kg.printer.kkm.services;

import kg.printer.kkm.domains.Organization;
import kg.printer.kkm.repositories.DatabaseDAO;
import kg.printer.kkm.repositories.OrganizationDAO;
import kg.printer.kkm.view.OrganizationActivity;

public class OrganizationService {

    private OrganizationDAO organizationDAO;

    public OrganizationService(OrganizationActivity organizationActivity) {
        organizationDAO = new OrganizationDAO(new DatabaseDAO(organizationActivity));
    }

    /**
     * @return объект организация с заполненными свойствами
     */
    public Organization readOrganization() {
        return organizationDAO.readOrganization();
    }

    /**
     * Метод обновляет данные организации
     *
     * @param typeOfOwnership - форма собственности
     * @param taxation - система налогооблажения
     * @param org_name - наименование организации
     * @param inn - ИНН организации
     * @param magazine_name - наименование торговой точки
     * @param address_magazine - адрес торговой точки
     * @param telephone_magazine - телефон торговой точки
     */
    public void updateOrganization(String typeOfOwnership,
                                   String taxation,
                                   String org_name,
                                   String inn,
                                   String magazine_name,
                                   String address_magazine,
                                   String telephone_magazine) {
        organizationDAO.updateOrganization(typeOfOwnership, taxation, org_name, inn, magazine_name, address_magazine, telephone_magazine);
    }

}
