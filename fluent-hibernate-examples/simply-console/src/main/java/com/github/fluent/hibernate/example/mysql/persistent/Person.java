package com.github.fluent.hibernate.example.mysql.persistent;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 *
 *
 * @author V.Ladynev
 */
@MappedSuperclass
public class Person {

    private String name;

    private Integer age;

    @Column(name = "f_name")
    public String getName() {
        return name;
    }

    @Column(name = "f_age")
    public Integer getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

}
