package com.springboot.blog.payload;

import com.springboot.blog.entity.Post;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private Long id;

    private String name;

    private String description;
}
