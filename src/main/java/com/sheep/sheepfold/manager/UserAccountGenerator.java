package com.sheep.sheepfold.manager;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class UserAccountGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    
    /**
     * 随机账号
     */
    public static Long generateUserAccount() {
        // 获取当前时间戳（毫秒）
        long timestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
        
        // 取时间戳后8位
        long timeComponent = timestamp % 100000000;
        
        // 生成3位随机数
        int randomComponent = RANDOM.nextInt(1000);
        
        // 组合: 时间戳后8位 + 3位随机数，确保在合理范围内
        long accountNumber = timeComponent * 1000 + randomComponent;
        
        // 确保生成的账号在5-10位范围内
        if (accountNumber < 10000) {
            accountNumber += 10000;
        }
        
        return accountNumber;
    }

}