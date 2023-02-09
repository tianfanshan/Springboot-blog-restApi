package com.springboot.blog.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDto {

    private Long id;

    @NotEmpty(message = "Name should not be null or empty")
    private String name;

    @NotEmpty(message = "Email should not be null or empty")
    @Email(message = "Must be a well-formed email address")
    private String email;

    @NotEmpty(message = "Comment body should not be null or empty")
    @Size(min = 10, message = "Comment body should have at least 10 character")
    private String body;
}
