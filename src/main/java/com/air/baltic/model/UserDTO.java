package com.air.baltic.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserDTO(
        @NotBlank
        @JsonProperty(required = true)
        @Size(min = 3, max = 50)
        String firstName,

        @NotBlank
        @JsonProperty(required = true)
        @Size(min = 2, max = 50)
        String lastName,

        @NotBlank
        @JsonProperty(required = true)
        @Email(message = "Email is not valid", regexp =
                "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
        String email
) {
}
