package com.shyam.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shyam.blog.entity.Comment;



public interface CommentRepo  extends JpaRepository<Comment	, Integer> {

}