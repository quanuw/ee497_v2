package com.example.yu.login.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by amand on 4/9/2017.
 */


public class User implements Parcelable{
    public static final String TAG = User.class.getSimpleName();
    public static final String TABLE = "User";
    // Labels Table Columns names
    public static final String KEY_UserId = "UserId";
    public static final String KEY_LoginName = "LoginName";
    public static final String KEY_LoginPW = "LoginPW";
    public static final String KEY_DOB = "DOB";
    public static final String KEY_LastName = "LastName";
    public static final String KEY_FirstName = "FirstName";
    public static final String KEY_Email = "Email";

    private int userId;
    private String loginName;
    private String loginPW;
    private String DOB;
    private String lastName;
    private String firstName;
    private String email;

    public User() {
        super();
    }

    public User(int userId, String loginName, String loginPW, String DOB, String lastName, String firstName, String email) {
        super();
        this.userId = userId;
        this.loginName = loginName;
        this.loginPW = loginPW;
        this.DOB = DOB;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
    }


    private User(Parcel in) {
        super();
        this.userId = in.readInt();
        this.loginName = in.readString();
        this.loginPW = in.readString();
        this.DOB = in.readString();
        this.lastName = in.readString();
        this.firstName = in.readString();
        this.email = in.readString();
    }

    public int getUserId() {
        return userId;
    }

    public String getLoginName() {
        return loginName;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPW() {
        return loginPW;
    }
    public void setLoginPW(String loginPW) {
        this.loginPW = loginPW;
    }

    public String getDOB() {
        return DOB;
    }
    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public String toString() {
        return "User ID:" + userId + ", Login Name:" + loginName + ", Login Password:" + loginPW
                + ", DOB:" + DOB + ", Last Name:" + lastName + ", First Name:" + firstName
                + ", Email:" + email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        //parcel.writeInt(getUserId());
        parcel.writeString(getLoginName());
        parcel.writeString(getLoginPW());
        parcel.writeString(getDOB());
        parcel.writeString(getLastName());
        parcel.writeString(getFirstName());
        parcel.writeString(getEmail());
    }
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + userId;
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
        if (userId != other.userId)
            return false;
        return true;
    }
}

