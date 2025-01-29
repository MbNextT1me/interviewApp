package ru.gormikle.interviewapp.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Table(name="email_data", schema="interview_app")
@Data
public class EmailData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "email", nullable = false, unique = true, length = 200)
    @Email(message = "Invalid email format")
    private String email;
}
