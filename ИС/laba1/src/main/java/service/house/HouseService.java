package service.house;

import dto.HouseData;
import dto.HouseRequest;
import dto.PageData;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import model.House;
import repository.HouseRepository;
import service.exception.BadRequestException;
import service.exception.NotFoundException;
import service.notification.ChangeNotifier;
import service.pagination.PageRequest;

@Stateless
public class HouseService {
    @EJB
    private HouseRepository houseRepository;
    @EJB
    private ChangeNotifier changeNotifier;

    public HouseData create(HouseRequest request) {
        if (request == null || request.name == null) {
            throw new BadRequestException("house name is required");
        }
        if (request.year == null || request.year <= 0) {
            throw new BadRequestException("house year must be positive");
        }
        if (request.numberOfFloors == null || request.numberOfFloors <= 0) {
            throw new BadRequestException("numberOfFloors must be positive");
        }
        House house = new House();
        house.setName(request.name);
        house.setYear(request.year);
        house.setNumberOfFloors(request.numberOfFloors);
        houseRepository.persist(house);
        houseRepository.flush();
        changeNotifier.changed();
        return new HouseData(house);
    }

    public HouseData find(long id) {
        if (id <= 0) {
            throw new BadRequestException("id must be positive");
        }
        House house = houseRepository.find(id);
        if (house == null) {
            throw new NotFoundException("house not found");
        }
        return new HouseData(house);
    }

    public PageData<HouseData> list(PageRequest page, String name) {
        return houseRepository.list(page, name).map(HouseData::new);
    }
}
