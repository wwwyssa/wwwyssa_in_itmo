package service.flat;

import dto.FlatData;
import dto.FlatRequest;
import dto.PageData;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import model.Coordinates;
import model.Flat;
import model.House;
import repository.CoordinatesRepository;
import repository.FlatRepository;
import repository.HouseRepository;
import service.exception.BadRequestException;
import service.exception.NotFoundException;
import service.notification.ChangeNotifier;

@Stateless
public class FlatService {
    @EJB
    private FlatRepository flatRepository;
    @EJB
    private HouseRepository houseRepository;
    @EJB
    private CoordinatesRepository coordinatesRepository;
    @EJB
    private ChangeNotifier changeNotifier;

    public PageData<FlatData> list(FlatSearch search) {
        return flatRepository.list(search).map(FlatData::new);
    }

    public FlatData create(FlatRequest request) {
        validate(request);

        Flat flat = new Flat();
        applyChanges(flat, request);
        flatRepository.persist(flat);
        flatRepository.flush();
        changeNotifier.changed();
        return new FlatData(flat);
    }

    public FlatData find(int id) {
        return new FlatData(requireFlat(id));
    }

    public FlatData update(int id, FlatRequest request) {
        Flat flat = requireFlat(id);

        validate(request);
        applyChanges(flat, request);
        flatRepository.flush();
        changeNotifier.changed();
        return new FlatData(flat);
    }

    public FlatData delete(int id) {
        Flat flat = requireFlat(id);

        FlatData deleted = new FlatData(flat);
        flatRepository.remove(flat);
        flatRepository.flush();
        changeNotifier.changed();
        return deleted;
    }

    private void validate(FlatRequest request) {
        if (request == null || request.name == null || request.name.isBlank()) {
            throw new BadRequestException("name must not be blank");
        }
        if (request.area == null || request.area <= 0) {
            throw new BadRequestException("area must be positive");
        }
        if (request.price == null || !Double.isFinite(request.price) || request.price <= 0) {
            throw new BadRequestException("price must be a finite positive number");
        }
        if (request.timeToMetroOnFoot == null || !Float.isFinite(request.timeToMetroOnFoot)
                || request.timeToMetroOnFoot <= 0) {
            throw new BadRequestException("timeToMetroOnFoot must be a finite positive number");
        }
        if (request.numberOfRooms == null || request.numberOfRooms < 1 || request.numberOfRooms > 11) {
            throw new BadRequestException("numberOfRooms must be between 1 and 11");
        }
        if (request.view == null) {
            throw new BadRequestException("view is required");
        }
    }

    private Flat requireFlat(int id) {
        if (id <= 0) {
            throw new BadRequestException("id must be positive");
        }
        Flat flat = flatRepository.find(id);
        if (flat == null) {
            throw new NotFoundException("flat not found");
        }
        return flat;
    }

    private void applyChanges(Flat flat, FlatRequest request) {
        Coordinates coordinates;
        if (request.coordinatesId != null) {
            if (request.coordinatesId <= 0) {
                throw new BadRequestException("coordinatesId must be positive");
            }
            if (request.x != null || request.y != null) {
                throw new BadRequestException("Use coordinatesId or x and y, not both");
            }
            coordinates = coordinatesRepository.find(request.coordinatesId);
            if (coordinates == null) {
                throw new BadRequestException("coordinatesId does not exist");
            }
        } else {
            if (request.x == null || request.y == null || !Float.isFinite(request.x)
                    || !Float.isFinite(request.y) || request.y > 249) {
                throw new BadRequestException("x and y must be finite; y must be at most 249");
            }
            coordinates = new Coordinates();
            coordinates.setX(request.x);
            coordinates.setY(request.y);
            coordinatesRepository.persist(coordinates);
        }

        House house = null;
        if (request.houseId != null) {
            if (request.houseId <= 0) {
                throw new BadRequestException("houseId must be positive");
            }
            house = houseRepository.find(request.houseId);
            if (house == null) {
                throw new BadRequestException("houseId does not exist");
            }
        }

        flat.setName(request.name);
        flat.setCoordinates(coordinates);
        flat.setArea(request.area);
        flat.setPrice(request.price);
        flat.setBalcony(request.balcony);
        flat.setTimeToMetroOnFoot(request.timeToMetroOnFoot);
        flat.setNumberOfRooms(request.numberOfRooms);
        flat.setFurniture(request.furniture);
        flat.setView(request.view);
        flat.setTransport(request.transport);
        flat.setHouse(house);
    }
}
