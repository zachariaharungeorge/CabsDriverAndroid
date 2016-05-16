package com.rdxcabs.Beans;

/**
 * Created by arung on 30/4/16.
 */
public class DriverBean {

    private String fullName;
    private String phone;
    private String email;
    private String username;
    private String password;
    private String cabType;
    private String registrationNo;
    private String licenseNo;

    public DriverBean(){

    }

    public DriverBean(String fullName, String phone, String email, String username, String password, String cabType, String registrationNo, String licenseNo) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.password = password;
        this.cabType = cabType;
        this.registrationNo = registrationNo;
        this.licenseNo = licenseNo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCabType() {
        return cabType;
    }

    public void setCabType(String cabType) {
        this.cabType = cabType;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }
}
