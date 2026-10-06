package service.flat;

import dto.FlatData;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;
import model.Flat;
import repository.FlatRepository;
import service.exception.BadRequestException;
import service.exception.NotFoundException;

@Stateless
public class FlatOperationsService {
    @EJB
    private FlatRepository flatRepository;

    public FlatData maxArea() {
        return requiredResult(flatRepository.maxArea());
    }

    public long countByHouse(Long houseId) {
        if (houseId != null && houseId <= 0) {
            throw new BadRequestException("houseId must be positive");
        }
        return flatRepository.countByHouse(houseId);
    }

    public List<FlatData> namesStartingWith(String prefix) {
        if (prefix == null) {
            throw new BadRequestException("prefix is required");
        }
        return flatRepository.namesStartingWith(prefix).stream().map(FlatData::new).toList();
    }

    public FlatData mostExpensiveWithoutBalcony() {
        return requiredResult(flatRepository.mostExpensiveWithoutBalcony());
    }

    public FlatData mostExpensiveOfThree(Integer id1, Integer id2, Integer id3) {
        if (id1 == null || id2 == null || id3 == null || id1 <= 0 || id2 <= 0 || id3 <= 0) {
            throw new BadRequestException("id1, id2 and id3 must be positive");
        }
        if (id1.equals(id2) || id1.equals(id3) || id2.equals(id3)) {
            throw new BadRequestException("Three distinct flat IDs are required");
        }
        for (int id : new int[]{id1, id2, id3}) {
            if (flatRepository.find(id) == null) {
                throw new NotFoundException("flat not found: " + id);
            }
        }
        return requiredResult(flatRepository.mostExpensiveOfThree(id1, id2, id3));
    }

    private FlatData requiredResult(Flat flat) {
        if (flat == null) {
            throw new NotFoundException("No matching flats");
        }
        return new FlatData(flat);
    }
}
