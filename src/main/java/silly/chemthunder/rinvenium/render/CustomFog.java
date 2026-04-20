package silly.chemthunder.rinvenium.render;

public class CustomFog {
    public final float red;
    public final float green;
    public final float blue;
    public final int maxAge;
    public int age;

    public CustomFog(float red, float green, float blue, int maxAge) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.maxAge = maxAge;
        this.age = 0;
    }
}
