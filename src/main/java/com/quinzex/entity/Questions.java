package com.quinzex.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "questions", schema = "public")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Questions {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String question;

    private String opt1;
    private String opt2;
    private String opt3;
    private String opt4;
    @Column(name = "correct_option")
    private String correctOption;
    private String category;
    private Long chapterId;
}
