package models.menu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "menu_item")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "code", length = 50, nullable = false)
    public String code;

    @Column(name = "title", length = 100, nullable = false)
    public String title;

    @Column(name = "path", length = 200, nullable = false)
    public String path;

    @Column(name = "sort_order", nullable = false)
    public Integer sortOrder;

    @Column(name = "active", nullable = false)
    public Boolean active;

    public MenuItem() {
    }
}
