package ru.gormikle.interviewapp.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
@Table(name="phone_data", schema="interview_app")
@Data
public class PhoneDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @Column(name = "phone", nullable = false, unique = true, length = 13)
    // Сомнительно насчет длины в 13 символов, учитывая что по формату нужен телефон в 11
    @Pattern(regexp = "79\\d{9}", message = "Phone must be in format 79207865432")
    private String phone;
}
