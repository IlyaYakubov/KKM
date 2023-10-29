package kg.printer.kkm.domains;

public class User {

    private boolean isBackings, isDiscounts, isChangePrice, isOrders;
    private int listIndex;
    private String position, surname, name, secondName, inn, percentOfDiscount, password;

    public int getListIndex() {
        return listIndex;
    }

    public void setListIndex(int listIndex) {
        this.listIndex = listIndex;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isBackings() {
        return isBackings;
    }

    public void setBackings(boolean backings) {
        isBackings = backings;
    }

    public boolean isDiscounts() {
        return isDiscounts;
    }

    public void setDiscounts(boolean discounts) {
        isDiscounts = discounts;
    }

    public boolean isChangePrice() {
        return isChangePrice;
    }

    public void setChangePrice(boolean changePrice) {
        isChangePrice = changePrice;
    }

    public boolean isOrders() {
        return isOrders;
    }

    public void setOrders(boolean orders) {
        isOrders = orders;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getPercentOfDiscount() {
        return percentOfDiscount;
    }

    public void setPercentOfDiscount(String percentOfDiscount) {
        this.percentOfDiscount = percentOfDiscount;
    }
}
