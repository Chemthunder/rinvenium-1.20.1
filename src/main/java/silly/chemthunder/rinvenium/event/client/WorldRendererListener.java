package silly.chemthunder.rinvenium.event.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import silly.chemthunder.rinvenium.render.FakePlayerRenderer;
import silly.chemthunder.rinvenium.render.ImpactFrame;
import silly.chemthunder.rinvenium.render.SlashRender;
import silly.chemthunder.rinvenium.render.manager.global.CustomFogManager;
import silly.chemthunder.rinvenium.render.manager.ImpactFrameManager;
import silly.chemthunder.rinvenium.render.manager.global.PlayerRendererManager;
import silly.chemthunder.rinvenium.render.manager.global.SlashRendererManager;
import silly.chemthunder.rinvenium.util.RinveniumTextureUtils;
import silly.chemthunder.rinvenium.util.inject.RenderContainer;

import java.util.ArrayList;
import java.util.List;

public class WorldRendererListener {
    public static void execute() {
        WorldRenderEvents.LAST.register(context -> {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientWorld world = client.world;
            Camera camera = context.camera();

            if (world != null) {
                SlashRendererManager.tick();
                SlashRendererManager.get().forEach(slashRender -> renderSlashes(context, client, world, camera, slashRender));
                if (client.player != null) {
                    ImpactFrameManager impactFrameManager = ((RenderContainer) client.player).getImpactFrameManager();
                    impactFrameManager.tick();
                    impactFrameManager.get().forEach(impactFrame -> {
                        if (impactFrameManager.shouldShow()) {
                            client.options.hudHidden = true;
                            renderImpactFrame(context, client, world, camera, impactFrame);
                        } else {
                            client.options.hudHidden = false;
                        }
                    });
                }
                CustomFogManager.tick();
                PlayerRendererManager.tick();
                PlayerRendererManager.get().forEach(fakePlayerRenderer -> renderPlayer(context, client, world, camera, fakePlayerRenderer));
            }
        });
    }

    private static void renderPlayer(WorldRenderContext context, MinecraftClient client, ClientWorld world, Camera camera, FakePlayerRenderer playerRenderer) {
        /*context.worldRenderer().renderEntity(
                new PlayerEntity(world, new BlockPos((int) playerRenderer.origin.x, (int) playerRenderer.origin.y, (int) playerRenderer.origin.z), playerRenderer.yaw, null) {
                    @Override
                    public boolean isSpectator() {
                        return false;
                    }

                    @Override
                    public boolean isCreative() {
                        return false;
                    }
                },
                camera.getPos().getX(),
                camera.getPos().getY(),
                camera.getPos().getZ(),
                context.tickDelta(),
                context.matrixStack(),
                context.worldRenderer().bufferBuilders.getEntityVertexConsumers()
        );*/
    }

    private static void renderImpactFrame(WorldRenderContext context, MinecraftClient client, ClientWorld world, Camera camera, ImpactFrame impactFrame) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        double viewDistance = client.options.getClampedViewDistance() * 16;

        double camX = camera.getPos().getX();
        double camY = camera.getPos().getY();
        double camZ = camera.getPos().getZ();

        RenderSystem.depthMask(false);
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();

