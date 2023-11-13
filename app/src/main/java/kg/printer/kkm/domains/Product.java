package kg.printer.kkm.domains;

public class Product {

    private String name, price;
    private String unit;

    private int unitId;

    public Product() {
    }

    public Product(String name, String unit, int unitId, String price) {
        this.name = name;
        this.unit = unit;
        this.unitId = unitId;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getUnitId() {
        return unitId;
    }

    public String getPrice() {
        return price;
    }

}
