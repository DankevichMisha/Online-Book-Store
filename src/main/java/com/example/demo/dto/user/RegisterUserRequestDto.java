package com.example.demo.dto.user;

import com.example.demo.validation.user.Email;
import com.example.demo.validation.user.FieldMatch;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@FieldMatch.List({
        @FieldMatch(first = "password", second = "repeatPassword", message = "password don't match")
})
public class RegisterUserRequestDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Length(min = 6, max = 20,
            message = "password must contain between 6 and 20 characters")
    private String password;
    @NotBlank
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String shippingAddress;
}