        RenderSystem.setShader(GameRenderer::getRenderTypeOutlineProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        for (Entity entity : world.getEntities()) {
            if (entity.getUuid().equals(impactFrame.uuid)) {
                BlockPos blockPos = entity.getBlockPos();
                if ((context.world().isOutOfHeightLimit(blockPos.getY()) || context.worldRenderer().isRenderingReady(blockPos))) {

                    if (entity.age == 0) {
                        entity.lastRenderX = entity.getX();
                        entity.lastRenderY = entity.getY();
                        entity.lastRenderZ = entity.getZ();
                    }

                    VertexConsumerProvider vertexConsumerProvider;
                    OutlineVertexConsumerProvider outlineVertexConsumerProvider = context.worldRenderer().bufferBuilders.getOutlineVertexConsumers();
                    vertexConsumerProvider = outlineVertexConsumerProvider;
                    int i = 0x000000;
                    outlineVertexConsumerProvider.setColor(ColorHelper.Argb.getRed(i), ColorHelper.Argb.getGreen(i), ColorHelper.Argb.getBlue(i), 255);

                    List<SlashRender> slashRenders = new ArrayList<>();
                    for (SlashRender slashRender : SlashRendererManager.get()) {
                        if (entity.getBoundingBox().expand(5).contains(slashRender.presetOrigin)) {
                            slashRenders.add(slashRender);
                        }
                    }

                    VertexConsumer outlineVertexConsumer = outlineVertexConsumerProvider.getBuffer(RenderLayer.getOutline(RinveniumTextureUtils.SCREEN_FLASH));

                    for (SlashRender slash : slashRenders) {
                        float ageDelta = (float) slash.age / slash.maxAge;
                        float yDelta;
                        float zDelta;
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
                        double endY = MathHelper.lerp(yDelta, slash.origin.y, slash.origin.add(slash.direction.normalize()).y);
                        double endYMid = MathHelper.lerp(yDelta, slash.origin.y, slash.origin.add(slash.direction.normalize().multiply(0.5f)).y);
                        double endZNeg = MathHelper.lerp(zDelta, slash.origin.z, slash.origin.add(slash.direction.add(slash.direction.normalize().multiply(0.1).rotateX((float) (Math.PI / 2))).normalize()).z);
                        double endZPos = MathHelper.lerp(zDelta, slash.origin.z, slash.origin.add(slash.direction.add(slash.direction.normalize().multiply(0.1).rotateX((float) -(Math.PI / 2))).normalize()).z);

                        MatrixStack matrices = context.matrixStack();
                        matrices.push();

                        matrices.translate(-camX, -camY, -camZ);
                        matrices.translate(slash.origin.add(slash.direction.normalize().multiply(0.5f)).x, slash.origin.add(slash.direction.normalize().multiply(0.5f)).y, slash.origin.add(slash.direction.normalize().multiply(0.5f)).z);

                        slash.TRANSFORMATION.forEach(matrices::multiply);
                        matrices.scale(slash.getSize(), slash.getSize(), slash.getSize());

                        Matrix4f transformation = matrices.peek().getPositionMatrix();

                        matrices.translate(-slash.origin.add(slash.direction.normalize().multiply(0.5f)).x, -slash.origin.add(slash.direction.normalize().multiply(0.5f)).y, -slash.origin.add(slash.direction.normalize().multiply(0.5f)).z);
                        matrices.translate(camX, camY, camZ);

                        buildSlashOutlineVertices(slash, outlineVertexConsumer, transformation, 0.0, camX, camY, camZ, endYMid, endZNeg, endY, endZPos);

                        matrices.pop();
                    }

                    context.worldRenderer().entityOutlinePostProcessor.render(context.tickDelta());
                    client.getFramebuffer().beginWrite(false);

                    context.worldRenderer().renderEntity(entity, camX, camY, camZ, context.tickDelta(), context.matrixStack(), vertexConsumerProvider);

                    VertexConsumerProvider.Immediate normal = context.worldRenderer().bufferBuilders.getEntityVertexConsumers();
                    RenderSystem.setShaderColor(255.0f, 255.0f, 255.0f, 1.0f);
                    context.worldRenderer().renderEntity(entity, camX, camY, camZ, context.tickDelta(), context.matrixStack(), normal);
                    normal.draw();
                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        }

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.enableBlend();
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

            boolean shouldRender = true;
            if (client.player != null) {
                shouldRender = ((RenderContainer) client.player).getImpactFrameManager().get().isEmpty();
            }
            if (shouldRender) {
                buildSlashVertices(slash, bufferbuilder, transformation, xOffset, camX, camY, camZ, red, nRed, endYMid, endZNeg, endY, endZPos);
            }

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
            RenderSystem.depthMask(true);
        }
    }

    private static void buildSlashVertices(SlashRender slash, BufferBuilder bufferbuilder, Matrix4f transformation, double xOffset, double camX, double camY, double camZ, float red, float nRed, double endYMid, double endZNeg, double endY, double endZPos) {
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(1.0f, 0.9f, 0.9f, 0.9f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(red, nRed, nRed, 0.9f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (endZNeg - camZ)).color(red, nRed, nRed, 0.9f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(red, nRed, nRed, 0.9f).next();

        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(1.0f, 0.9f, 0.9f, 0.9f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(red, nRed, nRed, 0.9f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (endZPos - camZ)).color(red, nRed, nRed, 0.9f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(red, nRed, nRed, 0.9f).next();
    }
    private static void buildSlashOutlineVertices(SlashRender slash, VertexConsumer vertexConsumer, Matrix4f transformation, double xOffset, double camX, double camY, double camZ, double endYMid, double endZNeg, double endY, double endZPos) {
        vertexConsumer.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).texture(1, 1).normal(1, 0, 0).next();
        vertexConsumer.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).texture(1, 0).normal(1, 0, 0).next();
        vertexConsumer.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (endZNeg - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).texture(0, 0).normal(1, 0, 0).next();
        vertexConsumer.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).texture(0, 1).normal(1, 0, 0).next();

        vertexConsumer.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).texture(1, 1).normal(1, 0, 0).next();
        vertexConsumer.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).texture(1, 0).normal(1, 0, 0).next();
        vertexConsumer.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (endZPos - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).texture(0, 0).normal(1, 0, 0).next();
        vertexConsumer.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).texture(0, 1).normal(1, 0, 0).next();
    }

    private static void buildSlashOutlineVertices_(SlashRender slash, BufferBuilder bufferbuilder, MatrixStack matrices, Matrix4f transformation, double xOffset, double camX, double camY, double camZ, double endYMid, double endZNeg, double endY, double endZPos) {
        float outlineScalar = 1.2f;
        matrices.translate(-camX, -camY, -camZ);
        matrices.translate(slash.origin.add(slash.direction.normalize().multiply(0.5f)).x, slash.origin.add(slash.direction.normalize().multiply(0.5f)).y, slash.origin.add(slash.direction.normalize().multiply(0.5f)).z);

        matrices.scale(outlineScalar, outlineScalar, outlineScalar);

        matrices.translate(-slash.origin.add(slash.direction.normalize().multiply(0.5f)).x, -slash.origin.add(slash.direction.normalize().multiply(0.5f)).y, -slash.origin.add(slash.direction.normalize().multiply(0.5f)).z);
        matrices.translate(camX, camY, camZ);

        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(0.0f, 0.0f, 0.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(0.0f, 0.0f, 0.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (endZNeg - camZ)).color(0.0f, 0.0f, 0.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(0.0f, 0.0f, 0.0f, 1.0f).next();

        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(0.0f, 0.0f, 0.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(0.0f, 0.0f, 0.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (endZPos - camZ)).color(0.0f, 0.0f, 0.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(0.0f, 0.0f, 0.0f, 1.0f).next();

        matrices.translate(-camX, -camY, -camZ);
        matrices.translate(slash.origin.add(slash.direction.normalize().multiply(0.5f)).x, slash.origin.add(slash.direction.normalize().multiply(0.5f)).y, slash.origin.add(slash.direction.normalize().multiply(0.5f)).z);

        matrices.scale(1 / outlineScalar, 1 / outlineScalar, 1 / outlineScalar);

        matrices.translate(-slash.origin.add(slash.direction.normalize().multiply(0.5f)).x, -slash.origin.add(slash.direction.normalize().multiply(0.5f)).y, -slash.origin.add(slash.direction.normalize().multiply(0.5f)).z);
        matrices.translate(camX, camY, camZ);

        xOffset = 4.98E-5;

        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (endZNeg - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();

        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endYMid - camY), (float) (endZPos - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x + xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();

        bufferbuilder.vertex(transformation, (float) (slash.origin.x - xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x - xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x - xOffset - camX), (float) (endYMid - camY), (float) (endZNeg - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x - xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();

        bufferbuilder.vertex(transformation, (float) (slash.origin.x - xOffset - camX), (float) (endYMid - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x - xOffset - camX), (float) (slash.origin.y - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x - xOffset - camX), (float) (endYMid - camY), (float) (endZPos - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferbuilder.vertex(transformation, (float) (slash.origin.x - xOffset - camX), (float) (endY - camY), (float) (slash.origin.z - camZ)).color(1.0f, 1.0f, 1.0f, 1.0f).next();
    }
}
