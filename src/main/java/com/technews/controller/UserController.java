package com.technews.controller;

import com.technews.model.Post;
import com.technews.model.User;
import com.technews.repository.UserRepository;
import com.technews.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    VoteRepository voteRepository;

    @GetMapping("/api/users")
    public List<User> getAllusers(){

        List<User> userList = userRepository.findAll();
        for (User u : userList){
            List<Post> posts = u.getPosts();
            for (Post p : posts){
                p.setVoteCount(voteRepository.countVotesByPostId(p.getId()));
            }
        }
        return  userList;
    }

    @GetMapping("/api/users/{id}")
    public User getUserById(@PathVariable Integer id){
        User returnUser = userRepository.getById(id);
        List<Post> posts = returnUser.getPosts();
        for(Post p : posts){
            p.setVoteCount(voteRepository.countVotesByPostId(p.getId()));
        }
        return returnUser;
    }

    @PostMapping("/api/users")
    public User addUser(@RequestBody User user){
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        userRepository.save(user);
        return user;
    }

    @PutMapping("/api/users/{id}")
    public User updateUser(Integer id, User user){
            User tempUser = userRepository.getById(id);
            if(!tempUser.equals(null)){
                user.setId(tempUser.getId());
                userRepository.save(user);
            }
            return user;
    }

    @DeleteMapping("/api/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int id){
            userRepository.deleteById(id);
    }
}
