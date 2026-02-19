package com.quinzex.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService implements IotpService {

    private static final SecureRandom random = new SecureRandom();
    private static final int otpLength = 6;
    private static final Duration optExpiration = Duration.ofMinutes(5);
    private final RedisTemplate<String,Object> redisTemplate;

    public OtpService (RedisTemplate<String,Object> template){
        this.redisTemplate =template;

    }

    @Override
    public String generateOtp(String email){
       String otp = generateOtp();


        try{
            redisTemplate.opsForValue()
                    .set(buildKey(email), otp, optExpiration);
        }catch (Exception e){
            throw new RuntimeException("OTP service temporarily unavailable. Please try again.");
        }
        return otp;

    }
    private String buildKey(String email){
        return "otp" +
                ":register"+email;
        }

        private String buildLoginKey(String email){
        return "otp:login"+email;
        }
    @Override
    public boolean validateOtp(String email , String enteredOtp) {
try{

    Object StoredOtp =  redisTemplate.opsForValue().get(buildKey(email));
    if(StoredOtp==null){
        return false;
    }
    boolean isValid = StoredOtp.toString().equals(enteredOtp);
    if(isValid){
        redisTemplate.delete(buildKey(email));
    }
    return isValid;
}catch (Exception e){
    throw new RuntimeException("OTP service temporarily unavailable. Please try again.");
}


    }

    @Override
    public String generateLoginOtp(String email) {
        String otp = generateOtp();
        redisTemplate.opsForValue()
                .set(buildLoginKey(email), otp, 5, TimeUnit.MINUTES);
        return otp;
    }

    @Override
    public boolean validateLoginOtp(String email, String otp) {

        String key = buildLoginKey(email);

        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return false;
        }

        String storedOtp = value.toString();

        if (storedOtp.equals(otp)) {
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }

    private String generateOtp() {
        int number = random.nextInt(1_000_000);
        return String.format("%06d", number);
    }

}
