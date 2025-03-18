package ru.gormikle.interviewapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gormikle.interviewapp.entity.EmailDataEntity;
import ru.gormikle.interviewapp.entity.PhoneDataEntity;
import ru.gormikle.interviewapp.entity.UserEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private List<String> phones;
    private List<String> emails;

    public static UserDto fromEntity(UserEntity user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setPhones(user.getPhoneDataEntityList()
                .stream().map(PhoneDataEntity::getPhone).collect(Collectors.toList()));
        dto.setEmails(user.getEmailDataEntityList()
                .stream().map(EmailDataEntity::getEmail).collect(Collectors.toList()));
        return dto;
    }
}
