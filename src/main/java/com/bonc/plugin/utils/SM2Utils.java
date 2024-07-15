package com.bonc.plugin.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.plugin.agent.entity.DeptFloorDictEntity;
import com.bonc.plugin.agent.entity.RoomQueryEntityDto;
import com.bonc.plugin.agent.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * 国密非对称加密SM2工具类
 */
@Slf4j
public class SM2Utils {

    static final BouncyCastleProvider bc = new BouncyCastleProvider();

    /**
     * 生成服务器公私钥对
     * @return
     */
//    public static Map<String,String> generateKey(){
//        KeyPair pair = SecureUtil.generateKeyPair("SM2");
//        Map<String,String> map = new HashMap<>();
//        map.put("publicKey", exportPublicKey(pair.getPublic()));
//        map.put("privateKey",exportPrivateKey(pair.getPrivate()));
//        return map;
//    }



    public static String encryptByPublicKey(String data, String publicKeyStr){
        SM2 sm2 = SmUtil.sm2();
        PublicKey publicKey = strToPublicKey(publicKeyStr);
        if(publicKey != null) {
            try {
                sm2.setPublicKey(publicKey);
                String s = sm2.encryptBcd(data, KeyType.PublicKey);
                return s;
            } catch (Exception e) {
                log.error("加密数据发生异常");
            }
        }
        return null;
    }

    public static String decryptByPrivateKey(String data, String privateKeyStr){
        SM2 sm2obj = SmUtil.sm2();
        PrivateKey privateKey = strToPrivateKey(privateKeyStr);
        if(privateKey != null) {
            sm2obj.setPrivateKey(privateKey);
            String decStr = StrUtil.utf8Str(sm2obj.decryptFromBcd(data, KeyType.PrivateKey));
            return decStr;
        }
        return null;
    }


    /**
     * 从字符串中读取 私钥 key
     * @param privateKeyStr String
     * @return PrivateKey
     */
    public static PrivateKey strToPrivateKey(String privateKeyStr) {
        PrivateKey privateKey = null;
        try {
            byte[] encPriv = Base64.decode(privateKeyStr);
            KeyFactory keyFact = KeyFactory.getInstance("EC", bc);
            privateKey = keyFact.generatePrivate(new PKCS8EncodedKeySpec(encPriv));
        } catch (NoSuchAlgorithmException e) {
            log.info("字符串转私钥失败：{}", e);
            privateKey=null;
        } catch (InvalidKeySpecException e) {
            log.info("字符串转私钥失败：{}", e);
            privateKey=null;
        }
        return privateKey;
    }

    /**
     * 从字符串中读取 公钥 key
     * @param publicKeyStr String
     * @return PublicKey
     */
    public  static PublicKey strToPublicKey(String publicKeyStr){
        PublicKey publicKey =  null;
        try {
            byte[] encPub = Base64.decode(publicKeyStr);
            KeyFactory keyFact = KeyFactory.getInstance("EC", bc);
            publicKey = keyFact.generatePublic(new X509EncodedKeySpec(encPub));
        } catch (NoSuchAlgorithmException e) {
            log.info("字符串转公钥失败：{}",e);
            publicKey=null;
        } catch (InvalidKeySpecException e) {
            log.info("字符串转公钥失败：{}",e);
            publicKey=null;
        }
        return publicKey;
    }

