package top.krasus1966.core.constant;

/**
 * @author Krasus1966
 * @date 2022/11/17 11:05
 **/
public interface SecurityConst {
    /**
     * 系统内RSA 私钥
     */
    String RSA_PRIVATE_KEY =
            "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIBCXuIVoytSFlNhA2bwBb9iZJmJ3ekh8KcODZBOTcIzD7KBVNvCWwgI/34gzeVl4Ab/sFM+J57ElepZ/N837xD8H7fquxmD6EpG5gqH7X8VTXenj2dY+LhO/WyeCOuqNu9bifvqSpO+SP2D3YOTuetb+wA5eb3ikjYmxZoyHuj3AgMBAAECgYBRt3eyfeyu3icBSo8bV5Oh4zOLK26qaYhlNyvnJCygrV2z8ni5ZzQOg4UcvBPSszyv2NpjvvcJTeDiJiFZqs5yUaW9Gi1H2EKkrEQL3gqvQhNvMq+u20ju1YzFOaPYivPYX/f/iUDTqlfYPJzUVbOL9WnNhdrzoLPSV7zAlJcxAQJBAL8m9OBZodmX9zOFbDrIj/ek27CYJunVDiwkA8sC5hWbKNCSicJ+sOQCrYvVtpqKnAbzy+lZXZ/eGMdqRmtQqXcCQQCrxVYpHPQPXKkWjIJnnVOiljbs0EktWUxBWOewzMHSaBhntgZoXaz9oQd90h/DX6xezv1VDxuv+tRWKN+Ps5yBAkA564HzoPUry6I52EnNHgyRi0COJ+xSmh4rPdZwYzZ4gYVCfcSETIMo867GWkF0Xl+cs66rzEpUMKXtdsRTrCNNAkEAgOeg102xIm4pPpDHtDZu807DfUh6AhxHi6rKhPXEZsBTwFDr0hqqndPgqV2tFJOR6bxwhQrHfnNAMkCONWtXAQJAK3q8in4WJqM9c1qfLeuLavFa3wx5aHIN03beXqqqR8Shh6DKYs4QAfU63nTvyqoPO9Xtz4BGUEZvwCeCPdCJJQ==";

    /**
     * 系统内公钥
     */
    String RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAQl7iFaMrUhZTYQNm8AW" +
            "/YmSZid3pIfCnDg2QTk3CMw+ygVTbwlsICP9+IM3lZeAG/7BTPieexJXqWfzfN+8Q/B+36rsZg+hKRuYKh+1" +
            "/FU13p49nWPi4Tv1sngjrqjbvW4n76kqTvkj9g92Dk7nrW/sAOXm94pI2JsWaMh7o9wIDAQAB";

    /**
     * 系统内AES KEY，用于加密并保存到数据库
     */
    String AES_KEY = "815dfbf26d324fa29705c05de7006267";
    /**
     * 系统内AES IV，用于加密并保存到数据库
     */
    String AES_IV = "28c31f467cac4e1b94ceb59da69dc3de";

    /**
     * 系统内SM2 私钥
     */
    String SM2_PRIVATE_KEY =
            "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgYqy5IORDz75QUB207cBCuD" +
                    "/sJ0PZ3Yfz8xPyx+JwIWGgCgYIKoEcz1UBgi2hRANCAAS2jLgKUGz0iAe" +
                    "+pTrWD19arluq8P40teW0nHWQNa6ft4QEYRR5h8ao/KHeStj3HSgNHgNBIJHkUpakGIu2O/EN";

    /**
     * 系统内SM2 公钥
     */
    String SM2_PUBLIC_KEY = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEtoy4ClBs9IgHvqU61g9fWq5bqvD" +
            "+NLXltJx1kDWun7eEBGEUeYfGqPyh3krY9x0oDR4DQSCR5FKWpBiLtjvxDQ==";
    /**
     * 系统内SM2 公钥 压缩给前端使用
     */
    String SM2_PUBLIC_KEY_Q =
            "04b68cb80a506cf48807bea53ad60f5f5aae5baaf0fe34b5e5b49c759035ae9fb7840461147987c6a8fca1de4ad8f71d280d1e03412091e45296a4188bb63bf10d";

    /**
     * 系统内SM4 KEY
     */
    String SM4_KEY = "d9bb02944e6940c5a20b659114af5ba0";

    /**
     * 系统内SM4 IV
     */
    String SM4_IV = "602a5410cc5447029f93f9815386801e";
}
