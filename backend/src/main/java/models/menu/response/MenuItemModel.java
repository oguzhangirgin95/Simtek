package models.menu.response;

import java.util.ArrayList;
import java.util.List;

public class MenuItemModel {

    public String code;

    public String title;

    public String path;

    public List<MenuItemModel> children = new ArrayList<>();

    public MenuItemModel() {
    }

    public MenuItemModel(String code, String title, String path) {
        this.code = code;
        this.title = title;
        this.path = path;
    }
}
