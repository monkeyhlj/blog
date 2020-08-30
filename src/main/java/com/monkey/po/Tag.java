package com.monkey.po;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="t_tag")
public class Tag {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "tags")  //被维护
    private List<Blog> blogs = new ArrayList<>();

    public Tag() {
    }

}
