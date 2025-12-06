package com.example.laba4.pointChecker;

import org.springframework.stereotype.Service;

@Service
public class HitCheckService {
    
    public boolean checkHit(Double x, Double y, Integer r) {
         if (x >= 0 && y >= 0) {;
            return x * x + y * y <= r * r;
        }

        if (x >= 0 && y <= 0) {
            return x <= r && y >= -r;
        }
        

        if (x <= 0 && y <= 0) {
            return y > -x - r;
        }
        
        return false;
    }
}
