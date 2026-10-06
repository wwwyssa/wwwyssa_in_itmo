package service.pagination;

import java.util.Locale;
import java.util.Set;
import service.exception.BadRequestException;

public record PageRequest(int page, int size, String sort, String direction) {
    public PageRequest {
        if (page < 0) {
            throw new BadRequestException("page must be at least 0");
        }
        if (size < 1 || size > 100) {
            throw new BadRequestException("size must be between 1 and 100");
        }
        if ((long) page * size > Integer.MAX_VALUE) {
            throw new BadRequestException("page offset is too large");
        }
        if (sort == null || sort.isBlank()) {
            throw new BadRequestException("sort is required");
        }
        if (direction == null) {
            throw new BadRequestException("direction must be asc or desc");
        }
        direction = direction.toLowerCase(Locale.ROOT);
        if (!direction.equals("asc") && !direction.equals("desc")) {
            throw new BadRequestException("direction must be asc or desc");
        }
    }

    public int offset() {
        return page * size;
    }

    public void validateSort(Set<String> fields) {
        if (!fields.contains(sort)) {
            throw new BadRequestException("Unsupported sort field: " + sort
                    + ". Allowed: " + String.join(", ", fields.stream().sorted().toList()));
        }
    }
}
