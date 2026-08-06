package models.general.response;

import java.util.ArrayList;
import java.util.List;

public class ResourceResponse {

    public String transactionName;

    public List<Resource> resources = new ArrayList<>();

    public ResourceResponse() {
    }

    public ResourceResponse(String transactionName) {
        this.transactionName = transactionName;
    }
}
