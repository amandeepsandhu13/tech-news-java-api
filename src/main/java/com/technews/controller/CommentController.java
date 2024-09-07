package com.technews.controller;

import com.technews.model.Comment;
import com.technews.repository.CommentRepository;
import com.technews.repository.PostRepository;
import com.technews.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CommentController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @GetMapping("/api/comments")
    public List<Comment> getAllComments(){
            List<Comment> commentList = commentRepository.findAll();
            return commentList;
    }

    @PostMapping("/api/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment createComment(@RequestBody Comment comment){
           return commentRepository.save(comment);
    }
    @PutMapping("/api/comments/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable int id, @RequestBody Comment comment){
       Optional<Comment> existingComment = commentRepository.findById(id);
       if(existingComment.isPresent()){
            comment.setId(id);
            Comment updatedComment = commentRepository.save(comment);
            return ResponseEntity.ok(comment);
       }else {
           return ResponseEntity.notFound().build();
       }
    }
    @DeleteMapping("/api/delete/{id}")
    public boolean deleteComment(@PathVariable int id){
        Optional<Comment> comment = commentRepository.findById(id);
        if(comment.isPresent()) {
            commentRepository.deleteById(id);
            return true;
        }else{
            return false;
        }
    }
}
