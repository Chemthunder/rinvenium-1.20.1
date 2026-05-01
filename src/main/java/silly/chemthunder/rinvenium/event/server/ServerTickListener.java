package silly.chemthunder.rinvenium.event.server;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import silly.chemthunder.rinvenium.render.manager.global.PlayerRendererManager;
import silly.chemthunder.rinvenium.util.persistent.DeathSequenceState;

public class ServerTickListener {
    private static ServerBossBar bossBar = (ServerBossBar) new ServerBossBar(Text.literal("abyss"), BossBar.Color.RED, BossBar.Style.PROGRESS).setDarkenSky(true);
    public static void execute() {
        ServerTickEvents.END_WORLD_TICK.register(serverWorld -> {
            DeathSequenceState deathSequenceState = DeathSequenceState.getServerState(serverWorld.getServer());
            if (deathSequenceState.shouldStartPostTick && !serverWorld.isClient) {
                if (deathSequenceState.postTick == 0) {
                    serverWorld.getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
                        bossBar.addPlayer(serverPlayerEntity);
                    });
                }
                if (deathSequenceState.postTick == 20 * 6) {
                    sendServerMessageS("<orchidpuppy> ...to the other friends left.", serverWorld);
                }
                if (deathSequenceState.postTick == 20 * 10) {
                    sendServerMessageS("<orchidpuppy> just remember.", serverWorld);
                }
                if (deathSequenceState.postTick == 20 * 14) {
                    if (serverWorld.getServer() != null) {
                        bossBar.setName(Text.literal("we're always watching").formatted(Formatting.RED).formatted(Formatting.ITALIC));
                    }
                }
                if (deathSequenceState.postTick == 20 * 16) {
                    PlayerRendererManager.remove("orchidpuppy");
                }
                if (deathSequenceState.postTick >= 20 * 16 + 20 * 120) {
                    deathSequenceState.shouldStartPostTick = false;
                    bossBar.clearPlayers();
                    deathSequenceState.postTick = 0;
                }
                if (deathSequenceState.shouldStartPostTick) deathSequenceState.postTick++;
                deathSequenceState.markDirty();
            }
        });
    }

    private static void sendServerMessageT(MutableText literal, ServerWorld serverWorld) {
        if (serverWorld.getServer() != null) {
            serverWorld.getServer().getPlayerManager().broadcast(literal, false);
        }
    }
    private static void sendServerMessageS(String string, ServerWorld serverWorld) {
        if (serverWorld.getServer() != null) {
            serverWorld.getServer().getPlayerManager().broadcast(Text.literal(string).formatted(Formatting.RED), false);
        }
    }
}
