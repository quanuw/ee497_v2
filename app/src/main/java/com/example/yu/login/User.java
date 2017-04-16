package com.example.yu.login;

/**
 * Created by amand on 3/5/2017.
 */

import java.util.Date;
import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private int id;
    private String firstName;
    private String lastName;
    private int DOB;
    private int SSN;
    private String street;
    private String city;
    private String state;
    private int zipcode;
    private String userName;
    private String password;

    public User() {
        super();
    }

    private User(Parcel in) {
        super();
        this.id = in.readInt();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.DOB = in.readInt();
        this.SSN = in.readInt();
        this.street = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.zipcode = in.readInt();
        this.userName = in.readString();
        this.password = in.readString();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getDOB() {
        return DOB;
    }
    public void setDOB(int DOB) {
        this.DOB = DOB;
    }

    public int getSSN() {
        return SSN;
    }
    public void setSSN(int SSN) {
        this.SSN = SSN;
    }

    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public int getZipcode() {
        return zipcode;
    }
    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", first name=" + firstName + ", last name=" + lastName +
                ", dateOfBirth=" + DOB + ", SSN=" + SSN + ", street=" + street +
                ", city=" + city + ", state=" + state + ", zipcode=" + zipcode +
                ", user name=" + userName + ", password=" + password + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(getId());
        parcel.writeString(getFirstName());
        parcel.writeString(getLastName());
        parcel.writeLong(getDOB());
        parcel.writeInt(getSSN());
        parcel.writeString(getStreet());
        parcel.writeString(getCity());
        parcel.writeString(getState());
        parcel.writeInt(getZipcode());
        parcel.writeString(getUserName());
        parcel.writeString(getPassword());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}