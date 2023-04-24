package com.shyam.blog.repositories;



import org.springframework.data.jpa.repository.JpaRepository;

import com.shyam.blog.entity.Category;



public interface CategoryRepo extends JpaRepository<Category, Integer> {

}
