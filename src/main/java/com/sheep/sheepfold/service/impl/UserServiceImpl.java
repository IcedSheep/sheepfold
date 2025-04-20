package com.sheep.sheepfold.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sheep.sheepfold.common.ErrorCode;
import com.sheep.sheepfold.exception.BusinessException;
import com.sheep.sheepfold.manager.UserAccountGenerator;
import com.sheep.sheepfold.mapper.UserMapper;
import com.sheep.sheepfold.model.User;
import com.sheep.sheepfold.model.dto.UserLoginVO;
import com.sheep.sheepfold.model.dto.UserRegisterDTO;
import com.sheep.sheepfold.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    private static final String SALT = "sheep";

    private static final String USER_LOGIN_STATE = "user_login";


    @Autowired
    private JavaMailSender javaMailSender;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public static final String EMAIL_CODE_PREFIX = "email:code:";

    public String EMAIL_REGISTER_LOCK = "email:register:lock:";

    @Autowired
    private RedissonClient redissonClient;



    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public String getCode(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        if (!validator.isValid(email)) {
            log.error("错误邮箱：{}",email);
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"错误邮箱");
        }
        return sendMessage(email);
    }

    public String sendMessage(String to){
        // 创建一个邮件消息
        MimeMessage message = javaMailSender.createMimeMessage();
        // 创建 MimeMessageHelper
        MimeMessageHelper helper = null;
        String code = generateVerificationCode();
        try {
            helper = new MimeMessageHelper(message, false);
            // 发件人邮箱和名称
            helper.setFrom(sender);
            // 收件人邮箱
            helper.setTo(to);
            log.info("发送的邮箱：{} 验证码：{}",to,code);
            // 邮件标题
            helper.setSubject("验证码");
            // 邮件内容，使用HTML格式
            String emailContent = "<html><body>"
                    + "<h2>亲爱的用户，恭喜你成功注册</h2>"
                    + "<p>为了确保账户安全，请输入以下验证码完成验证：</p>"
                    + "<h3 style='color: #FF6347;'>验证码：<strong>" + code + "</strong></h3>"
                    + "<p>该验证码将在 60 秒内有效，请尽快完成验证。</p>"
                    + "<p>如果您没有发起此请求，请忽略此邮件。</p>"
                    + "<br>"
                    + "</body></html>";
            // 邮件正文，第二个参数表示是否是HTML正文
            helper.setText(emailContent, true);
            // code放入 Redis 60s
            stringRedisTemplate.opsForValue().set(EMAIL_CODE_PREFIX + to,code,60, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("发送邮箱失败");
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"发送邮箱失败");
        }
        // 发送
        javaMailSender.send(message);
        return code;
    }

    /**
     * 随机6位数字
     * @return
     */
    public  String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10)); // 每位添加0-9的随机数
        }
        return code.toString();
    }


    @Override
    public Long userRegister(UserRegisterDTO param) {
        String userEmail = param.getUserEmail();
        String code = param.getCode();
        String userPassword = param.getUserPassword();
        String checkPassword = param.getCheckPassword();
        if (StringUtils.isAnyBlank(userEmail,code,userPassword,checkPassword)) {
            log.error("请求参数不能为空,{}", JSONUtil.toJsonStr(param));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        EmailValidator validator = EmailValidator.getInstance();
        if (!validator.isValid(userEmail)) {
            log.error("错误邮箱：{}",userEmail);
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"错误邮箱");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不正确");
        }
        // 获取分布式锁，防止并发注册
        String lockKey = EMAIL_REGISTER_LOCK + userEmail;
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);
        if (Boolean.FALSE.equals(flag)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该邮箱正在注册中，请稍后重试");
        }
        try {
            //  校验邮箱是否已注册
            LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.lambdaQuery(User.class)
                    .eq(User::getUserEmail, userEmail);
            long count = this.baseMapper.selectCount(lambdaQueryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "该邮箱已注册");
            }
            // 校验验证码
            String correctCode = stringRedisTemplate.opsForValue().get(EMAIL_CODE_PREFIX+ userEmail);
            if (correctCode == null || !correctCode.equals(code)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误或已过期");
            }

            // 验证码比对成功后删除 Redis 中的验证码，防止重复使用
            stringRedisTemplate.delete(EMAIL_CODE_PREFIX + userEmail);

            //  加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            // 插入数据
            Long userAccount = UserAccountGenerator.generateUserAccount();
            User user = new User();
            user.setUserEmail(userEmail);
            user.setUserAccount(userAccount.toString());
            user.setUserPassword(encryptPassword);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        } finally {
            // 释放 Redis 锁
            stringRedisTemplate.delete(lockKey);
        }
    }

    @Override
    public UserLoginVO userLogin(String email, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(email, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        EmailValidator validator = EmailValidator.getInstance();
        if (!validator.isValid(email)) {
            log.error("错误邮箱：{}",email);
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"错误邮箱");
        }
        if (userPassword.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getUserEmail, email)
                .eq(User::getUserPassword, encryptPassword);
        User user = this.baseMapper.selectOne(lambdaQueryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 3. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    public UserLoginVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserLoginVO loginUserVO = new UserLoginVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }


}




