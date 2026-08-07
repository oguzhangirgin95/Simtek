package models.menu.response;

public class MenuItemModel {

    public String code;

    public String title;

    public String path;

    public MenuItemModel() {
    }

    public MenuItemModel(String code, String title, String path) {
        this.code = code;
        this.title = title;
        this.path = path;
    }
}
