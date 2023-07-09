package com.chatgpt.model;


import lombok.Data;

@Data
public class PersonRecord {
   public String name;
   public int age;

    public PersonRecord(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public PersonRecord() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
//非常方便的自动类
//public record PersonRecord(String name, int age){
//
//}