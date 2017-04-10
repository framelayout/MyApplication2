package com.bn.yfc.wxapi;

import com.bn.yfc.tools.Tools;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;


public class SignUtils {

    private static final String ALGORITHM = "RSA";

    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    private static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";

    private static final String DEFAULT_CHARSET = "UTF-8";

    private static String getAlgorithms(boolean rsa2) {
        return rsa2 ? SIGN_SHA256RSA_ALGORITHMS : SIGN_ALGORITHMS;
    }

    public static String sign(String content, String privateKey, boolean rsa2) {
        Tools.setLog("签名0");
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                    Base64.decode(privateKey));
            Tools.setLog("签名1");
            KeyFactory keyf = KeyFactory.getInstance(ALGORITHM, "BC");
            Tools.setLog("签名2");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            Tools.setLog("签名3");
            Signature signature = Signature
                    .getInstance("SHA1WithRSA"/*getAlgorithms(rsa2)*/);
            Tools.setLog("签名4");
           /* Tools.setLog("签名方式为" + Signature
                    .getInstance(getAlgorithms(rsa2)).toString());*/
            signature.initSign(priKey);
            Tools.setLog("签名5");
            signature.update(content.getBytes(DEFAULT_CHARSET));
            Tools.setLog("签名6");
            byte[] signed = signature.sign();
            Tools.setLog("签名7");
            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
            Tools.setLog("签名抛出错误" + e.getMessage());
        }

        return null;
    }


}
