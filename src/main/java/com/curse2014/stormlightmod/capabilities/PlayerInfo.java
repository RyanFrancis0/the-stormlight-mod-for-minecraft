package com.curse2014.stormlightmod.capabilities;

import com.curse2014.stormlightmod.objects.items.ShardBladeItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.INBT;

import java.util.ArrayList;

public class PlayerInfo implements IPlayerInfo {
    private float stormlight = 0f; // max 1000
    private int blade = -1;
    private ArrayList<ShardBladeItem> bondedBlades = new ArrayList<>();
    private int ideal = 4; //1==1st surge && stormlight 2==blade 3==2nd surge, 4==plate, change to 0 post oath making
    public int order = 0;// rand.nextInt();//list orders
    public static int MAX_STORMLIGHT = 10000;
    public static int CHARGE_RATE = 1000;

    @Override
    public float getStormlight() {
        //System.out.println(this.stormlight);
        return this.stormlight;
    }

    @Override
    public void changeStormlight(float stormlight) {
        this.setStormlight(this.getStormlight() + stormlight);
    }

    @Override
    public void setStormlight(float stormlight) {
        if (this.getIdeal() > 0) {
            this.stormlight = stormlight;
            if (this.stormlight < 0) {
                this.stormlight = 0;
            } else if (this.stormlight > MAX_STORMLIGHT) {
                this.stormlight = MAX_STORMLIGHT;
            }
        }
    }

    @Override
    public int getBlade() {
        return this.blade;
    }

    @Override
    public void setBlade(int blade) {
        this.blade = blade;
    }

    public void bondBlade(ShardBladeItem blade) {
        this.bondedBlades.add(blade);
    }

    @Override
    public int getIdeal() {//so get level
        return this.ideal;
    }

    @Override
    public void oathAccepted() {
        //create player achievement where utilise this
        this.ideal++;
        //need to do order specific things here, but too complicated atm
        //such as should i now have a shardblade stored up
        //means each order should be a class and i can say this.order.oathAccepted
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public boolean canDoThing(int thing) {
        if (thing == 0) {
            if (blade != -1) {
                return true;
            }
        } else if (thing == 1) { //or has honourblade as one of blades
            //can use 1st surge
            //this.order.firstsurge
        } else if (thing == 2) { //or has honourblade as one of blades
            //can use 2nd surge
            //this.order.secondsurge
        } else if (thing == 3) {
            //can summon plate
        }
        return true;//change to false
    }

    public INBT convertToNBT() {
        return PlayerInfoProvider.PLAYER_INFO.getStorage().writeNBT(PlayerInfoProvider.PLAYER_INFO, this, null);
    }

    public void setValuesFromNBT(INBT nbt) {
        PlayerInfoProvider.PLAYER_INFO.getStorage().readNBT(PlayerInfoProvider.PLAYER_INFO, this, null, nbt);
    }

    public static IPlayerInfo getFromPlayer(PlayerEntity player){
        return player
                .getCapability(PlayerInfoProvider.PLAYER_INFO,null)
                .orElseThrow(()->new IllegalArgumentException("LazyOptional must be not empty!"));
    }
}
