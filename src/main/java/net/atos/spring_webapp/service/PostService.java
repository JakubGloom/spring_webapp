package net.atos.spring_webapp.service;

import net.atos.spring_webapp.model.Post;
import net.atos.spring_webapp.model.User;
import net.atos.spring_webapp.repository.PermissionRepository;
import net.atos.spring_webapp.repository.PostRepository;
import net.atos.spring_webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private PostRepository postRepository;
    private UserRepository userRepository;
    private PermissionRepository permissionRepository;
    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, PermissionRepository permissionRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
    }

    public List<Post> getAllPostsOrdered(Sort.Direction direction){
        return postRepository.findAll(Sort.by(direction,"addedDate"));
    }
    public Post getPostById(long postId){
        Post post = null;
        Optional<Post> postOptional = postRepository.findById(postId);
        if(postOptional.isPresent()){
            post = postOptional.get();
        }
        return post;
    }
    public void addNewPost(Post post, Authentication auth){
        if(auth != null) {
            UserDetails credentials = (UserDetails) auth.getPrincipal();
            String email = credentials.getUsername();
            post.setAuthor(userRepository.findFirstByEmail(email));
            postRepository.save(post);
        }
    }
    public String isAuthenticated(Authentication auth){
        if(auth != null){
            UserDetails credentals = (UserDetails) auth.getPrincipal();
            return credentals.getUsername();
        }
        return "not logged";
    }
    public boolean isAdmin(Authentication auth) {
        boolean isAdmin = false;
        if (auth != null) {
            UserDetails credentals = (UserDetails) auth.getPrincipal();
            String email = credentals.getUsername();
            // wyszukaj usera po adresie email
            User user = userRepository.findFirstByEmail(email);
            System.out.println("+++++++++++++" + credentals.getAuthorities() + "+++++++++++++");
            if (user.getRoles().contains(permissionRepository.getOne((byte) 2))) {
                isAdmin = true;
            }
        }
        return isAdmin;
    }


    public void deletePostById(long postId) {
        postRepository.deleteById(postId);
    }

    public void editPostById(Post post){
        Post editedPost = getPostById(post.getPostId());
        editedPost.setTitle(post.getTitle());
        editedPost.setCategory(post.getCategory());
        editedPost.setContent(post.getContent());

        postRepository.save(editedPost);
    }
}