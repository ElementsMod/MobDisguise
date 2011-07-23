package me.desmin88.mobdisguise.utils;

import java.lang.reflect.Field;

import me.desmin88.mobdisguise.MobDisguise;
import net.minecraft.server.DataWatcher;
import net.minecraft.server.Packet20NamedEntitySpawn;
import net.minecraft.server.Packet24MobSpawn;
import net.minecraft.server.Packet29DestroyEntity;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketUtils {
    public MobDisguise plugin;

    public PacketUtils(MobDisguise instance) {
        plugin = instance;
    }

    public void undisguiseToAll(Player p1) {
        for (Player p2 : Bukkit.getServer().getOnlinePlayers()) {
            if (p2 == p1) {
                continue;
            }
            CraftPlayer p22 = (CraftPlayer) p1;
            //Packet24MobSpawn p24 = packetMaker(p1, plugin.playerMobId.get(p1));
            Packet29DestroyEntity p29 = new Packet29DestroyEntity(p22.getEntityId());
            Packet20NamedEntitySpawn p20 = new Packet20NamedEntitySpawn(p22.getHandle());
            // Premake packets, important.
            ((CraftPlayer) p2).getHandle().netServerHandler.sendPacket(p29);
            ((CraftPlayer) p2).getHandle().netServerHandler.sendPacket(p20);
        }
    }
    
    public void disguiseToAll(Player p1) {
        for (Player p2 : Bukkit.getServer().getOnlinePlayers()) {
            if (p2 == p1) {
                continue;
            }
            CraftPlayer p22 = (CraftPlayer) p1;
            Packet24MobSpawn p24 = packetMaker(p1, plugin.playerMobId.get(p1));
            Packet29DestroyEntity p29 = new Packet29DestroyEntity(p22.getEntityId());
            // Premake packets, important.
            ((CraftPlayer) p2).getHandle().netServerHandler.sendPacket(p29);
            ((CraftPlayer) p2).getHandle().netServerHandler.sendPacket(p24);
        }
    }

    public Packet24MobSpawn packetMaker(Player p1, Byte id) {
        Packet24MobSpawn packet = new Packet24MobSpawn();
        packet.a = ((CraftPlayer) p1).getEntityId();
        if (id == null || id == 0) {
            packet.b = 90;
        } else {
            packet.b = id.byteValue();
        }
        packet.c = (int) p1.getLocation().getX();
        packet.d = (int) p1.getLocation().getY();
        packet.e = (int) p1.getLocation().getZ();
        packet.f = (byte) p1.getLocation().getYaw();
        packet.g = (byte) p1.getLocation().getPitch();
        Field datawatcher;
        try {
            datawatcher = packet.getClass().getDeclaredField("h");
            datawatcher.setAccessible(true);
            datawatcher.set(packet, new DataWatcher());
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return packet;
    }

}