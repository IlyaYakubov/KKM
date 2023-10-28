package kg.printer.kkm.domains;

public class Organization {

    private String typeOfOwnership, tax, name, magazine_name, inn, address_magazine, telephone_magazine;

    private final String[] typesOfOwnership = {"Индивидуальный предприниматель", "Юридическое лицо"};
    private final String[] taxation = {"Общая система", "Упрощенная система"};

    public String getTypeOfOwnership() {
        return typeOfOwnership;
    }

    public void setTypeOfOwnership(String typeOfOwnership) {
        this.typeOfOwnership = typeOfOwnership;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMagazine_name() {
        return magazine_name;
    }

    public void setMagazine_name(String magazine_name) {
        this.magazine_name = magazine_name;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getAddress_magazine() {
        return address_magazine;
    }

    public void setAddress_magazine(String address_magazine) {
        this.address_magazine = address_magazine;
    }

    public String getTelephone_magazine() {
        return telephone_magazine;
    }

    public void setTelephone_magazine(String telephone_magazine) {
        this.telephone_magazine = telephone_magazine;
    }

    public String[] getTypesOfOwnership() {
        return typesOfOwnership;
    }

    public String[] getTaxation() {
        return taxation;
    }

}
