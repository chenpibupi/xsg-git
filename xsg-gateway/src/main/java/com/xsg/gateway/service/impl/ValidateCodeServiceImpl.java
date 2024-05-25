package com.xsg.gateway.service.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;
import com.google.code.kaptcha.Producer;
import com.xsg.common.core.constant.CacheConstants;
import com.xsg.common.core.constant.Constants;
import com.xsg.common.core.exception.CaptchaException;
import com.xsg.common.core.utils.StringUtils;
import com.xsg.common.core.utils.sign.Base64;
import com.xsg.common.core.utils.uuid.IdUtils;
import com.xsg.common.core.web.domain.AjaxResult;
import com.xsg.common.redis.service.RedisService;
import com.xsg.gateway.config.properties.CaptchaProperties;
import com.xsg.gateway.service.ValidateCodeService;

/**
 * 验证码实现处理
 *
 * @author Chenpi
 */
@Service
public class ValidateCodeServiceImpl implements ValidateCodeService
{
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CaptchaProperties captchaProperties;

    /**
     * 生成验证码
     *
     * @return AjaxResult 包含验证码信息的Ajax结果，如果验证码功能未启用，则仅返回一个表示验证码功能已禁用的AjaxResult。
     * @throws IOException 当写入验证码图像时发生IO错误。
     * @throws CaptchaException 当生成验证码过程中出现异常。
     */
    @Override
    public AjaxResult createCaptcha() throws IOException, CaptchaException
    {
        AjaxResult ajax = AjaxResult.success(); // 初始化成功结果
        boolean captchaEnabled = captchaProperties.getEnabled(); // 检查验证码是否启用
        ajax.put("captchaEnabled", captchaEnabled);
        if (!captchaEnabled)
        {
            return ajax; // 如果验证码未启用，直接返回
        }

        // 生成验证码前的准备工作
        String uuid = IdUtils.simpleUUID(); // 生成唯一标识符
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid; // 构造验证码在缓存中的键名

        String capStr = null, code = null;
        BufferedImage image = null;

        String captchaType = captchaProperties.getType(); // 获取验证码类型
        // 根据不同的验证码类型生成验证码
        if ("math".equals(captchaType))
        {
            // 生成算术型验证码
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        }
        else if ("char".equals(captchaType))
        {
            // 生成字符型验证码
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        // 将验证码代码存储到缓存中，设置过期时间
        redisService.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);


        // 将生成的验证码图像转换为字节流并进行Base64编码，以便于在前端展示
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try
        {
            ImageIO.write(image, "jpg", os); // 将图像写入输出流
        }
        catch (IOException e)
        {
            return AjaxResult.error(e.getMessage()); // 当写入图像失败时，返回错误信息
        }

        // 将验证码的标识符和编码后的图像数据添加到Ajax结果中返回
        ajax.put("uuid", uuid);
        ajax.put("img", Base64.encode(os.toByteArray()));
        return ajax;
    }


    /**
     * 校验验证码
     */
    @Override
    public void checkCaptcha(String code, String uuid) throws CaptchaException
    {
        if (StringUtils.isEmpty(code))
        {
            throw new CaptchaException("验证码不能为空");
        }
        if (StringUtils.isEmpty(uuid))
        {
            throw new CaptchaException("验证码已失效");
        }
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        String captcha = redisService.getCacheObject(verifyKey);
        redisService.deleteObject(verifyKey);

        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException("验证码错误");
        }
    }
}
