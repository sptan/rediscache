package com.sptan.exec.rediscache.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * The type Signature util.
 *
 * @author liupeng
 * @date 2020 -03-19 17:24
 */
public final class SignatureUtil {

    /**
     * 签名
     *
     * @param params  the params
     * @param signKey the sign key
     * @return string
     */
    public static String sign256(Map<String, String> params, String signKey) {
        SortedMap<String, String> sortedMap = new TreeMap<>(params);

        StringBuilder toSign = new StringBuilder();
        for (String key : sortedMap.keySet()) {
            String value = params.get(key);
            if (StringUtils.isNotEmpty(value) && !"sign".equals(key) && !"key".equals(key)) {
                toSign.append(key).append("=").append(value).append("&");
            }
        }

        toSign.append("key=").append(signKey);
        return sha256Hmac(toSign.toString(), signKey);
    }


    private static String sha256Hmac(String message, String secret) {
        String hash = "";
        try {
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256Hmac.init(secret_key);
            byte[] bytes = sha256Hmac.doFinal(message.getBytes());
            hash = byteArrayToHexString(bytes);
            System.out.println(hash);
        } catch (Exception e) {
            System.out.println("Error HmacSHA256 ===========" + e.getMessage());
        }
        return hash;
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b!=null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }


    /**
     * 校验签名
     *
     * @param params     the params
     * @param privateKey the private key
     * @return boolean
     */
    public static Boolean verifySha256(Map<String, String> params, String privateKey) {
        String sign = sign256(params, privateKey);
        return sign.equals(params.get("sign"));
    }
}
