package com.deriys.divinerelics.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class DivineRelicsCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.DoubleValue MOTOSIGNIR_RANGE;
    public static final ForgeConfigSpec.DoubleValue MOTOSIGNIR_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue MOTOSIGNIR_FORCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> MOTOSIGNIR_EFFECTS_DURATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> MOTOSIGNIR_COOLDOWN;

    public static final ForgeConfigSpec.ConfigValue<Integer> HEIMDALL_GAUNTLET_DURATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> HEIMDALL_GAUNTLET_COOLDOWN;

    public static final ForgeConfigSpec.ConfigValue<Integer> GUARDIAN_SHIELD_PARRY_WINDOW;

    public static final ForgeConfigSpec.DoubleValue MJOLNIR_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue MJOLNIR_ATTACK_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Integer> MJOLNIR_THROW_THRESHOLD;
    public static final ForgeConfigSpec.DoubleValue MJOLNIR_SHOOT_POWER;
    public static final ForgeConfigSpec.ConfigValue<Integer> MJOLNIR_THROW_COOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Integer> MJOLNIR_RIPTIDE_COOLDOWN;

    public static final ForgeConfigSpec.DoubleValue THROWN_MJOLNIR_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue THROWN_MJOLNIR_STRIKE_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue THROWN_MJOLNIR_STRIKE_RADIUS;
    public static final ForgeConfigSpec.DoubleValue THROWN_MJOLNIR_STRIKE_FORCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> THROWN_MJOLNIR_LIGHNTING_COUNT;
    public static final ForgeConfigSpec.DoubleValue THROWN_MJOLNIR_RETURNING_SPEED;

    public static final ForgeConfigSpec.DoubleValue LEVIATHAN_AXE_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue LEVIATHAN_AXE_ATTACK_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Integer> LEVIATHAN_AXE_THROW_THRESHOLD;
    public static final ForgeConfigSpec.DoubleValue LEVIATHAN_AXE_SHOOT_POWER;
    public static final ForgeConfigSpec.ConfigValue<Integer> LEVIATHAN_AXE_THROW_COOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Integer> LEVIATHAN_AXE_FREEZE_TIME_HIT;
    public static final ForgeConfigSpec.ConfigValue<Integer> THROWN_LEVIATHAN_FROZEN_TICKS_HIT;
    public static final ForgeConfigSpec.DoubleValue THROWN_LEVIATHAN_RETURNING_SPEED;

    public static final ForgeConfigSpec.DoubleValue DRAUPNIR_SPEAR_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue DRAUPNIR_SPEAR_SHOOT_POWER;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRAUPNIR_SPEAR_THROW_THRESHOLD;
    public static final ForgeConfigSpec.DoubleValue THROWN_DRAUPNIR_SPEAR_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue DRAUPNIR_SPEAR_ATTACK_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRAUPNIR_SPEARS_COUNT;
    public static final ForgeConfigSpec.DoubleValue DRAUPNIR_SPEAR_EXPLOSION_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue DRAUPNIR_SPEAR_EXPLOSION_RADIUS;

    public static final ForgeConfigSpec.DoubleValue DRAUGR_HP;
    public static final ForgeConfigSpec.DoubleValue DRAUGR_ARMOR;
    public static final ForgeConfigSpec.DoubleValue DRAUGR_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRAUGR_BURNING_TIME;
    public static final ForgeConfigSpec.DoubleValue DRAUGR_KB_RESISTANCE;
    public static final ForgeConfigSpec.DoubleValue DRAUGR_FOLLOW_RANGE;
    public static final ForgeConfigSpec.DoubleValue DRAUGR_SPEED_MODIFIER;

    public static final ForgeConfigSpec.DoubleValue HEL_WALKER_HP;
    public static final ForgeConfigSpec.DoubleValue HEL_WALKER_ARMOR;
    public static final ForgeConfigSpec.DoubleValue HEL_WALKER_KB_RESISTANCE;
    public static final ForgeConfigSpec.DoubleValue HEL_WALKER_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Integer> HEL_WALKER_FREEZING_TIME;
    public static final ForgeConfigSpec.DoubleValue HEL_WALKER_FOLLOW_RANGE;
    public static final ForgeConfigSpec.DoubleValue HEL_WALKER_SPEED_MODIFIER;

    public static final ForgeConfigSpec.DoubleValue THOR_HP;
    public static final ForgeConfigSpec.DoubleValue THOR_ARMOR;
    public static final ForgeConfigSpec.DoubleValue THOR_KB_RESISTANCE;
    public static final ForgeConfigSpec.DoubleValue THOR_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue THOR_FOLLOW_RANGE;
    public static final ForgeConfigSpec.DoubleValue THOR_SPEED_MODIFIER;
    public static final ForgeConfigSpec.DoubleValue THOR_CLAP_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue THOR_CLAP_FORCE;
    public static final ForgeConfigSpec.DoubleValue THOR_CLAP_RADIUS;
    public static final ForgeConfigSpec.DoubleValue THOR_GROUND_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue THOR_GROUND_FORCE;
    public static final ForgeConfigSpec.DoubleValue THOR_GROUND_RADIUS;
    public static final ForgeConfigSpec.DoubleValue THOR_SHIELD_KNOCKBACK_DISTANCE;
    public static final ForgeConfigSpec.DoubleValue THOR_SHIELD_KNOCKBACK_HEIGHT;
    public static final ForgeConfigSpec.DoubleValue THOR_MELEE_KNOCKBACK_DISTANCE;
    public static final ForgeConfigSpec.DoubleValue THOR_MELEE_SHIELD_KNOCKBACK_DISTANCE;
    public static final ForgeConfigSpec.DoubleValue THOR_MELEE_KNOCKBACK_HEIGHT;
    public static final ForgeConfigSpec.DoubleValue THOR_LEG_KNOCKBACK_DISTANCE;
    public static final ForgeConfigSpec.DoubleValue THOR_LEG_SHIELD_KNOCKBACK_DISTANCE;
    public static final ForgeConfigSpec.DoubleValue THOR_LEG_KNOCKBACK_HEIGHT;
    public static final ForgeConfigSpec.DoubleValue THOR_CLAP_KNOCKBACK_DISTANCE;
    public static final ForgeConfigSpec.DoubleValue THOR_CLAP_SHIELD_KNOCKBACK_DISTANCE;
    public static final ForgeConfigSpec.DoubleValue THOR_CLAP_KNOCKBACK_HEIGHT;
    public static final ForgeConfigSpec.DoubleValue THOR_GROUND_KNOCKBACK_DISTANCE;
    public static final ForgeConfigSpec.DoubleValue THOR_GROUND_SHIELD_KNOCKBACK_DISTANCE;
    public static final ForgeConfigSpec.DoubleValue THOR_GROUND_KNOCKBACK_HEIGHT;
    public static final ForgeConfigSpec.DoubleValue THOR_MJOLNIR_KNOCKBACK_DISTANCE;
    public static final ForgeConfigSpec.DoubleValue THOR_MJOLNIR_SHIELD_KNOCKBACK_DISTANCE;
    public static final ForgeConfigSpec.DoubleValue THOR_MJOLNIR_KNOCKBACK_HEIGHT;
    public static final ForgeConfigSpec.DoubleValue THOR_AREA_MIN_DAMAGE_FACTOR;
    public static final ForgeConfigSpec.DoubleValue THOR_AREA_MIN_KNOCKBACK_FACTOR;
    public static final ForgeConfigSpec.DoubleValue THOR_CLOSE_KNOCKBACK_RANGE;
    public static final ForgeConfigSpec.DoubleValue THOR_CLOSE_KNOCKBACK_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Integer> THOR_XP_AMOUNT;

    public static final ForgeConfigSpec.ConfigValue<Integer> BROK_AND_SINDRI_RESTOCK_TIME;

    public static final ForgeConfigSpec.ConfigValue<Integer> HACKSILVER_ORE_VEINS_PER_CHUNK;
    public static final ForgeConfigSpec.ConfigValue<Integer> HACKSILVER_ORE_VEIN_SIZE;

    public static final ForgeConfigSpec.ConfigValue<Integer> SVARTALFHEIM_STEEL_ORE_VEINS_PER_CHUNK;
    public static final ForgeConfigSpec.ConfigValue<Integer> SVARTALFHEIM_STEEL_ORE_VEIN_SIZE;

    public static final ForgeConfigSpec.ConfigValue<Integer> ASGARDIAN_STEEL_ORE_VEIN_SIZE;
    public static final ForgeConfigSpec.ConfigValue<Integer> ASGARDIAN_STEEL_ORE_VEINS_PER_CHUNK;

    public static final ForgeConfigSpec.ConfigValue<Boolean> NO_WATER_NEARBY_USE;
    public static final ForgeConfigSpec.ConfigValue<Integer> NO_WATER_NEARBY_DISTANCE;

    static {
        BUILDER.push("Common config for Divine Relics");

        MOTOSIGNIR_RANGE = BUILDER.comment("The range of the Motosignir's sound wave effect (in blocks)")
                .defineInRange("Motosignir Radius", 10f, 0f, 30f);
        MOTOSIGNIR_DAMAGE = BUILDER.comment("The damage dealt by the Motosignir's sound wave.")
                .defineInRange("Motosignir Damage", 20.0f, 0f, 100f);
        MOTOSIGNIR_FORCE = BUILDER.comment("The knockback force applied to targets hit by the Motosignir's sound wave.")
                .defineInRange("Motosignir Force", 1.1f, 0f, 100f);
        MOTOSIGNIR_EFFECTS_DURATION = BUILDER.comment("Duration of the stun effects caused by the Motosignir (in ticks)")
                .define("Motosignir Duration", 20);
        MOTOSIGNIR_COOLDOWN = BUILDER.comment("Cooldown period for the Motosignir in ticks.")
                .define("Motosignir Cooldown", 200);

        HEIMDALL_GAUNTLET_DURATION = BUILDER.comment("Duration of Bifrost protection provided by Heimdall's Gauntlet (in ticks)")
                .define("Heimdall's Gauntlet Duration", 300);
        HEIMDALL_GAUNTLET_COOLDOWN = BUILDER.comment("Cooldown period for using Heimdall's Gauntlet, measured in ticks.")
                .define("Heimdall's Gauntlet Cooldown", 1200);

        GUARDIAN_SHIELD_PARRY_WINDOW = BUILDER.comment("Time window for successfully parrying an attack after opening the Guardian Shield (in ticks).")
                .define("Parry Window Duration", 12);

        MJOLNIR_DAMAGE = BUILDER.comment("Melee damage dealt by Mjölnir.")
                .defineInRange("Mjölnir Damage", 25.0f, 0f, 1000f);
        MJOLNIR_ATTACK_SPEED = BUILDER.comment("Attack speed of Mjölnir.")
                .defineInRange("Mjölnir Speed", -3.25f, -10f, 10f);
        MJOLNIR_THROW_THRESHOLD = BUILDER.comment("The minimum charge time before Mjölnir can be thrown (in ticks).")
                .define("Mjölnir Throw Threshold", 13);
        MJOLNIR_SHOOT_POWER = BUILDER.comment("The speed of a thrown Mjölnir when airborne.")
                .defineInRange("Mjölnir Shoot Power", 3.8f, 0f, 5f);
        MJOLNIR_THROW_COOLDOWN = BUILDER.comment("Cooldown period before Mjölnir can be thrown again (in ticks).")
                .define("Mjölnir Throw Cooldown", 141);
        MJOLNIR_RIPTIDE_COOLDOWN = BUILDER.comment("Cooldown period for Mjölnir's Riptide ability (in ticks).")
                .define("Mjölnir Riptide Cooldown", 30);

        THROWN_MJOLNIR_DAMAGE = BUILDER.comment("Damage dealt by a thrown Mjölnir.")
                .defineInRange("Thrown Mjölnir Damage", 25.0f, 0f, 1000f);
        THROWN_MJOLNIR_STRIKE_DAMAGE = BUILDER.comment("Damage dealt by the strike effect of a thrown Mjölnir.")
                .defineInRange("Thrown Mjölnir Strike Damage", 10.0f, 0f, 100f);
        THROWN_MJOLNIR_STRIKE_RADIUS = BUILDER.comment("Radius of the strike effect caused by a thrown Mjölnir (in ticks).")
                .defineInRange("Thrown Mjölnir Strike Radius", 5f, 0f, 50f);
        THROWN_MJOLNIR_STRIKE_FORCE = BUILDER.comment("Knockback force applied by the strike effect of a thrown Mjölnir.")
                .defineInRange("Thrown Mjölnir Strike Force", 1.5f, 0f, 100f);
        THROWN_MJOLNIR_RETURNING_SPEED = BUILDER.comment("Speed at which a thrown Mjölnir returns to the player.")
                .defineInRange("Thrown Mjölnir Returning Speed", 3.0f, 0f, 10f);
        THROWN_MJOLNIR_LIGHNTING_COUNT = BUILDER.comment("Number of lightning strikes summoned by a thrown Mjölnir upon hitting a target.")
                .defineInRange("Lightning Count", 4, 0, 32);

        LEVIATHAN_AXE_DAMAGE = BUILDER.comment("Melee damage dealt by the Leviathan Axe.")
                .defineInRange("Leviathan Axe Damage", 18.0f, 0f, 1000f);
        LEVIATHAN_AXE_ATTACK_SPEED = BUILDER.comment("Attack speed of the Leviathan Axe.")
                .defineInRange("Leviathan Axe Speed", -2.4f, -10f, 10f);
        LEVIATHAN_AXE_THROW_THRESHOLD = BUILDER.comment("The minimum using time before the Leviathan Axe can be thrown (in ticks).")
                .define("Leviathan Axe Throw Threshold", 10);
        LEVIATHAN_AXE_SHOOT_POWER = BUILDER.comment("The power of the Leviathan Axe when thrown, affecting its distance and damage.")
                .defineInRange("Leviathan Axe Shoot Power", 3f, 0f, 10f);
        LEVIATHAN_AXE_THROW_COOLDOWN = BUILDER.comment("Cooldown period before the Leviathan Axe can be thrown again (in ticks).")
                .define("Leviathan Axe Throw Cooldown", 0);
        LEVIATHAN_AXE_FREEZE_TIME_HIT = BUILDER.comment("Number of ticks a target is frozen upon being hit by the Leviathan Axe.")
                .define("Melee Freeze Time", 60);
        THROWN_LEVIATHAN_FROZEN_TICKS_HIT = BUILDER.comment("Number of ticks a target is frozen upon being hit by a thrown Leviathan Axe.")
                .define("Thrown Hit Freeze Time", 400);
        THROWN_LEVIATHAN_RETURNING_SPEED = BUILDER.comment("Speed at which a thrown Leviathan Axe returns to the player.")
                .defineInRange("Thrown Leviathan Returning Speed", 3.0f, 0f, 10f);

        DRAUPNIR_SPEAR_DAMAGE = BUILDER.comment("Melee damage dealt by the Draupnir Spear.")
                .defineInRange("Draupnir Spear Damage", 10.0f, 0f, 1000f);
        DRAUPNIR_SPEAR_ATTACK_SPEED = BUILDER.comment("Attack speed of the Draupnir Spear.")
                .defineInRange("Draupnir Spear Speed", -2f, -10f, 10f);
        DRAUPNIR_SPEAR_THROW_THRESHOLD = BUILDER.comment("Minimum charge time required to throw the Draupnir Spear (in ticks).")
                .define("Draupnir Spear Throw Threshold", 8);
        THROWN_DRAUPNIR_SPEAR_DAMAGE = BUILDER.comment("Damage dealt by a thrown Draupnir Spear.")
                .defineInRange("Thrown Draupnir Spear Damage", 8.0f, 0f, 1000f);
        DRAUPNIR_SPEAR_SHOOT_POWER = BUILDER.comment("Speed of a thrown Draupnir Spear.")
                .defineInRange("Draupnir Spear Shoot Power", 3.5f, 0f, 10f);
        DRAUPNIR_SPEARS_COUNT = BUILDER.comment("Number of spears that can be thrown before triggering an explosion.")
                .define("Draupnir Spears Count", 5);
        DRAUPNIR_SPEAR_EXPLOSION_DAMAGE = BUILDER.comment("Damage dealt by the explosion of a Draupnir Spear.")
                .defineInRange("Draupnir Spear Explosion Damage", 9.0f, 0f, 1000f);
        DRAUPNIR_SPEAR_EXPLOSION_RADIUS = BUILDER.comment("Radius of the explosion caused by the Draupnir Spear.")
                .defineInRange("Draupnir Spear Explosion Radius", 4.0f, 0f, 30f);

        DRAUGR_HP = BUILDER.comment("Draugr HP")
                .defineInRange("Draugr Health", 25.0f, 0.1f, 100f);
        DRAUGR_ARMOR = BUILDER.comment("Draugr Armor")
                .defineInRange("Draugr Armor", 8.0f, 0.1f, 100f);
        DRAUGR_KB_RESISTANCE = BUILDER.comment("Draugr Knockback Resistance")
                .defineInRange("Draugr KB Resistance", 0.35f, 0f, 1f);
        DRAUGR_DAMAGE = BUILDER.comment("Draugr Melee Damage")
                .defineInRange("Draugr Damage", 7.0f, 0f, 100f);
        DRAUGR_BURNING_TIME = BUILDER.comment("How many burning ticks will be added to a Draugr's target on hit")
                .define("Draugr Burning Time", 60);
        DRAUGR_FOLLOW_RANGE = BUILDER.comment("How far Draugr can see its target")
                .defineInRange("Draugr Follow Range", 35.0f, 0f, 100f);
        DRAUGR_SPEED_MODIFIER = BUILDER.comment("How fast Draugr will follow you")
                .defineInRange("Draugr Speed Modifier", 0.5f, 0f, 1f);

        HEL_WALKER_HP = BUILDER.comment("Hel Walker HP")
                .defineInRange("Hel Walker Health", 25.0f, 0f, 100f);
        HEL_WALKER_ARMOR = BUILDER.comment("Hel Walker Armor")
                .defineInRange("Hel Walker Armor", 8.0f, 0f, 100f);
        HEL_WALKER_KB_RESISTANCE = BUILDER.comment("Hel Walker Knockback Resistance")
                .defineInRange("Hel Walker KB Resistance", 0.35f, 0f, 1f);
        HEL_WALKER_DAMAGE = BUILDER.comment("Hel Walker Melee Damage")
                .defineInRange("Hel Walker Damage", 7.0f, 0f, 100f);
        HEL_WALKER_FREEZING_TIME = BUILDER.comment("How many frozen ticks will be added to a hel walker's target on hit")
                .define("Hel Walker Freeing Time", 80);
        HEL_WALKER_FOLLOW_RANGE = BUILDER.comment("How far Hel Walker can see its target")
                .defineInRange("Hel Walker Follow Range", 35.0f, 0f, 100f);
        HEL_WALKER_SPEED_MODIFIER = BUILDER.comment("How fast Hel Walker will follow you")
                .defineInRange("Hel Walker Speed Modifier", 0.35f, 0f, 1f);

        THOR_HP = BUILDER.comment("Thor HP")
                .defineInRange("Thor Health", 600.0f, 0f, 10000f);
        THOR_ARMOR = BUILDER.comment("Thor Armor")
                .defineInRange("Thor Armor", 20.0f, 0f, 100f);
        THOR_KB_RESISTANCE = BUILDER.comment("Thor Knockback Resistance")
                .defineInRange("Thor KB Resistance", 0.7f, 0f, 1f);
        THOR_DAMAGE = BUILDER.comment("Thor Melee Damage")
                .defineInRange("Thor Damage", 20.0f, 0f, 1000f);
        THOR_CLAP_DAMAGE = BUILDER.comment("Thor's Clap Attack Damage")
                .defineInRange("Thor Clap Damage", 27.0f, 0f, 1000f);
        THOR_CLAP_FORCE = BUILDER.comment("Legacy clap force. Thor's combat attacks now use the per-attack knockback distances instead.")
                .defineInRange("Thor Clap Force", 4.5f, 0f, 100f);
        THOR_CLAP_RADIUS = BUILDER.comment("Thor's Clap Attack Radius")
                .defineInRange("Thor Clap Radius", 6.0f, 0f, 20f);
        THOR_GROUND_DAMAGE = BUILDER.comment("Thor's Ground Attack Damage")
                .defineInRange("Thor Ground Damage", 37.0f, 0f, 100f);
        THOR_GROUND_FORCE = BUILDER.comment("Legacy ground force. Thor's combat attacks now use the per-attack knockback distances instead.")
                .defineInRange("Thor Ground Force", 8.0f, 0f, 30f);
        THOR_GROUND_RADIUS = BUILDER.comment("Thor's Ground Attack Radius")
                .defineInRange("Thor Ground Radius", 8.0f, 0f, 30f);
        THOR_SHIELD_KNOCKBACK_DISTANCE = BUILDER.comment("Shield knockback distance for player-owned Mjölnir and Thor's summoning shockwave. Thor's combat attacks use their own distances below. A Guardian Shield parry stops knockback completely.")
                .defineInRange("Thor Shield Knockback Distance", 15.0f, 0f, 30f);
        THOR_SHIELD_KNOCKBACK_HEIGHT = BUILDER.comment("How many blocks high that blow lifts them. In the air the throw keeps its momentum; along the ground friction and every bump stop it after a few blocks. 0 slides them along the ground.")
                .defineInRange("Thor Shield Knockback Height", 1.0f, 0f, 5f);
        THOR_MELEE_KNOCKBACK_DISTANCE = BUILDER.comment("Hammer melee knockback distance in blocks on level ground without movement input. Walls and terrain can shorten it.")
                .defineInRange("Thor Melee Knockback Distance", 3.0D, 0.0D, 30.0D);
        THOR_MELEE_SHIELD_KNOCKBACK_DISTANCE = BUILDER.comment("Hammer melee knockback distance when blocked with a shield.")
                .defineInRange("Thor Melee Shield Knockback Distance", 1.5D, 0.0D, 30.0D);
        THOR_MELEE_KNOCKBACK_HEIGHT = BUILDER.comment("Peak hammer melee launch height in blocks, for blocked and unblocked hits.")
                .defineInRange("Thor Melee Knockback Height", 0.2D, 0.0D, 5.0D);
        THOR_LEG_KNOCKBACK_DISTANCE = BUILDER.comment("Leg kick knockback distance in blocks.")
                .defineInRange("Thor Leg Knockback Distance", 5.5D, 0.0D, 30.0D);
        THOR_LEG_SHIELD_KNOCKBACK_DISTANCE = BUILDER.comment("Leg kick knockback distance when blocked with a shield.")
                .defineInRange("Thor Leg Shield Knockback Distance", 5.0D, 0.0D, 30.0D);
        THOR_LEG_KNOCKBACK_HEIGHT = BUILDER.comment("Peak leg kick launch height in blocks, for blocked and unblocked hits.")
                .defineInRange("Thor Leg Knockback Height", 0.2D, 0.0D, 5.0D);
        THOR_CLAP_KNOCKBACK_DISTANCE = BUILDER.comment("Clap knockback distance in blocks before distance falloff and the close-range bonus.")
                .defineInRange("Thor Clap Knockback Distance", 10.0D, 0.0D, 30.0D);
        THOR_CLAP_SHIELD_KNOCKBACK_DISTANCE = BUILDER.comment("Clap knockback distance when blocked, before falloff and the close-range bonus.")
                .defineInRange("Thor Clap Shield Knockback Distance", 10.0D, 0.0D, 30.0D);
        THOR_CLAP_KNOCKBACK_HEIGHT = BUILDER.comment("Peak clap launch height in blocks, for blocked and unblocked hits.")
                .defineInRange("Thor Clap Knockback Height", 1.0D, 0.0D, 5.0D);
        THOR_GROUND_KNOCKBACK_DISTANCE = BUILDER.comment("Ground slam knockback distance in blocks before distance falloff and the close-range bonus.")
                .defineInRange("Thor Ground Knockback Distance", 17.0D, 0.0D, 30.0D);
        THOR_GROUND_SHIELD_KNOCKBACK_DISTANCE = BUILDER.comment("Ground slam knockback distance when blocked, before falloff and the close-range bonus.")
                .defineInRange("Thor Ground Shield Knockback Distance", 15.0D, 0.0D, 30.0D);
        THOR_GROUND_KNOCKBACK_HEIGHT = BUILDER.comment("Peak ground slam launch height in blocks, for blocked and unblocked hits.")
                .defineInRange("Thor Ground Knockback Height", 2.5D, 0.0D, 5.0D);
        THOR_MJOLNIR_KNOCKBACK_DISTANCE = BUILDER.comment("Thor-owned thrown Mjolnir knockback distance in blocks. Direct hits and surrounding strikes stay at full strength.")
                .defineInRange("Thor Mjolnir Knockback Distance", 6.0D, 0.0D, 30.0D);
        THOR_MJOLNIR_SHIELD_KNOCKBACK_DISTANCE = BUILDER.comment("Thor-owned thrown Mjolnir knockback distance when blocked with the Guardian Shield.")
                .defineInRange("Thor Mjolnir Shield Knockback Distance", 5.0D, 0.0D, 30.0D);
        THOR_MJOLNIR_KNOCKBACK_HEIGHT = BUILDER.comment("Peak thrown Mjolnir launch height in blocks, for blocked and unblocked hits.")
                .defineInRange("Thor Mjolnir Knockback Height", 1.0D, 0.0D, 5.0D);
        THOR_AREA_MIN_DAMAGE_FACTOR = BUILDER.comment("Minimum damage fraction at the edge of clap/ground range. Falloff is linear from Thor's hitbox surface.")
                .defineInRange("Thor Area Minimum Damage Factor", 0.7D, 0.0D, 1.0D);
        THOR_AREA_MIN_KNOCKBACK_FACTOR = BUILDER.comment("Minimum horizontal knockback fraction at the edge of clap/ground range.")
                .defineInRange("Thor Area Minimum Knockback Factor", 0.85D, 0.0D, 1.0D);
        THOR_CLOSE_KNOCKBACK_RANGE = BUILDER.comment("Clap/ground proximity bonus range in blocks between hitbox surfaces. 0 disables the bonus.")
                .defineInRange("Thor Close Knockback Range", 1.5D, 0.0D, 5.0D);
        THOR_CLOSE_KNOCKBACK_MULTIPLIER = BUILDER.comment("Maximum horizontal clap/ground knockback multiplier at hitbox contact, fading to 1 at the proximity range.")
                .defineInRange("Thor Close Knockback Multiplier", 1.7D, 1.0D, 3.0D);
        THOR_FOLLOW_RANGE = BUILDER.comment("How far Thor can see its target")
                .defineInRange("Thor Follow Range", 50.0f, 0f, 1000f);
        THOR_SPEED_MODIFIER = BUILDER.comment("How fast Thor will follow you")
                .defineInRange("Thor Speed Modifier", 0.3f, 0f, 1f);
        THOR_XP_AMOUNT = BUILDER.comment("Amount of XP dropped upon Thor's death")
                .define("Thor XP Amount", 2000);

        BROK_AND_SINDRI_RESTOCK_TIME = BUILDER.comment("Brok and Sindri restock time in ticks")
                .define("Restock time", 18000);

        HACKSILVER_ORE_VEINS_PER_CHUNK = BUILDER.comment("How many Hacksilver Ore Veins spawns in one chunk (this uses commonOrePlacement")
                .define("Hacksilver Veins", 8);
        HACKSILVER_ORE_VEIN_SIZE = BUILDER.comment("How many Hacksilver Ore blocks spawn in one vein")
                .define("Hacksilver Vein Size", 7);

        SVARTALFHEIM_STEEL_ORE_VEINS_PER_CHUNK = BUILDER.comment("How many Svartalfheim Steel Ore Veins spawns in one chunk (this uses rareOrePlacement)")
                .define("Svartalfheim Steel Veins", 6);
        SVARTALFHEIM_STEEL_ORE_VEIN_SIZE = BUILDER.comment("How many Svartalfheim Steel Ore blocks spawn in one vein")
                .define("Svartalfheim Steel Vein Size", 7);

        ASGARDIAN_STEEL_ORE_VEINS_PER_CHUNK = BUILDER.comment("How many Asgardian Steel Ore Veins spawns in one chunk (this uses rareOrePlacement)")
                .define("Asgardian Steel Veins", 3);
        ASGARDIAN_STEEL_ORE_VEIN_SIZE = BUILDER.comment("How many Asgardian Steel Ore blocks spawn in one vein")
                .define("Asgardian Steel Vein Size", 7);

        NO_WATER_NEARBY_USE = BUILDER.comment("Whether the NoWaterNearby structures additionally require that no water biome (river, ocean, beach, swamp...) is within the distance below. Starts on open water are always rejected; this adds a stricter surface biome check with a small generation cost.")
                .define("Use NoWaterNearby structure type", false);

        NO_WATER_NEARBY_DISTANCE = BUILDER.comment("The minimum distance from the structure to the nearest water biome in blocks. Used only if the NoWaterNearby flag is true. The cost grows with the square of this value.")
                .defineInRange("NoWaterNearby Distance", 40, 0, 256);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
