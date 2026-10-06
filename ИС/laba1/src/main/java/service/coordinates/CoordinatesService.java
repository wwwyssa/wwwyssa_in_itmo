package service.coordinates;

import dto.CoordinatesData;
import dto.CoordinatesRequest;
import dto.PageData;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import model.Coordinates;
import repository.CoordinatesRepository;
import service.exception.BadRequestException;
import service.exception.NotFoundException;
import service.notification.ChangeNotifier;
import service.pagination.PageRequest;

@Stateless
public class CoordinatesService {
    @EJB
    private CoordinatesRepository coordinatesRepository;
    @EJB
    private ChangeNotifier changeNotifier;

    public CoordinatesData create(CoordinatesRequest request) {
        if (request == null || request.x == null || request.y == null
                || !Float.isFinite(request.x) || !Float.isFinite(request.y) || request.y > 249) {
            throw new BadRequestException("x and y must be finite; y must be at most 249");
        }
        Coordinates coordinates = new Coordinates();
        coordinates.setX(request.x);
        coordinates.setY(request.y);
        coordinatesRepository.persist(coordinates);
        coordinatesRepository.flush();
        changeNotifier.changed();
        return new CoordinatesData(coordinates);
    }

    public CoordinatesData find(long id) {
        if (id <= 0) {
            throw new BadRequestException("id must be positive");
        }
        Coordinates coordinates = coordinatesRepository.find(id);
        if (coordinates == null) {
            throw new NotFoundException("coordinates not found");
        }
        return new CoordinatesData(coordinates);
    }

    public PageData<CoordinatesData> list(PageRequest page) {
        return coordinatesRepository.list(page).map(CoordinatesData::new);
    }
}
