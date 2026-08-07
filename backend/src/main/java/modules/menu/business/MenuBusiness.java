package modules.menu.business;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import models.menu.entity.MenuItem;
import models.menu.request.MenuListRequest;
import models.menu.response.MenuItemModel;
import models.menu.response.MenuListResponse;
import modules.menu.repositories.MenuItemRepository;

@Service
public class MenuBusiness {

    private final MenuItemRepository menuItemRepository;

    public MenuBusiness(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Transactional(readOnly = true)
    public MenuListResponse MenuList(MenuListRequest menuListRequest) {

        MenuListResponse menuListResponse = new MenuListResponse();

        List<MenuItem> items = menuItemRepository.findByActiveTrueOrderBySortOrderAsc();

        for (MenuItem item : items) {
            menuListResponse.items.add(new MenuItemModel(item.code, item.title, item.path));
        }

        menuListResponse.totalCount = menuListResponse.items.size();

        return menuListResponse;
    }
}
