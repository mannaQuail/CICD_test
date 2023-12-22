package com.hacker.hacker.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="evaluation_id")
    private Long evaluationId;

    @Column
    private Long overall;

    @Column
    private Long pronunciation;

    @Column
    private Long fluency;

    @Column
    private Long integrity;

    @Column
    private Long rhythm;

    @Column
    private Long speed;

    @Column
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "transcript_id", nullable = false)
    @JsonBackReference
    private Transcript transcript;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    @JsonBackReference
    private Users users;

}
