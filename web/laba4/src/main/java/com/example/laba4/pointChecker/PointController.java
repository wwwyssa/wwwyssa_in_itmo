package com.example.laba4.pointChecker;

import com.example.laba4.auth.UserRepository;
import com.example.laba4.pointChecker.dto.PointRequest;
import com.example.laba4.pointChecker.dto.PointResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/points")
public class PointController {
    private static final Logger logger = LoggerFactory.getLogger(PointController.class);
    
    private final PointRepository pointRepository;
    private final HitCheckService hitCheckService;
    private final UserRepository userRepository;

    public PointController(PointRepository pointRepository, HitCheckService hitCheckService, UserRepository userRepository) {
        this.pointRepository = pointRepository;
        this.hitCheckService = hitCheckService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<PointResponse> createPoint(
            @Valid @RequestBody PointRequest request,
            Authentication authentication) {
        

        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        
        String username = authentication.getName();
        
        var userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Long userId = userOpt.get().getId();
        Boolean hit = hitCheckService.checkHit(request.getX(), request.getY(), request.getR());

        
        pointRepository.savePoint(userId, request.getX(), request.getY(), request.getR(), hit);
        
        PointResponse response = new PointResponse(
                null,
                request.getX(),
                request.getY(),
                request.getR(),
                hit,
                null
        );

        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getPoints(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        
        String username = authentication.getName();
        var userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Long userId = userOpt.get().getId();
        List<Map<String, Object>> points = pointRepository.findByUserId(userId);
        
        return ResponseEntity.ok(points);
    }
}
