package service.auth;

import dto.AuthData;
import dto.AuthRequest;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.Locale;
import model.User;
import repository.UserRepository;
import service.exception.BadRequestException;
import service.exception.ConflictException;
import service.exception.UnauthorizedException;

@Stateless
public class AuthService {
    @EJB
    private UserRepository userRepository;
    @EJB
    private JwtService jwtService;

    public AuthData register(AuthRequest request) {
        String username = validate(request);
        if (!userRepository.create(username, PasswordSupport.hash(request.password))) {
            throw new ConflictException("Этот логин уже занят");
        }
        return jwtService.issue(username);
    }

    public AuthData login(AuthRequest request) {
        String username = validate(request);
        User user = userRepository.find(username);
        if (!PasswordSupport.matches(request.password, user == null ? null : user.getPasswordHash())) {
            throw new UnauthorizedException("Неверный логин или пароль");
        }
        return jwtService.issue(user.getUsername());
    }

    private String validate(AuthRequest request) {
        if (request == null || request.username == null || request.password == null) {
            throw new BadRequestException("Введите логин и пароль");
        }
        String username = request.username.trim().toLowerCase(Locale.ROOT);
        return username;
    }
}
