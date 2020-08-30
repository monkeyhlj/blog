package com.monkey.po;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
@Entity
@Table(name="t_type")
public class Type {

    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "分类名称不能为空") //不为空校验
    private String name;

    @OneToMany(mappedBy = "type") //一对多 被维护
    private List<Blog> blogs = new ArrayList<>();


    public Type() {
    }

}
