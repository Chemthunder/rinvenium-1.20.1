package silly.chemthunder.rinvenium.util;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class RinveniumUuids {
    public static final List<UUID> UUIDS = new ArrayList<>();
    public static final List<UUID> CRAFT_ENVIXIA = new ArrayList<>();

    public static final String RIVA_S = "4a58221e-97d0-40cd-9425-01e0ff15ecea";
    public static final UUID RIVA = create(RIVA_S, CRAFT_ENVIXIA, UUIDS);
    public static final String MIDGET_S = "b0a8bc6a-4856-44e6-8301-a036d37c24df";
    public static final UUID MIDGET = create(MIDGET_S, CRAFT_ENVIXIA, UUIDS);

    // add me frfr trust ong 

    @SafeVarargs
    private static UUID create(String uuid, List<UUID>... lists) {
        UUID created = UUID.fromString(uuid);
        Stream<List<UUID>> uuidStream = Arrays.stream(lists);
        uuidStream.forEach(uuids -> {
            uuids.add(created);
        });
        return created;
    }

    public static boolean canCraftEnvixia(ServerPlayerEntity serverPlayerEntity) {
        return CRAFT_ENVIXIA.contains(serverPlayerEntity.getUuid());
    }
}