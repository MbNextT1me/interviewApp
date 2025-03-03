package ru.gormikle.interviewapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Table(name="email_data", schema="interview_app")
@Data
public class EmailDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserEntity userEntity;

    @Column(name = "email", nullable = false, unique = true, length = 200)
    @Email(message = "Invalid email format")
    private String email;
}
