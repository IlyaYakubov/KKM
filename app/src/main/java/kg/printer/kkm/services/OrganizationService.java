package kg.printer.kkm.services;

import kg.printer.kkm.domains.Organization;
import kg.printer.kkm.repositories.DatabaseDAO;
import kg.printer.kkm.repositories.OrganizationDAO;
import kg.printer.kkm.view.OrganizationActivity;

public class OrganizationService {

    private final OrganizationDAO organizationDAO;

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
     * @param organization - организация
     */
    public void updateOrganization(Organization organization) {
        organizationDAO.updateOrganization(organization);
    }

}
