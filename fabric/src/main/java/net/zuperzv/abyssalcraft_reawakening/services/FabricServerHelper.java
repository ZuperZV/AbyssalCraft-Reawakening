package net.zuperzv.abyssalcraft_reawakening.services;

import net.minecraft.server.MinecraftServer;
import net.zuperzv.abyssalcraft_reawakening.services.types.IServerHelper;

public class FabricServerHelper implements IServerHelper {

    private static MinecraftServer server;

    public static void onServerStarted(MinecraftServer srv) {
        server = srv;
    }

    @Override
    public MinecraftServer getCurrentServer() {
        return server;
    }
}