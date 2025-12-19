package org.ikitadevs.kafkatestuserservice.dto.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {
    @Size(min = 1, max = 20, message = "Name must be between 1 and 20!")
    @NotEmpty
    private String name;

    @Email(message = "Provide correct email syntax!")
    @Size(min = 5, max = 32, message = "Email must be between 5 and 32!")
    @NotEmpty
    private String email;

    @Size(min = 1, max = 20, message = "Last name must be between 1 and 20!")
    @NotEmpty
    private String lastname;


    @Size(min = 8, max = 25, message = "Password must be between 8 and 25!")
    @NotEmpty
    private String password;
}
