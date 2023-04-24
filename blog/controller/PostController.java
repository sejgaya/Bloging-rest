 package com.shyam.blog.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

//import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.util.StreamUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shyam.blog.config.AppConstants;
import com.shyam.blog.entity.Post;
import com.shyam.blog.playload.ApiResponse;
import com.shyam.blog.playload.PostDto;
import com.shyam.blog.playload.PostResponse;
import com.shyam.blog.service.PostService;
import com.shyam.blog.service.FileService;

import jakarta.servlet.http.HttpServletResponse;



    @RestController
	@RequestMapping("/api")
	public class PostController {

		@Autowired
		private PostService postService;
		
		@Autowired
		private FileService fileService;
		
		@Value("${project.image}")
		private String path;
    
//		create

		@PostMapping("/users/{userId}/category/{categoryId}/posts")
		public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable Integer userId,
				@PathVariable Integer categoryId) {
			PostDto createPost = this.postService.createPost(postDto, userId, categoryId);
			return new ResponseEntity<PostDto>(createPost, HttpStatus.CREATED);
		}
		
		// get by user

		@GetMapping("/users/{userId}/posts")
		public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable Integer userId) {

			List<PostDto> posts = this.postService.getPostsByUser(userId);
			return new ResponseEntity<List<PostDto>>(posts, HttpStatus.OK);

		}

		// get by category

		@GetMapping("/category/{categoryId}/posts")
		public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable Integer categoryId) {

			List<PostDto> posts = this.postService.getPostsByCategory(categoryId);
			return new ResponseEntity<List<PostDto>>(posts, HttpStatus.OK);
		}
		
		
		
		// get all posts

		@GetMapping("/posts")
		public ResponseEntity<PostResponse> getAllPost(
				@RequestParam(value = "pageNumber", defaultValue =AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
				@RequestParam(value = "pageSize", defaultValue =AppConstants.PAGE_SIZE, required = false) Integer pageSize,
				@RequestParam(value = "sortBy", defaultValue =AppConstants.SORT_BY, required = false) String sortBy,
				@RequestParam(value = "sortDir", defaultValue =AppConstants.SORT_DIR, required = false) String sortDir) {

			PostResponse postResponse = this.postService.getAllPost(pageNumber, pageSize, sortBy, sortDir);
			return new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
		}
		
		// get post details by id

		@GetMapping("/posts/{postId}")
		public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {

			PostDto postDto = this.postService.getPostById(postId);
			return new ResponseEntity<PostDto>(postDto, HttpStatus.OK);

		}
		
		// delete post
		@DeleteMapping("/posts/{postId}")
		public ApiResponse deletePost(@PathVariable Integer postId) {
			this.postService.deletePost(postId);
			return new ApiResponse("Post is successfully deleted !!", true);
		}
		
		// update post

		@PutMapping("/posts/{postId}")
		public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable Integer postId) {
			PostDto updatePost = this.postService.updatePost(postDto,postId);
			return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);
		}
		
		// search
		@GetMapping("/posts/search/{keywords}")
		public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable("keywords") String keywords) {
			List<PostDto> result = this.postService.searchPosts(keywords);
			return new ResponseEntity<List<PostDto>>(result, HttpStatus.OK);
		}
		
		// post image upload

		@PostMapping("/posts/image/upload/{postId}")
		public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image,
				@PathVariable Integer postId) throws IOException {

			PostDto postDto = this.postService.getPostById(postId);
			
			String fileName = this.fileService.uploadImage(path, image);
			postDto.setImageName(fileName);
			PostDto updatePost = this.postService.updatePost(postDto, postId);
			return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);

		}
		
		//method to serve files
	    @GetMapping(value = "/posts/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
	    public void downloadImage(
	            @PathVariable("imageName") String imageName,
	            HttpServletResponse response
	    ) throws IOException {

	        InputStream resource = this.fileService.getResource(path, imageName);
	        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
	        StreamUtils.copy(resource,response.getOutputStream())   ;

	    }

}
