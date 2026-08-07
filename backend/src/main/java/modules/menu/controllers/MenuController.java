package modules.menu.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.menu.request.MenuListRequest;
import models.menu.response.MenuListResponse;
import modules.menu.business.MenuBusiness;

@RestController
@RequestMapping("/menu")
public class MenuController {

    private final MenuBusiness menuBusiness;

    public MenuController(MenuBusiness menuBusiness) {
        this.menuBusiness = menuBusiness;
    }

    @PostMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public MenuListResponse MenuList(@RequestBody MenuListRequest menuListRequest) {
        return menuBusiness.MenuList(menuListRequest);
    }
}
