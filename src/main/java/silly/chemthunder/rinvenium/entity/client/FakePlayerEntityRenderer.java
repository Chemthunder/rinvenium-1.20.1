package silly.chemthunder.rinvenium.entity.client;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.util.Identifier;
import silly.chemthunder.rinvenium.Rinvenium;

public class FakePlayerEntityRenderer extends PlayerEntityRenderer {
    public static final Identifier TEXTURE = Rinvenium.id("textures/entity/fakeplayer/orchidpuppy.png");

    public FakePlayerEntityRenderer(EntityRendererFactory.Context ctx, boolean slim) {
        super(ctx, slim);
    }

    @Override
    public Identifier getTexture(AbstractClientPlayerEntity abstractClientPlayerEntity) {
        return TEXTURE;
    }
}
