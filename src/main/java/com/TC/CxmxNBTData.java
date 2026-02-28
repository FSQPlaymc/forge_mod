package com.TC;

import net.minecraft.nbt.NBTTagCompound;

public class CxmxNBTData extends NBTTagCompound {
    public int killcount;
    public float health;
    public int brokenblocks;
    public float bonus;
    public int curse;
    public String name;
    public float radius;
    public int hunger;
    public float dfloat;
    public int dint;
    public int times;
    public boolean active;
    public int soul;
    public float damage;
    public boolean cxmxswitch;

    public static CxmxNBTData read(NBTTagCompound tag) {
        CxmxNBTData data = new CxmxNBTData();
        data.killcount = tag.getInteger("killcount");
        data.brokenblocks = tag.getInteger("brokenblocks");
        data.health = tag.getFloat("health");
        data.bonus = tag.getFloat("bonus");
        data.curse = tag.getInteger("curse");
        data.name = tag.getString("name");
        data.radius = tag.getFloat("radius");
        data.dfloat = tag.getFloat("dfloat");
        data.hunger = tag.getInteger("hunger");
        data.dint = tag.getInteger("dint");
        data.times = tag.getInteger("times");
        data.active = tag.getBoolean("active");
        data.soul = tag.getInteger("soul");
        data.damage = tag.getFloat("damage");
        data.cxmxswitch = tag.getBoolean("cxmxswitch");

        return data;
    }

    public void write(NBTTagCompound tag) {
        tag.setInteger("killcount", killcount);
        tag.setInteger("brokenblocks", brokenblocks);
        tag.setFloat("health", health);
        tag.setFloat("bonus", bonus);
        tag.setInteger("curse", curse);
        tag.setString("name", name);
        tag.setFloat("radius", radius);
        tag.setInteger("dint", dint);
        tag.setInteger("hunger", hunger);
        tag.setInteger("times", times);
        tag.setFloat("dfloat", dfloat);
        tag.setBoolean("active", active);
        tag.setInteger("soul", soul);
        tag.setFloat("damage", damage);
        tag.setBoolean("cxmxswitch", cxmxswitch);
    }
}
