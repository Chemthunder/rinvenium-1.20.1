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
        matrices.scale(slash.getSize(), slash.getSize(), slash.getSize());

        /*matrices.scale(2.0f, 2.0f, 2.0f);*/

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

            bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

            double xOffset = 0.0;

            double endY = MathHelper.lerp(delta, slash.origin.y, slash.origin.add(slash.direction.normalize()).y);
            double endYMid = MathHelper.lerp(delta, slash.origin.y, slash.origin.add(slash.direction.normalize().multiply(0.5f)).y);
            double endZNeg = MathHelper.lerp(delta, slash.origin.z, slash.origin.add(slash.direction.add(slash.direction.normalize().rotateX((float) (Math.PI / 2))).normalize()).z);
            double endZPos = MathHelper.lerp(delta, slash.origin.z, slash.origin.add(slash.direction.add(slash.direction.normalize().rotateX((float) -(Math.PI / 2))).normalize()).z);

            double bgEndY = 0;


            // Background translucent layer
           /* bufferbuilder.vertex(transformation, (float) (slash.origin.x - camX), (float) (slash.origin.y - (slash.getSize() / 4) - camY), (float) (slash.origin.z - camZ)).color(1.0f, 0.0f, 0.0f, 0.01f).next();
            bufferbuilder.vertex(transformation, (float) (slash.origin.x - camX), (float) (endYMid - (slash.getSize() / 4) - camY), (float) (endZNeg - camZ)).color(1.0f, 0.0f, 0.0f, 0.01f).next();
            bufferbuilder.vertex(transformation, (float) (slash.origin.x - camX), (float) (endY - (slash.getSize() / 4) - camY), (float) (slash.origin.z - camZ)).color(1.0f, 0.0f, 0.0f, 0.01f).next();
            bufferbuilder.vertex(transformation, (float) (slash.origin.x - camX), (float) (endYMid - (slash.getSize() / 4) - camY), (float) (slash.origin.z - camZ)).color(1.0f, 0.9f, 0.9f, 0.6f).next();
            xOffset += 4.9E-5;*/

           /* matrices.translate(-camX, -camY, -camZ);
            matrices.translate(slash.origin.x, slash.origin.y, slash.origin.z);

            matrices.scale(0.5f, 0.5f, 0.5f);
            transformation = matrices.peek().getPositionMatrix();

            matrices.translate(-slash.origin.x, -slash.origin.y, -slash.origin.z);
            matrices.translate(camX, camY, camZ);*/

            // Main layer
            bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(1.0f, 0.0f, 0.0f, 0.9f).next();
            bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (endZNeg - camZ)).color(0.4f, 0.0f, 0.0f, 0.9f).next();
            bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(1.0f, 0.0f, 0.0f, 0.9f).next();
            bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(1.0f, 0.9f, 0.9f, 0.9f).next();

           /* bufferbuilder.vertex(transformation, (float) (slash.origin.x - xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(1.0f, 0.0f, 0.0f, 0.9f).next();
            bufferbuilder.vertex(transformation, (float) (slash.origin.x - xOffset - camX), (float) (endYMid - camY), (float) (endZNeg - camZ)).color(0.4f, 0.0f, 0.0f, 0.9f).next();
            bufferbuilder.vertex(transformation, (float) (slash.origin.x - xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(1.0f, 0.0f, 0.0f, 0.9f).next();
            bufferbuilder.vertex(transformation, (float) (slash.origin.x - xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(1.0f, 0.9f, 0.9f, 0.9f).next();
*/
            // Inner layer
            //bufferbuilder.vertex(transformation, (float) (slash.origin.x - camX), (float) (endY - camY), (float) (endZPos - camZ)).color(1.0f, 0.0f, 0.0f, 0.9f).next();

            matrices.pop();

            BufferBuilder.BuiltBuffer builtBuffer = bufferbuilder.end();
            SlashRendererManager.slashBuffer.bind();
            SlashRendererManager.slashBuffer.upload(builtBuffer);
            VertexBuffer.unbind();

            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
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
