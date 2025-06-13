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
                "0613";//trans date
        String s = aesEncrypter.encryptEcb(transactionParameters,
                "mV+R5N1KgfPDSeBgqV+XD4m6xTFaIO8o7W/NtZRVtVg=");
        System.out.println(s);
        String decrypt = aesEncrypter.decryptECB(s, "mV+R5N1KgfPDSeBgqV+XD4m6xTFaIO8o7W/NtZRVtVg=");
        log.info("compare** {}",decrypt.equals(transactionParameters));
        return s;
//        String abcdefg = aesEncrypter.encrypt("thisisakey", "t3@mApt123456789");
//        log.info(abcdefg);


    }
}
