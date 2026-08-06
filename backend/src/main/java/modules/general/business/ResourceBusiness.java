package modules.general.business;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import models.general.entity.ResourceItem;
import models.general.request.ResourceRequest;
import models.general.response.Resource;
import models.general.response.ResourceResponse;
import modules.general.repositories.ResourceRepository;

@Service
public class ResourceBusiness {

    private final ResourceRepository resourceRepository;

    public ResourceBusiness(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    /** transactionName'e ait resource listesi; 'general' tum ekranlarda ortak olanlardir */
    @Transactional(readOnly = true)
    public ResourceResponse Get(ResourceRequest resourceRequest) {

        String transactionName = resourceRequest == null || resourceRequest.transactionName == null
                ? ""
                : resourceRequest.transactionName;

        ResourceResponse resourceResponse = new ResourceResponse(transactionName);

        for (ResourceItem item : resourceRepository.findByTransactionNameOrderByKeyAsc(transactionName)) {
            resourceResponse.resources.add(new Resource(item.key, item.value));
        }

        return resourceResponse;
    }
}
