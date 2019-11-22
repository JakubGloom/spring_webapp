package net.atos.spring_webapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.atos.spring_webapp.model.enums.Category;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;
    @NotBlank
    private String title;
    @NotBlank
    @Type(type = "text")
    private String content;
    private LocalDateTime addedDate = LocalDateTime.now();
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY,
    cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "user_id")
    private User author;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    public Post(@NotBlank String title, @NotBlank String content, @NotBlank Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

}
