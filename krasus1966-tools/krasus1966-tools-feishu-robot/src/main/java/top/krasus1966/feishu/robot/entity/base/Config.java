package top.krasus1966.feishu.robot.entity.base;

/**
 * @author Krasus1966
 * @date 2021/10/9 22:08
 **/
public class Config {
    private boolean wideScreenMode;

    private boolean enableForward;

    public boolean isWideScreenMode() {
        return wideScreenMode;
    }

    public Config setWideScreenMode(boolean wideScreenMode) {
        this.wideScreenMode = wideScreenMode;
        return this;
    }

    public boolean isEnableForward() {
        return enableForward;
    }

    public Config setEnableForward(boolean enableForward) {
        this.enableForward = enableForward;
        return this;
    }
}
