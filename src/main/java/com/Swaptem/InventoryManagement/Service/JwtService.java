package com.Swaptem.InventoryManagement.Service;
import com.Swaptem.InventoryManagement.Entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    private static final String SECRET_KEY = "y833hfefc3v8f3mvfhw89fmhahfa89fhvah88a93hvfa8h9hw8hfaw8vf83awhv98fawn3nrn3nf3iwf3fw3nfnf90j4g40jg93jg9j3g34fj4g39gh934g"; // Replace with your own secret key

    public String generateJwtToken(User userInput) {
        long currentTimeMillis = System.currentTimeMillis();
        long expirationTimeMillis = currentTimeMillis + 86400000; // 24 hours

        Integer userIdInteger = (Integer) userInput.getUserId();
        String userIdString = userIdInteger.toString();

        return Jwts.builder()
                .setSubject(userIdString)
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(expirationTimeMillis))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public int getUserIdFromJwtToken(String token) {
        // Workaround for REST API testing
        int userId = 0;
        if(token.equals("RestAPIUserId1")){
            userId = 1;
            return userId;
        } else if(token.equals("RestAPIUserId2")){
            userId = 2;
            return userId;
        } else if(token.equals("RestAPIUserId3")){
            userId = 3;
            return userId;
        }


        userId = Integer.parseInt(Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject());


        return userId;
    }
}









