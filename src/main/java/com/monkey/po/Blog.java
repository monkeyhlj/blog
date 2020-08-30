package com.monkey.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="t_blog")
public class Blog {

    @Id
    @GeneratedValue  //自动生成
    private Long id;
    private String title; //标题

    @Basic(fetch = FetchType.LAZY)
    @Lob  //大字段类型
    private String content; //内容
    private String firstPicture; //首图
    private String flag; //标记
    private Integer views; //浏览次数
    private boolean appreciation;
    private boolean shareStatement;
    private boolean commentabled;
    private boolean published;
    private boolean recommend;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @ManyToOne //多对一
    private Type type;

    @ManyToMany(cascade = {CascadeType.PERSIST}) //级联新增,当新增blog时，如果连同tags也需要新增，它会同时保存到数据库中
    private List<Tag> tags  = new ArrayList<>();

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "blog")//多方是关系的维护方
    private List<Comment> comments = new ArrayList<>();

    @Transient //不会记入数据库中
    private String tagIds;

    private String description;


    public void init(){
        this.tagIds = tagsToIds(this.getTags());
    }
    //1,2,3
    private String tagsToIds(List<Tag> tags){
        if(!tags.isEmpty()){
            StringBuffer ids = new StringBuffer();
            boolean flag = false;
            for(Tag tag:tags){
                if(flag){
                    ids.append(",");
                }else{
                    flag=true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        }else{
            return tagIds;
        }
    }


    public Blog() {
    }

}
