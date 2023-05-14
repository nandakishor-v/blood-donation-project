package com.example.blooddonor.Model;

public class User {
    String name,email,id,Phone,age,bgp,Type,profilepictureurl,search;

    public User() {
    }

    public User(String name, String email, String id, String phone, String age, String bgp, String type, String profilepictureurl, String search) {
        this.name = name;
        this.email = email;
        this.id = id;
        Phone = phone;
        this.age = age;
        this.bgp = bgp;
        Type = type;
        this.profilepictureurl = profilepictureurl;
        this.search = search;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBgp() {
        return bgp;
    }

    public void setBgp(String bgp) {
        this.bgp = bgp;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getProfilepictureurl() {
        return profilepictureurl;
    }

    public void setProfilepictureurl(String profilepictureurl) {
        this.profilepictureurl = profilepictureurl;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
