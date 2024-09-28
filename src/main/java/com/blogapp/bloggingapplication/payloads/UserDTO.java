package com.blogapp.bloggingapplication.payloads;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {

    public interface PublicView {}

    public interface InternalView extends PublicView {}
    @JsonView(PublicView.class)
    @NotBlank(message = "Email address cannot be empty")
    @Email(message = "Email address must be valid (e.g., user@gmail.com)")
    String emailaddress;

    @JsonView(InternalView.class)
    String password;
    @JsonView(PublicView.class)
    String about;
    @JsonView(PublicView.class)
    private List<String> roles = new ArrayList<>();

    public UserDTO() {
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String passward) {
        this.password = passward;
    }
    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
