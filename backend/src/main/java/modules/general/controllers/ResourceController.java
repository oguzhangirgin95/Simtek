package modules.general.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.general.request.ResourceRequest;
import models.general.response.ResourceResponse;
import modules.general.business.ResourceBusiness;


@RestController
@RequestMapping("/resource")
public class ResourceController {

    private final ResourceBusiness resourceBusiness;

    public ResourceController(ResourceBusiness resourceBusiness) {
        this.resourceBusiness = resourceBusiness;
    }

    @PostMapping(path = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResourceResponse Get(@RequestBody ResourceRequest resourceRequest) {
        return resourceBusiness.Get(resourceRequest);
    }
}
