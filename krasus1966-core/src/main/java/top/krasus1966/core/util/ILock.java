package top.krasus1966.core.util;

/**
 * 锁相关
 *
 * @author Krasus1966
 * @date 2022/10/28 14:30
 **/
public interface ILock {

    /**
     * 加锁
     *
     * @param timeoutSec 锁定时间
     * @return boolean
     * @method tryLock
     * @author krasus1966
     * @date 2022/10/28 14:37
     * @description 加锁
     */
    boolean tryLock(long timeoutSec);

    /**
     * 解锁
     *
     * @method unlock
     * @author krasus1966
     * @date 2022/10/28 14:37
     * @description 解锁
     */
    void unlock();
}
