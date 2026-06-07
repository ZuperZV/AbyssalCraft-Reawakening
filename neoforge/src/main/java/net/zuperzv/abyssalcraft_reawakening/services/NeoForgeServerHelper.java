package net.zuperzv.abyssalcraft_reawakening.services;

import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.zuperzv.abyssalcraft_reawakening.services.types.IServerHelper;

public class NeoForgeServerHelper implements IServerHelper {
    @Override
    public MinecraftServer getCurrentServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }
}