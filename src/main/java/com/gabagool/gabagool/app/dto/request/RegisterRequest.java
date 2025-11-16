package com.gabagool.gabagool.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 64, message = "First name is too short or too long")
    @Pattern(
            regexp = "[a-zA-Z'\\s\\-]+",
            message = "First name can only contain a-z, spaces, hyphens, and apostrophes"
    )
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 64, message = "Last name is too short or too long")
    @Pattern(
            regexp = "[a-zA-Z'\\s\\-]+",
            message = "Last name can only contain a-z, spaces, hyphens, and apostrophes"
    )
    private String lastName;

    @NotBlank(message = "Email is required")
    @Size(min = 5, max = 64, message = "Email is too short or too long")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email must be in format email@website.com"
    )
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 32, message = "Password must be 8-32 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*$",
            message = "Password must contain uppercase, lowercase, and a number"
    )
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public RegisterRequest() {
    }

    public RegisterRequest(String firstName, String lastName, String email, String password, String confirmPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
