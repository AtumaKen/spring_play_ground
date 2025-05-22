package com.example.spring_play_ground;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ContService {
    @Autowired
    public AesEncrypter aesEncrypter;

    public String cont(String rrn) throws Exception {
//        String transactionParameters = "5249101000999918" +//pan 1234567895555555
        String transactionParameters = "5399839222071010" +
                "000000012040" +//amount
                "000018" +//stan
                rrn +//rrn
                "2TAMNFY00000001" +//merchant id
                "0222";//trans date
        String s = aesEncrypter.encryptEcb(transactionParameters,
                "EeJ/7zrGciS2h2g45nVmI3l1YDrYiRzYXDTg5CdIIl4=");
        System.out.println(s);
//        String decrypt = aesEncrypter.decrypt(s, "EeJ/7zrGciS2h2g45nVmI3l1YDrYiRzYXDTg5CdIIl4=");
//        log.info("compare** {}",decrypt.equals(transactionParameters));
        return s;
//        String abcdefg = aesEncrypter.encrypt("thisisakey", "t3@mApt123456789");
//        log.info(abcdefg);


    }
}
