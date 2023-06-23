package top.krasus1966.core.web.auth.facade;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.UUID;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.krasus1966.core.cache.redis_util.CacheUtil;
import top.krasus1966.core.web.auth.entity.Captcha;
import top.krasus1966.core.web.constant.LoginConstants;
import top.krasus1966.core.web.entity.R;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Krasus1966
 * @date 2023/5/3 15:49
 **/
@RestController
@RequestMapping(value = "/captcha")
@Slf4j
public class CaptchaFacade {

    private final Producer producer;

    public CaptchaFacade(Producer producer) {
        this.producer = producer;
    }

    /**
     * 获取图片验证码
     *
     * @return top.krasus1966.core.web.entity.R<top.krasus1966.core.web.auth.entity.Captcha>
     * @throws IOException
     * @method captcha
     * @author krasus1966
     * @date 2023/5/3 15:57
     * @description 获取图片验证码
     */
    @RequestMapping(value = "/get")
    public R<Captcha> captcha() throws IOException {
        String code = producer.createText();
        String key = UUID.randomUUID().toString();

        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        String str = "data:image/jpeg;base64,";
        String base64Img = str + Base64.encode(outputStream.toByteArray());

        // 存储到redis中，超时时间120秒
        CacheUtil.hset(LoginConstants.CAPTCHA_KEY, key, code, 120L);
        log.info("验证码 -- {} - {}", key, code);
        return R.success(new Captcha(key, base64Img));
    }
}
