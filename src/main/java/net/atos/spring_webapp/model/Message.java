package net.atos.spring_webapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
@NoArgsConstructor
@Data
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    private String title;
    private String message;
    private LocalDateTime addedDate = LocalDateTime.now();
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User receiver;

    public Message(String title, String message) {
        this.title = title;
        this.message = message;
    }
}
