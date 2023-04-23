package top.krasus1966.core.util.redis_util;

import cn.hutool.core.lang.UUID;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import top.krasus1966.core.util.ILock;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author Krasus1966
 * @date 2022/10/28 14:31
 **/
public class SimpleRedisLock implements ILock {

    /**
     * 锁的key前缀
     */
    private static final String KEY_PREFIX = "lock:simpleRedisLock:";
    private static final String ID_PREFIX = UUID.randomUUID().toString(true) + "-";

    /**
     * lua脚本
     */
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;

    static {
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("/lua_script/unlock.lua"));
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    /**
     * 锁的key
     */
    private final String name;
    /**
     * redis操作
     */
    private final StringRedisTemplate stringRedisTemplate;


    public SimpleRedisLock(String name, StringRedisTemplate stringRedisTemplate) {
        this.name = name;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean tryLock(long timeoutSec) {
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        Boolean isSuccess = stringRedisTemplate.opsForValue().setIfAbsent(KEY_PREFIX + name,
                threadId, timeoutSec, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(isSuccess);
    }

//    @Override
//    public void unlock() {
//        String threadId = ID_PREFIX + Thread.currentThread().getId();
//        String value = stringRedisTemplate.opsForValue().get(threadId);
//        // 存储的value（即UUID-线程id相同）才释放锁
//        if (threadId.equals(value)) {
//            stringRedisTemplate.delete(KEY_PREFIX + name);
//        }
//    }

    @Override
    public void unlock() {
        stringRedisTemplate.execute(UNLOCK_SCRIPT, Collections.singletonList(KEY_PREFIX + name),
                ID_PREFIX + Thread.currentThread().getId());
    }
}
