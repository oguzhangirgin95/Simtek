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

    /** Menu iki seviyelidir: modul ve altindaki islemler */
    @Transactional(readOnly = true)
    public MenuListResponse MenuList(MenuListRequest menuListRequest) {

        MenuListResponse menuListResponse = new MenuListResponse();

        List<MenuItem> modules = menuItemRepository.findByActiveTrueAndParentCodeIsNullOrderBySortOrderAsc();

        for (MenuItem module : modules) {

            MenuItemModel moduleModel = new MenuItemModel(module.code, module.title, module.path);

            List<MenuItem> transactions = menuItemRepository
                    .findByActiveTrueAndParentCodeOrderBySortOrderAsc(module.code);

            for (MenuItem transaction : transactions) {
                moduleModel.children.add(new MenuItemModel(transaction.code, transaction.title, transaction.path));
            }

            if (!moduleModel.children.isEmpty()) {
                menuListResponse.items.add(moduleModel);
            }
        }

        menuListResponse.totalCount = menuListResponse.items.size();

        return menuListResponse;
    }
}
