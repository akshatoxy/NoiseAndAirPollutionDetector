package com.example.noisedetector.Models;

public class User {
    public String name,email, location;

    public User(){

    }
    public User(String name,String email,String location){
        this.name= name;
        this.email= email;
        this.location= location;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
