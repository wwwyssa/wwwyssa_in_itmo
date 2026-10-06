package service.flat;

import service.exception.BadRequestException;
import service.pagination.PageRequest;

public record FlatSearch(PageRequest pagination, String name, String houseName,
                         Integer numberOfRooms, Float timeToMetroOnFoot) {
    public FlatSearch {
        if (numberOfRooms != null && (numberOfRooms < 1 || numberOfRooms > 11)) {
            throw new BadRequestException("numberOfRooms must be between 1 and 11");
        }
        if (timeToMetroOnFoot != null && (!Float.isFinite(timeToMetroOnFoot)
                || timeToMetroOnFoot <= 0)) {
            throw new BadRequestException("timeToMetroOnFoot must be a finite positive number");
        }
    }
}
