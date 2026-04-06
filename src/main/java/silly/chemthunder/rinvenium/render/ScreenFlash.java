package silly.chemthunder.rinvenium.render;

public class ScreenFlash {
    public final int maxAge;
    public final int color;
    public final int fadeTime;
    public final float maxOpacity;
    public int age;
    public int fadeTicks;

    public ScreenFlash(int maxAge, int color, int fadeTime, float maxOpacity) {
        this.maxAge = maxAge;
        this.color = color;
        this.fadeTime = fadeTime;
        this.maxOpacity = maxOpacity;
        this.age = 0;
        this.fadeTicks = 0;
    }

    public ScreenFlash(int maxAge, int color, int fadeTime) {
        this.maxAge = maxAge;
        this.color = color;
        this.fadeTime = fadeTime;
        this.maxOpacity = 1.0f;
        this.age = 0;
        this.fadeTicks = 0;
    }
}