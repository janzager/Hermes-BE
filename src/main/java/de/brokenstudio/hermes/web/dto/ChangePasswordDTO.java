package de.brokenstudio.hermes.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangePasswordDTO {

    private String username;
    private String oldPassword;
    private String newPassword;

}
