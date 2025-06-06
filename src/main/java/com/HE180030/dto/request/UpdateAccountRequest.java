package com.HE180030.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateAccountRequest {
    int id;
    String username;
    String password;
    String email;
    boolean isAdmin;
    boolean isSell;
}
