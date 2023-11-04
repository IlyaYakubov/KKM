package kg.printer.kkm.domains;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private boolean isBackings, isDiscounts, isChangePrice, isOrders;
    private int listIndex;
    private String position, surname, name, secondName, inn, percentOfDiscount, password;

    public User() {
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(listIndex);

        dest.writeString(position);
        dest.writeString(surname);
        dest.writeString(name);
        dest.writeString(secondName);
        dest.writeString(inn);
        dest.writeString(percentOfDiscount);
        dest.writeString(password);

        boolean[] booleans = {isBackings, isDiscounts, isChangePrice, isOrders};
        dest.writeBooleanArray(booleans);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel parcel) {
        listIndex = parcel.readInt();

        position = parcel.readString();
        surname = parcel.readString();
        name = parcel.readString();
        secondName = parcel.readString();
        inn = parcel.readString();
        percentOfDiscount = parcel.readString();
        password = parcel.readString();

        boolean[] booleans = {isBackings, isDiscounts, isChangePrice, isOrders};
        parcel.readBooleanArray(booleans);
    }

}
