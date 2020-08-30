package com.monkey.service;

import com.monkey.dao.BlogRepository;
import com.monkey.po.Blog;
import com.monkey.po.Type;
import com.monkey.util.MarkdownUtils;
import com.monkey.util.MyBeanUtils;
import com.monkey.vo.BlogQuery;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl  implements BlogService{

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }

    @Transactional
    @Override
    public Blog getAndconvert(Long id) {
        Blog blog = blogRepository.getOne(id);
        if(blog == null){
            try {
                throw new NotFoundException("该博客不存在！");
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();
        MarkdownUtils.markdownToHtmlExtensions(content);
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        blogRepository.updateViews(id);
        return b;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(!"".equals(blog.getTitle()) && blog.getTitle() != null){
                    predicates.add(cb.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));
                }
                if(blog.getTypeId() != null){
                    predicates.add(cb.equal(root.<Type>get("type").get("id"),blog.getTypeId()));
                }
                if(blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()])); //sql组合这些条件语句where
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");//关联查询
                return cb.equal(join.get("id"), tagId);
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query,pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Pageable pageable = PageRequest.of(0,size, Sort.by(Sort.Direction.DESC,"updateTime"));
        return blogRepository.findTop(pageable);
    }



    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if(blog.getId() == null){
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }else{
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.getOne(id);
        if(b == null){
            try {
                throw new NotFoundException("该博客不存在");
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
//        BeanUtils.copyProperties(blog,b);//数据源，被拷贝的对象
        BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

}
