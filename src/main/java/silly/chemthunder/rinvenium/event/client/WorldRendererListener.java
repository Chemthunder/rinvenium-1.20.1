package silly.chemthunder.rinvenium.event.client;

import com.mojang.blaze3d.platform.GlStateManager;
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
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import silly.chemthunder.rinvenium.render.SlashRender;
import silly.chemthunder.rinvenium.render.manager.global.SlashRendererManager;

public class WorldRendererListener {
    public static void execute() {
        WorldRenderEvents.LAST.register(context -> {
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
        float yDelta;
        float zDelta;
        float red;
        float nRed;
        // yDelta
        if (ageDelta <= 0.29f) {
            yDelta = (float) (9.2 * ageDelta * Math.exp(-3.4 * (ageDelta)));
        } else if (ageDelta > 0.29f && ageDelta <= 0.33f) {
            yDelta = 1f;
        } else {
            yDelta = 1f;
        }
        // zDelta
        if (ageDelta <= 0.19f) {
            zDelta = (float) (ageDelta * Math.exp(-Math.pow(3.6034, 2) * (Math.pow(ageDelta, 2) - 0.164)));
        } else if (ageDelta > 0.19f && ageDelta <= 0.35f) {
            zDelta = 1;
        } else {
            zDelta = (float) ((ageDelta - 0.1475) * Math.exp(-Math.pow(3.6034, 2) * (Math.pow(ageDelta - 0.1475, 2) - 0.164)));
        }
        // red
        red = (float) Math.min(-0.65 * Math.exp(-5.5 * ageDelta) + 1.05, 1.0);
        // green and blue
        nRed = (float) MathHelper.clamp(-0.8 * Math.exp(-10 * (ageDelta - 0.5)) + 0.8, 0.0, 1.0);

        Vec3d positionOffset = slash.direction.normalize().multiply(slash.size).multiply(0.5f);

        MatrixStack matrices = context.matrixStack();
        matrices.push();

        matrices.translate(-camX, -camY, -camZ);
        matrices.translate(slash.origin.add(slash.direction.normalize().multiply(0.5f)).x, slash.origin.add(slash.direction.normalize().multiply(0.5f)).y, slash.origin.add(slash.direction.normalize().multiply(0.5f)).z);

        slash.TRANSFORMATION.forEach(matrices::multiply);
        matrices.scale(slash.getSize(), slash.getSize(), slash.getSize());

        Matrix4f transformation = matrices.peek().getPositionMatrix();

        matrices.translate(-slash.origin.add(slash.direction.normalize().multiply(0.5f)).x, -slash.origin.add(slash.direction.normalize().multiply(0.5f)).y, -slash.origin.add(slash.direction.normalize().multiply(0.5f)).z);
        matrices.translate(camX, camY, camZ);

        if (slash.origin.squaredDistanceTo(camera.getPos()) < viewDistance * viewDistance) {
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
            RenderSystem.enableDepthTest();

            /*if (SlashRendererManager.slashBuffer != null) {
                SlashRendererManager.slashBuffer.close();
            }
            SlashRendererManager.slashBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);*/

            bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

            double xOffset = 0.0;

            double endY = MathHelper.lerp(yDelta, slash.origin.y, slash.origin.add(slash.direction.normalize()).y);
            double endYMid = MathHelper.lerp(yDelta, slash.origin.y, slash.origin.add(slash.direction.normalize().multiply(0.5f)).y);
            double endZNeg = MathHelper.lerp(zDelta, slash.origin.z, slash.origin.add(slash.direction.add(slash.direction.normalize().multiply(0.1).rotateX((float) (Math.PI / 2))).normalize()).z);
            double endZPos = MathHelper.lerp(zDelta, slash.origin.z, slash.origin.add(slash.direction.add(slash.direction.normalize().multiply(0.1).rotateX((float) -(Math.PI / 2))).normalize()).z);
            double edgeOffset = MathHelper.lerp(zDelta, 0, slash.direction.normalize().multiply(slash.getSize()).multiply(0.05).y);

            buildSlashVertices(slash, bufferbuilder, transformation, xOffset, camX, endYMid, camY, camZ, red, nRed, endZNeg, endY, endZPos);

            matrices.pop();

            /*BufferBuilder.BuiltBuffer builtBuffer = bufferbuilder.end();
            SlashRendererManager.slashBuffer.bind();
            SlashRendererManager.slashBuffer.upload(builtBuffer);
            VertexBuffer.unbind();*/

            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

            /*if (SlashRendererManager.slashBuffer != null) {
                SlashRendererManager.slashBuffer.bind();
                ShaderProgram shaderProgram = RenderSystem.getShader();
                Matrix4f positionMatrix = RenderSystem.getModelViewStack().peek().getPositionMatrix();

                SlashRendererManager.slashBuffer.draw(positionMatrix, RenderSystem.getProjectionMatrix(), shaderProgram);
            }*/
            tessellator.draw();

            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
        }
    }

    private static void buildSlashVertices(SlashRender slash, BufferBuilder bufferbuilder, Matrix4f transformation, double xOffset, double camX, double endYMid, double camY, double camZ, float red, float nRed, double endZNeg, double endY, double endZPos) {
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(1.0f, 0.9f, 0.9f, 0.9f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(red, nRed, nRed, 0.9f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (endZNeg - camZ)).color(red, nRed, nRed, 0.9f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(red, nRed, nRed, 0.9f).next();

        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(1.0f, 0.9f, 0.9f, 0.9f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(red, nRed, nRed, 0.9f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (endZPos - camZ)).color(red, nRed, nRed, 0.9f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(red, nRed, nRed, 0.9f).next();
    }
}
