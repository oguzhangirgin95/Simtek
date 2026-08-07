package models.menu.response;

import java.util.ArrayList;
import java.util.List;

public class MenuListResponse {

    public List<MenuItemModel> items = new ArrayList<>();

    public Integer totalCount = 0;

    public MenuListResponse() {
    }
}
