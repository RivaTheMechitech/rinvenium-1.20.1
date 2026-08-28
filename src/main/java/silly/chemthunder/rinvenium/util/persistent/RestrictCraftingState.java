package silly.chemthunder.rinvenium.util.persistent;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

public class RestrictCraftingState extends PersistentState {
    public static final String LOCK_RECIPES_KEY = "RinveniumLockedRecipes";

    public boolean lockRecipes = false;

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putBoolean(LOCK_RECIPES_KEY, this.lockRecipes);
        return nbt;
    }

    public static RestrictCraftingState createFromNbt(NbtCompound nbt) {
        RestrictCraftingState state = new RestrictCraftingState();
        state.lockRecipes = nbt.getBoolean(LOCK_RECIPES_KEY);
        return state;
    }

    public static RestrictCraftingState createNew() {
        RestrictCraftingState state = new RestrictCraftingState();
        state.lockRecipes = false;
        return state;
    }

    public static RestrictCraftingState getServerState(MinecraftServer server) {
        ServerWorld serverWorld = server.getWorld(ServerWorld.OVERWORLD);
        assert serverWorld != null;

        RestrictCraftingState state = serverWorld.getPersistentStateManager().getOrCreate(RestrictCraftingState::createFromNbt, RestrictCraftingState::createNew, "restrictRinveniumCraftingRecipes");
        state.markDirty();
        return state;
    }
}