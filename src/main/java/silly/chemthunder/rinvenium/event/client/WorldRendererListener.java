package silly.chemthunder.rinvenium.event.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import silly.chemthunder.rinvenium.render.SlashRender;
import silly.chemthunder.rinvenium.render.manager.global.SlashRendererManager;

public class WorldRendererListener {
    public static void execute() {
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientWorld world = client.world;
            Camera camera = context.camera();

            if (world != null) {
                SlashRendererManager.tick();
                SlashRendererManager.get().forEach(slashRender -> renderSlashes(context, client, world, camera, slashRender));
            }
        });
    }

    private static void renderSlashes(WorldRenderContext context, MinecraftClient client, ClientWorld world, Camera camera, SlashRender slash) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        double viewDistance = client.options.getClampedViewDistance() * 16;

        double camX = camera.getPos().getX();
        double camY = camera.getPos().getY();
        double camZ = camera.getPos().getZ();

        float ageDelta = (float) slash.age / slash.maxAge;
        float delta = (float) (0.3 * Math.log(ageDelta + 0.011) + (Math.exp(1) / 2));

        MatrixStack matrices = context.matrixStack();
        matrices.push();

        matrices.translate(-camX, -camY, -camZ);
        matrices.translate(slash.origin.x, slash.origin.y, slash.origin.z);

        slash.TRANSFORMATION.forEach(matrices::multiply);

        Matrix4f transformation = matrices.peek().getPositionMatrix();

        matrices.translate(-slash.origin.x, -slash.origin.y, -slash.origin.z);
        matrices.translate(camX, camY, camZ);

        if (slash.origin.squaredDistanceTo(camera.getPos()) < viewDistance * viewDistance) {
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();

            if (SlashRendererManager.slashBuffer != null) {
                SlashRendererManager.slashBuffer.close();
            }
            SlashRendererManager.slashBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);

            bufferbuilder.begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);

            double endY = MathHelper.lerp(delta, slash.origin.y, slash.origin.add(slash.direction.normalize()).y);

            bufferbuilder.vertex(transformation, (float) (slash.origin.x - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(1.0f, 0.0f, 0.0f, 0.9f);
            bufferbuilder.vertex(transformation, (float) (slash.origin.x - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(1.0f, 0.0f, 0.0f, 0.9f);
            bufferbuilder.vertex(transformation, (float) (slash.origin.x - camX), (float) (endY - camY), (float) (slash.origin.z - 1 - camZ)).color(1.0f, 0.0f, 0.0f, 0.9f);

            matrices.pop();

            BufferBuilder.BuiltBuffer builtBuffer = bufferbuilder.end();
            SlashRendererManager.slashBuffer.bind();
            SlashRendererManager.slashBuffer.upload(builtBuffer);
            VertexBuffer.unbind();

            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            if (SlashRendererManager.slashBuffer != null) {
                SlashRendererManager.slashBuffer.bind();
                ShaderProgram shaderProgram = RenderSystem.getShader();
                Matrix4f positionMatrix = RenderSystem.getModelViewStack().peek().getPositionMatrix();

                SlashRendererManager.slashBuffer.draw(positionMatrix, RenderSystem.getProjectionMatrix(), shaderProgram);
            }
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }
    }
}
