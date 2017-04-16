package com.example.yu.login.data.model;

/**
 * Created by amand on 4/9/2017.
 */


public class User {
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

    private String userId;
    private String loginName;
    private String loginPW;
    private String DOB;
    private String lastName;
    private String firstName;
    private String email;

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
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
}

