package top.krasus1966.jwt;

import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;

/**
 * @author Krasus1966
 * @date 2021/9/27 21:53
 **/
public class SecretInfo {

    private SignatureAlgorithm signatureAlgorithm;

    private Key key;

    public SecretInfo(SignatureAlgorithm signatureAlgorithm, Key key) {
        this.signatureAlgorithm = signatureAlgorithm;
        this.key = key;
    }

    public SignatureAlgorithm getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(SignatureAlgorithm signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }
}
