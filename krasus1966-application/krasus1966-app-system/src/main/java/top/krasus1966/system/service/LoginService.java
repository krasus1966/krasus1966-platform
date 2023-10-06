package top.krasus1966.system.service;

import cn.hutool.core.text.CharSequenceUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import top.krasus1966.core.base.constant.Constants;
import top.krasus1966.core.base.constant.LoginConstants;
import top.krasus1966.core.base.exception.BizException;
import top.krasus1966.core.base.util.TreeUtil;
import top.krasus1966.core.cache.constant.DictConst;
import top.krasus1966.core.cache.redis_util.CacheUtil;
import top.krasus1966.core.crypto.util.AESUtil;
import top.krasus1966.core.crypto.util.RSAUtil;
import top.krasus1966.core.json.util.JsonUtils;
import top.krasus1966.core.web.auth.entity.LoginDTO;
import top.krasus1966.core.web.auth.entity.LoginVO;
import top.krasus1966.core.web.auth.entity.UserLoginInfo;
import top.krasus1966.core.web.constant.LoginConst;
import top.krasus1966.core.web.exception.NoLoginException;
import top.krasus1966.core.web.exception.NotFoundException;
import top.krasus1966.core.web.exception.WrongFieldThroble;
import top.krasus1966.core.web.util.login.LoginUtils;
import top.krasus1966.core.web.util.servlet.IPUtils;
import top.krasus1966.core.web.util.servlet.ServletUtils;
import top.krasus1966.system.domain.menu.SysMenu;
import top.krasus1966.system.domain.menu.SysRole;
import top.krasus1966.system.domain.sys_user.SysUser;
import top.krasus1966.system.domain.sys_user.SysUserRole;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Krasus1966
 * @date 2022/11/17 11:27
 **/
@Service
public class LoginService {

    private final SysUserService sysUserService;
    private final SysUserRoleService sysUserRoleService;
    private final SysMenuService sysMenuService;
    private final LoginConstants loginConstants;

    public LoginService(SysUserService sysUserService, SysMenuService sysMenuService,
                        SysUserRoleService sysUserRoleService,
                        LoginConstants loginConstants) {
        this.sysUserService = sysUserService;
        this.sysMenuService = sysMenuService;
        this.sysUserRoleService = sysUserRoleService;
        this.loginConstants = loginConstants;
    }

    /**
     * 登录信息校验
     *
     * @param param 加密数据信息
     * @return top.krasus1966.base.domain.SysUser
     * @method checkLoginParam
     * @author krasus1966
     * @date 2022/1/8 22:01
     * @description 登录信息校验
     */
    public LoginDTO checkLoginParam(String param) throws JsonProcessingException {
        if (CharSequenceUtil.isBlank(param)) {
            throw new BizException("登录参数为空");
        }
        String decodeParam = RSAUtil.decrypt(param);
        if (CharSequenceUtil.isBlank(decodeParam)) {
            throw new BizException("解密失败");
        }
        Map<String, String> jsonMap = JsonUtils.jsonToMap(decodeParam, String.class, String.class);
        if (null == jsonMap) {
            throw new BizException("参数解析失败");
        }
        String account = jsonMap.get("account");
        String password = jsonMap.get("password");
        String keys = jsonMap.get("keys");
        String loginType = jsonMap.get("loginType");
        if (CharSequenceUtil.isBlank(account)) {
            throw new BizException("用户名不能为空");
        }
        if (CharSequenceUtil.isBlank(password)) {
            throw new BizException("密码不能为空");
        }
        if (CharSequenceUtil.isBlank(keys)) {
            throw new BizException("keys不能为空");
        }
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setAccount(account);
        loginDTO.setPassword(password);
        loginDTO.setKeys(keys);
        loginDTO.setLoginType(loginType);
        return loginDTO;
    }

