package com.curse2014.stormlightmod.capabilities;

import com.curse2014.stormlightmod.objects.items.ShardBladeItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.storage.loot.RandomValueRange;

import java.util.ArrayList;
import java.util.Random;

public class PlayerInfo implements IPlayerInfo {
    private float stormlight = 0f; // max 1000
    private int blade = -1;
    private ArrayList<ShardBladeItem> bondedBlades = new ArrayList<>();
    private int ideal = 4; //1==1st surge && stormlight 2==blade 3==2nd surge, 4==plate, change to 0 post oath making
    private Random rand = new Random();
    public int order = 0;// rand.nextInt();//list orders

    @Override
    public float getStormlight() {
        //System.out.println(this.stormlight);
        return this.stormlight;
    }

    @Override
    public void changeStormlight(float stormlight) {
        if (this.getIdeal() > 0) {
            this.stormlight += stormlight;
            if (this.stormlight < 0) {
                this.stormlight = 0;
            } else if (this.stormlight > 1000) {
                this.stormlight = 1000;
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

    public static IPlayerInfo getFromPlayer(PlayerEntity player){
        return player
                .getCapability(PlayerInfoProvider.PLAYER_INFO,null)
                .orElseThrow(()->new IllegalArgumentException("LazyOptional must be not empty!"));
    }
}
