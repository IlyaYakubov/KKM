package kg.printer.kkm.domains;

public class Unit {

    private String name, fullName, code;

    public Unit(String name, String fullName, String code) {
        this.name = name;
        this.fullName = fullName;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCode() {
        return code;
    }

}