    /**
     * 登录并返回用户登录信息
     *
     * @param obj 用户对象，包含用户名/手机号码/邮箱中的一个和原始的密码
     * @return top.krasus1966.base.entity.UserLoginInfo
     * @method login
     * @author krasus1966
     * @date 2022/1/9 21:29
     * @description 用户登录
     */
    public LoginVO login(LoginDTO obj) throws WrongFieldThroble {
        // 此处执行用户（用户名或手机号码或邮箱）
        SysUser sysUser = getLoginUser(obj);
        if (null == sysUser) {
            throw new BizException("用户不存在");
        }
        if (!sysUser.checkPassword(obj.getPassword())) {
            // 记录错误次数到缓存
            int failNum = 0;
            String failNumStr = CacheUtil.get(LoginConst.FAIL_USER_ID + sysUser.getId());
            if (CharSequenceUtil.isBlank(failNumStr)) {
                failNumStr = "0";
            }
            failNum = Integer.parseInt(failNumStr);
            if (failNum >= loginConstants.getFailLockNum()) {
                CacheUtil.set(LoginConst.LOCK_USER_ID + sysUser.getId(), "LOCK_ON",
                        loginConstants.getLockLoginExpire());
                throw new BizException("密码输入次数超过限制，已经锁定，请5分钟后再试");
            }
            CacheUtil.set(LoginConst.FAIL_USER_ID + sysUser.getId(),
                    String.valueOf(failNum + 1), loginConstants.getLockLoginExpire());
            throw new BizException("用户名或密码错误，" + (loginConstants.getFailLockNum() - failNum) + "次后将锁定账号");
        }
        if (CacheUtil.keyIsExist(LoginConst.LOCK_USER_ID + sysUser.getId())) {
            throw new BizException("该账户已经锁定，请5分钟后再试");
        }
        if (DictConst.STATUS_TYPE.STATUS_TYPE_OFF.equals(sysUser.getStatus())) {
            throw new BizException("该账户已经禁用");
        }
        String token = UUID.randomUUID().toString();
        // 前端对称密钥key
        String[] webKey = obj.getKeys().split(",");
        // 生成用户缓存对称密钥key
        String[] aesKeys = AESUtil.generateKey();
        String cacheKey = aesKeys[0] + "," + aesKeys[1];
        // 登录成功操作
        loginSuccessAction(token, cacheKey, sysUser);
        // 修改最后登录时间
        return new LoginVO(token, AESUtil.encrypt(cacheKey, webKey[0], webKey[1]));
    }

    /**
     * 不同方式登录获取用户信息
     *
     * @param obj
     * @return top.krasus1966.system.domain.sys_user.SysUser
     * @throws
     * @method getLoginUser
     * @author krasus1966
     * @date 2023/10/6 16:53
     * @description 不同方式登录获取用户信息
     */
    private SysUser getLoginUser(LoginDTO obj) {
        return switch (obj.getLoginType()) {
            case LoginDTO.MOBILE -> sysUserService.lambdaQuery()
                    .eq(SysUser::getMobile,
                            obj.getAccount())
                    .last("LIMIT 1")
                    .one();
            case LoginDTO.EMAIL -> sysUserService.lambdaQuery()
                    .eq(SysUser::getEmail,
                            obj.getAccount())
                    .last("LIMIT 1")
                    .one();
            default -> sysUserService.lambdaQuery()
                    .eq(SysUser::getUserName,
                            obj.getAccount())
                    .last("LIMIT 1")
                    .one();
        };
    }

    /**
     * 获取用户信息
     *
     * @param
     * @return top.krasus1966.system.domain.sys_user.SysUser
     * @throws
     * @method profile
     * @author krasus1966
     * @date 2023/10/6 15:14
     * @description 获取用户信息
     */
    public SysUser profile() {
        String userId = LoginUtils.getUserLoginId();
        if (CharSequenceUtil.isBlank(userId)) {
            throw new NoLoginException("未登录");
        }
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getId, userId).one();
        if (sysUser == null) {
            throw new NotFoundException("用户不存在");
        }
        sysUser.setPassword(null);
        UserLoginInfo info = LoginUtils.getUserLoginInfo();
        if (info == null) {
            throw new NoLoginException("当前用户登录信息不存在，请重新登录");
        }
        // 获取菜单列表
        List<SysMenu> menuList = sysMenuService.queryMenuByUserId(userId);
        if (!menuList.isEmpty()) {
            List<SysMenu> menus =
                    menuList.stream().filter(sysMenu -> DictConst.MENU_TYPE.MENU_TYPE_PAGE.equals(sysMenu.getMenuType())).collect(Collectors.toList());
            sysUser.setMenus(TreeUtil.parseTreeWithLevel(menus, Constants.Entity.ROOT,
                    SysMenu::getId, SysMenu::getParentId, SysMenu::setChildren, SysMenu::setLevel,
                    Comparator.comparing(SysMenu::getSort)));
            sysUser.setButtons(menuList.stream().filter(sysMenu -> DictConst.MENU_TYPE.MENU_TYPE_BUTTON.equals(sysMenu.getMenuType())).map(SysMenu::getMenuCode).collect(Collectors.joining(";")));
        }

