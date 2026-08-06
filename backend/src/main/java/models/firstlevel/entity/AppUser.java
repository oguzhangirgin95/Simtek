package models.firstlevel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @Column(name = "username", length = 50)
    public String username;

    @Column(name = "password", length = 100, nullable = false)
    public String password;

    @Column(name = "full_name", length = 100)
    public String fullName;

    public AppUser() {
    }
}
