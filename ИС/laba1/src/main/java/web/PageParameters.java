package web;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import service.pagination.PageRequest;

public class PageParameters {
    @QueryParam("page")
    @DefaultValue("0")
    public String page;
    @QueryParam("size")
    @DefaultValue("20")
    public String size;
    @QueryParam("sort")
    @DefaultValue("id")
    public String sort;
    @QueryParam("direction")
    @DefaultValue("asc")
    public String direction;

    public PageRequest toRequest() {
        return new PageRequest(QueryNumbers.requiredInt(page, "page"),
                QueryNumbers.requiredInt(size, "size"), sort, direction);
    }
}