        if (CharSequenceUtil.isNotBlank(sysUser.getMobile())) {
            sysUser.setMobile(AESUtil.aesGetToNewKey(sysUser.getMobile()));
        }
        if (CharSequenceUtil.isNotBlank(sysUser.getEmail())) {
            sysUser.setEmail(AESUtil.aesGetToNewKey(sysUser.getEmail()));
        }
        if (CharSequenceUtil.isNotBlank(sysUser.getIdCard())) {
            sysUser.setIdCard(AESUtil.aesGetToNewKey(sysUser.getIdCard()));
        }
        return sysUser;
    }

    /**
     * @method loginSuccessAction
     * @author krasus1966
     * @date 2022/11/17 11:35
     * @description 登录成功操作
     */
    private void loginSuccessAction(String token, String newKey, SysUser sysUser) throws WrongFieldThroble {

        // 成功后清除失败次数
        CacheUtil.del(LoginConst.FAIL_USER_ID + sysUser.getId());

        String lastLoginRoleId = SysRole.DEFAULT_ROLE_IDS.EMPLOYEE;
        // 查询最后一次登录角色是否本用户依旧拥有，有则直接登录此角色，没有则进入默认的员工角色
        if (CharSequenceUtil.isNotBlank(sysUser.getLastLoginRoleId())) {
            long count = sysUserRoleService.lambdaQuery().eq(SysUserRole::getUserId,
                    sysUser.getId()).eq(SysUserRole::getRoleId, sysUser.getLastLoginRoleId()).count();
            if (count > 0) {
                lastLoginRoleId = sysUser.getLastLoginRoleId();
            }
        }

        List<SysMenu> menuList = sysMenuService.queryMenuByUserIdAndRoleId(sysUser.getId(),
                lastLoginRoleId);
        //存放前端菜单权限
        StringBuilder userMenus = new StringBuilder();
        //存放前端菜单权限
        StringBuilder userButtons = new StringBuilder();
        // 获取菜单
        for (SysMenu sysMenu : menuList) {
            if (DictConst.MENU_TYPE.MENU_TYPE_PAGE.equals(sysMenu.getMenuType())) {
                // 获取菜单
                if (!userMenus.isEmpty()) {
                    userMenus.append(";");
                }
                userMenus.append(CharSequenceUtil.trim(sysMenu.getMenuCode()));
            } else {
                // 获取按钮
                if (!userButtons.isEmpty()) {
                    userButtons.append(";");
                }
                userButtons.append(CharSequenceUtil.trim(sysMenu.getMenuCode()));
            }
        }

        // 缓存用户信息
        UserLoginInfo userLoginInfo = new UserLoginInfo()
                .setId(sysUser.getId())
                .setToken(token)
                .setUserName(sysUser.getUserName())
                .setRealName(sysUser.getRealName())
                .setLoginTime(System.currentTimeMillis())
                .setLoginIp(IPUtils.getRequestIp(ServletUtils.getRequest()))
                .addOneInfo(LoginConst.INFO_USER_MENUS, ";" + userMenus + ";")
                .addOneInfo(LoginConst.INFO_USER_BUTTONS, ";" + userButtons + ";")
                .addOneInfo(LoginConst.INFO_USER_AES_KEY, newKey);
        if (CharSequenceUtil.isNotBlank(sysUser.getTenantId())) {
            userLoginInfo.setTenantId(sysUser.getTenantId());
        }

        // 移除旧token关联的数据
        String oldToken =
                CacheUtil.get(LoginConst.USER_TOKEN + userLoginInfo.getTenantId() + ":" + sysUser.getId());
        if (CharSequenceUtil.isNotBlank(oldToken)) {
            CacheUtil.del(LoginConst.USER_INFO + oldToken);
        }

        // 加入到缓存中
        CacheUtil.hset(LoginConst.USER_INFO + token, userLoginInfo.toMap(),
                loginConstants.getExpireTimeLogin());
        CacheUtil.set(LoginConst.USER_TOKEN + userLoginInfo.getTenantId() + ":" + sysUser.getId(), token,
                loginConstants.getExpireTimeLogin());
    }
}
