package net.atos.spring_webapp.controller;

import net.atos.spring_webapp.model.Post;
import net.atos.spring_webapp.model.User;
import net.atos.spring_webapp.model.enums.Category;
import net.atos.spring_webapp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class PostController {
    private PostService postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }
    @GetMapping("/")
    public String allPosts(Model model, Authentication auth){
        model.addAttribute("isAuth",postService.isAuthenticated(auth));
        model.addAttribute("isAdmin",postService.isAdmin(auth));
        model.addAttribute("user", new User("X","X"));
        model.addAttribute("posts", postService.getAllPostsOrdered(Sort.Direction.DESC));
        // obiekt pusty do formularza dodającego nowego posta
        model.addAttribute("newpost", new Post());
        model.addAttribute("categories", Category.values());
        return "index";     // -> nazwa widoku html
    }
    @GetMapping("/post&{post_id}")
    public String getPostById(@PathVariable("post_id") long postId, Model model){
        model.addAttribute("post",postService.getPostById(postId));
        return "post";
    }
    @PostMapping("/addpost")
    public String addnewPost(
            @ModelAttribute("newpost") @Valid Post newpost,
            BindingResult bindingResult,
            Model model,
            Authentication auth){
        if(bindingResult.hasErrors()){
            model.addAttribute("posts", postService.getAllPostsOrdered(Sort.Direction.DESC));
            model.addAttribute("categories", Category.values());
            return "index";
        }
        postService.addNewPost(newpost, auth);
        return "redirect:/";
    }
    @GetMapping("/post/delete/{post_id}")
    public String deletePost(
            @PathVariable("post_id") long postId){
        postService.deletePostById(postId);
        return "redirect:/";
    }
    @PostMapping("/post/delete")
    public String deletePostDELETE(
            @RequestParam("post_id") long postId
    ){
        postService.deletePostById(postId);
        return "redirect:/";
    }
    @GetMapping("/post&edit&{post_id}")
    public String editPost(@PathVariable("post_edit") long postId, Model model){
        model.addAttribute("post",postService.getPostById(postId));
        model.addAttribute("categories", Category.values());
        return "editPost";
    }

    @PostMapping("/editpost")
    public String editPostById(@ModelAttribute("post") @Valid Post post, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "editPost";
        }
        postService.editPostById(post);
        return "redirect:/";
    }
}