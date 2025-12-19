package org.ikitadevs.kafkatestuserservice.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
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
public class UserDto {
    @Size(min = 1, max = 20, message = "Name must be between 1 and 20!")
    private String name;

    @Email(message = "Provide correct email syntax!")
    @Size(min = 5, max = 32, message = "Email must be between 5 and 32!")
    private String email;

    @Size(min = 1, max = 20, message = "Last name must be between 1 and 20!")
    private String lastname;
}