    public static void main(String[] args) throws Exception {
//        Map<String, Object> keys = generateKey();
//        String publicKeyStr = exportPublicKey((PublicKey) keys.get("publicKey"));
//        String privateKeyStr = exportPrivateKey((PrivateKey)keys.get("privateKey"));
//        exportPublicKey((PublicKey) keys.get("publicKey"), "D://publicKey.key");
//        //智能体解密接口
//        String publicKeyStr = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEaWopmokuBUC/XSjLDsvu5CyxPlDp8SVRl8y85eKKc2/V/CLsgVMAgEPo3Uk7RQaSxmmlEUHZAlrmU6WrE4qpkg==";

//        办公app-测试
//        String publicKeyStr = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEnzolQiPi7oMTtZrEw9zfRnvELf708tfNxieVTCui9k0SdVmFTSGjZqMyVb6kvAjGL2151dbUe4UdLDQD6ZM5Tw==";
//        String privateKeyStr = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQg0tml4+p8t+A0USqucKvNpr+KRMUZ5k+ZpfIiOj221PegCgYIKoEcz1UBgi2hRANCAASfOiVCI+LugxO1msTD3N9Ge8Qt/vTy183GJ5VMK6L2TRJ1WYVNIaNmozJVvqS8CMYvbXnV1tR7hR0sNAPpkzlP";
//       办公app-正式
        String publicKeyStr = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE8W9gNV2aFQONsBx/ilOSTWKJuQyenOhiCXoZGCl47qa/VmCxX3WHiZS3Kzgyry4hCAC15q5OZsosoROSGwqdXg==";
//        System.out.println("公钥：" + publicKeyStr);
//        System.out.println("私钥：" + privateKeyStr);
         String uuid= String.valueOf(UUID.randomUUID());
        String encryptStr = "{\"orgName\":\"上海市分公司数字化部\",\"phone\":\"13167101771\",\"orgCode\":353959,\"name\":\"王晨晨\",\"user\":\"sh-wangcc1\",\"uuid\":\""+uuid+"\",\"token\":\"23909b772e71c9fcf097e021de3c1f0b249d44fc65968eb036bf40cd8f6d69bf\"}";
//        String encryptStr = "{\"orgName\":\"上海市分公司数字化部\",\"phone\":\"18601725623\",\"orgCode\":353959,\"name\":\"姚琳燕\",\"user\":\"yaoly15\",\"uuid\":\"1-122tyyt34jhjh-11=\",\"token\":\"23909b772e71c9fcf097e021de3c1f0b249d44fc65968eb036bf40cd8f6d69bf\"}";
//        String encryptStr = "{\"orgName\":\"上海市分公司数字化部\",\"phone\":\"18601727257\",\"orgCode\":353959,\"name\":\"史玮强\",\"user\":\"shiwq23\",\"uuid\":\"890dioo-=i356\",\"token\":\"23909b772e71c9fcf097e021de3c1f0b249d44fc65968eb036bf40cd8f6d69bf\"}";
//        String encryptStr = "{\"orgName\":\"上海市分公司数字化部\",\"phone\":\"18601727257\",\"orgCode\":353959,\"name\":\"谈佳俊\",\"user\":\"tanjj11\",\"uuid\":\""+uuid+"\",\"token\":\"23909b772e71c9fcf097e021de3c1f0b249d44fc65968eb036bf40cd8f6d69bf\",\"empCode\":\"0923349\"}";
//        String encryptStr = "{\"orgName\":\"上海市分公司数字化部\",\"phone\":\"18601727257\",\"orgCode\":353959,\"name\":\"测试\",\"user\":\"shentou\",\"uuid\":\"cf21879i356\",\"token\":\"23909b772e71c9fcf097e021de3c1f0b249d44fc65968eb036bf40cd8f6d69bf\",\"empCode\":\"0923349\"}";
//        String encryptStr = "{\"orgName\":\"上海市分公司数字化部\",\"phone\":\"17521213573\",\"orgCode\":353959,\"name\":\"谢溪溪\",\"user\":\"sh-xiexx\",\"uuid\":\"1-jkashk-11=\",\"token\":\"23909b772e71c9fcf097e021de3c1f0b249d44fc65968eb036bf40cd8f6d69bf\"}";
//           String encryptStr = "{\"orgName\":\"上海市分公司数字化部\",\"phone\":\"13167275325\",\"orgCode\":353959,\"name\":\"涂波\",\"user\":\"sh-tub\",\"uuid\":\""+uuid+"\",\"token\":\"23909b772e71c9fcf097e021de3c1f0b249d44fc65968eb036bf40cd8f6d69bf\",\"empCode\":\"0923349\"}";
//           String encryptStr = "{\"orgName\":\"上海市分公司数字化部\",\"phone\":\"13167275325\",\"orgCode\":353959,\"name\":\"苏奇\",\"user\":\"xiaoxiaosu\",\"uuid\":\"re--9081311=\",\"token\":\"23909b772e71c9fcf097e021de3c1f0b249d44fc65968eb036bf40cd8f6d69bf\"}";

//        String encryptStr="04B4D74CF65DB2C719600FCD789BB17E4E4EDC7517537F348CED9523CE80BF6000933C7630A6D0EFDCCC81A75DF9C45922B602FD5D7AF4E1B4F6A4897CF5F9E22573394311E2CD135B1E10F66B91382E258D4EBD24BB1ACF65C84ACAD3A95739D9FA11D1609FEC6E1239F258C33397A654CC083D528F0022F7D5EC2894C05F0E6603C75FCB9DC2EF542E9F400880AFDA1D3D046DE8062BE449A2A90CA711D56DC1D4E7E82A641E6083D4D93A8A684BDA994E7564684A3A06FD3DAFC1EBC1A60F9661F5A5C541C6132425642677604340216DAC784C33B852509A103FD81DA8A412FC1D55B0EB34997CE9742D00";
        System.out.println("原文：" + encryptStr);
        String encryptStr1 = encryptByPublicKey(encryptStr, publicKeyStr);
        System.out.println("公钥加密后的内容：https://10.125.184.174:6600/agent-app/sso?touchId=171818101288700&params=" + encryptStr1);
//        String decryptStr = decryptByPrivateKey(encryptStr, privateKeyStr);
//        System.out.println("私钥解密后的内容：" + decryptStr);




    }



}
