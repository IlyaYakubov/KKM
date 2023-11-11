package kg.printer.kkm.domains;

public class Organization {

    private String typeOfOwnership, taxation, name, magazineName, inn, magazineAddress, magazineTelephone;

    private final String[] typesOfOwnership = {"Индивидуальный предприниматель", "Юридическое лицо"};
    private final String[] taxationList = {"Общая система", "Упрощенная система"};

    public String getTypeOfOwnership() {
        return typeOfOwnership;
    }

    public void setTypeOfOwnership(String typeOfOwnership) {
        this.typeOfOwnership = typeOfOwnership;
    }

    public String getTaxation() {
        return taxation;
    }

    public void setTaxation(String taxation) {
        this.taxation = taxation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMagazineName() {
        return magazineName;
    }

    public void setMagazineName(String magazineName) {
        this.magazineName = magazineName;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getMagazineAddress() {
        return magazineAddress;
    }

    public void setMagazineAddress(String magazineAddress) {
        this.magazineAddress = magazineAddress;
    }

    public String getMagazineTelephone() {
        return magazineTelephone;
    }

    public void setMagazineTelephone(String magazineTelephone) {
        this.magazineTelephone = magazineTelephone;
    }

    public String[] getTypesOfOwnership() {
        return typesOfOwnership;
    }

    public String[] getTaxationList() {
        return taxationList;
    }

}
