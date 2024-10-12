package com.deriys.divinerelics.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class DivineRelicsCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Float> MOTOSIGNIR_RANGE;
    public static final ForgeConfigSpec.ConfigValue<Float> MOTOSIGNIR_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Float> MOTOSIGNIR_FORCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> MOTOSIGNIR_EFFECTS_DURATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> MOTOSIGNIR_COOLDOWN;

    public static final ForgeConfigSpec.ConfigValue<Integer> HEIMDALL_GAUNTLET_DURATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> HEIMDALL_GAUNTLET_COOLDOWN;

    public static final ForgeConfigSpec.ConfigValue<Integer> GUARDIAN_SHIELD_PARRY_WINDOW;

    public static final ForgeConfigSpec.ConfigValue<Float> MJOLNIR_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Float> MJOLNIR_ATTACK_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Integer> MJOLNIR_THROW_THRESHOLD;
    public static final ForgeConfigSpec.ConfigValue<Float> MJOLNIR_SHOOT_POWER;
    public static final ForgeConfigSpec.ConfigValue<Integer> MJOLNIR_THROW_COOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Integer> MJOLNIR_RIPTIDE_COOLDOWN;

    public static final ForgeConfigSpec.ConfigValue<Float> THROWN_MJOLNIR_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Float> THROWN_MJOLNIR_STRIKE_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Float> THROWN_MJOLNIR_STRIKE_RADIUS;
    public static final ForgeConfigSpec.ConfigValue<Float> THROWN_MJOLNIR_STRIKE_FORCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> THROWN_MJOLNIR_LIGHNTING_COUNT;
    public static final ForgeConfigSpec.ConfigValue<Float> THROWN_MJOLNIR_RETURNING_SPEED;

    public static final ForgeConfigSpec.ConfigValue<Float> LEVIATHAN_AXE_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Float> LEVIATHAN_AXE_ATTACK_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Integer> LEVIATHAN_AXE_THROW_THRESHOLD;
    public static final ForgeConfigSpec.ConfigValue<Float> LEVIATHAN_AXE_SHOOT_POWER;
    public static final ForgeConfigSpec.ConfigValue<Integer> LEVIATHAN_AXE_THROW_COOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Integer> LEVIATHAN_AXE_FREEZE_TIME_HIT;
    public static final ForgeConfigSpec.ConfigValue<Integer> THROWN_LEVIATHAN_FROZEN_TICKS_HIT;
    public static final ForgeConfigSpec.ConfigValue<Float> THROWN_LEVIATHAN_RETURNING_SPEED;

    public static final ForgeConfigSpec.ConfigValue<Float> DRAUPNIR_SPEAR_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Float> DRAUPNIR_SPEAR_SHOOT_POWER;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRAUPNIR_SPEAR_THROW_THRESHOLD;
    public static final ForgeConfigSpec.ConfigValue<Float> THROWN_DRAUPNIR_SPEAR_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Float> DRAUPNIR_SPEAR_ATTACK_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRAUPNIR_SPEARS_COUNT;
    public static final ForgeConfigSpec.ConfigValue<Float> DRAUPNIR_SPEAR_EXPLOSION_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Float> DRAUPNIR_SPEAR_EXPLOSION_RADIUS;

    public static final ForgeConfigSpec.ConfigValue<Float> DRAUGR_HP;
    public static final ForgeConfigSpec.ConfigValue<Float> DRAUGR_ARMOR;
    public static final ForgeConfigSpec.ConfigValue<Float> DRAUGR_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRAUGR_BURNING_TIME;
    public static final ForgeConfigSpec.ConfigValue<Float> DRAUGR_KB_RESISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Float> DRAUGR_FOLLOW_RANGE;
    public static final ForgeConfigSpec.ConfigValue<Float> DRAUGR_SPEED_MODIFIER;

    public static final ForgeConfigSpec.ConfigValue<Float> HEL_WALKER_HP;
    public static final ForgeConfigSpec.ConfigValue<Float> HEL_WALKER_ARMOR;
    public static final ForgeConfigSpec.ConfigValue<Float> HEL_WALKER_KB_RESISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Float> HEL_WALKER_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Integer> HEL_WALKER_FREEZING_TIME;
    public static final ForgeConfigSpec.ConfigValue<Float> HEL_WALKER_FOLLOW_RANGE;
    public static final ForgeConfigSpec.ConfigValue<Float> HEL_WALKER_SPEED_MODIFIER;

    public static final ForgeConfigSpec.ConfigValue<Float> THOR_HP;
    public static final ForgeConfigSpec.ConfigValue<Float> THOR_ARMOR;
    public static final ForgeConfigSpec.ConfigValue<Float> THOR_KB_RESISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Float> THOR_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Float> THOR_FOLLOW_RANGE;
    public static final ForgeConfigSpec.ConfigValue<Float> THOR_SPEED_MODIFIER;
    public static final ForgeConfigSpec.ConfigValue<Float> THOR_CLAP_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Float> THOR_CLAP_FORCE;
    public static final ForgeConfigSpec.ConfigValue<Float> THOR_CLAP_RADIUS;
    public static final ForgeConfigSpec.ConfigValue<Float> THOR_GROUND_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Float> THOR_GROUND_FORCE;
    public static final ForgeConfigSpec.ConfigValue<Float> THOR_GROUND_RADIUS;

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

        MOTOSIGNIR_RANGE = BUILDER.comment("Motosignir Sound Wave Range in Blocks")
                .define("Motosignir Radius", 10f);
        MOTOSIGNIR_DAMAGE = BUILDER.comment("Motosignir Sound Wave Damage")
                .define("Motosignir Damage", 20.0f);
        MOTOSIGNIR_FORCE = BUILDER.comment("Motosignir Sound Wave Knockback Force")
                .define("Motosignir Force", 1.1f);
        MOTOSIGNIR_EFFECTS_DURATION = BUILDER.comment("Duration of the motosignir's stun effects in ticks")
                .define("Motosignir Duration", 20);
        MOTOSIGNIR_COOLDOWN = BUILDER.comment("Motosignir Cooldown in Ticks")
                .define("Motosignir Cooldown", 200);

        HEIMDALL_GAUNTLET_DURATION = BUILDER.comment("Bifrost Protection Duration in Ticks")
                .define("Heimdall's Gauntlet Duration", 300);
        HEIMDALL_GAUNTLET_COOLDOWN = BUILDER.comment("Heimdall's Gauntlet Cooldown in Ticks")
                .define("Heimdall's Gauntlet Cooldown", 1200);

        GUARDIAN_SHIELD_PARRY_WINDOW = BUILDER.comment("Time between shield's opening and hit to successfully parry the attack in ticks")
                .define("Parry Window Duration", 12);

        MJOLNIR_DAMAGE = BUILDER.comment("Mjölnir Melee Damage")
                .define("Mjölnir Damage", 25.0f);
        MJOLNIR_ATTACK_SPEED = BUILDER.comment("Mjölnir Attack Speed")
                .define("Mjölnir Speed", -3.25f);
        MJOLNIR_THROW_THRESHOLD = BUILDER.comment("Mjölnir Throw Threshold in Ticks")
                .define("Mjölnir Throw Threshold", 13);
        MJOLNIR_SHOOT_POWER = BUILDER.comment("Mjölnir Shoot Power")
                .define("Mjölnir Shoot Power", 3.8f);
        MJOLNIR_THROW_COOLDOWN = BUILDER.comment("Mjölnir Throw Cooldown in Ticks")
                .define("Mjölnir Throw Cooldown", 141);
        MJOLNIR_RIPTIDE_COOLDOWN = BUILDER.comment("Mjölnir Riptide Cooldown in Ticks")
                .define("Mjölnir Riptide Cooldown", 30);

        THROWN_MJOLNIR_DAMAGE = BUILDER.comment("Thrown Mjölnir Damage")
                .define("Thrown Mjölnir Damage", 25.0f);
        THROWN_MJOLNIR_STRIKE_DAMAGE = BUILDER.comment("Thrown Mjölnir Strike Damage")
                .define("Thrown Mjölnir Strike Damage", 10.0f);
        THROWN_MJOLNIR_STRIKE_RADIUS = BUILDER.comment("Thrown Mjölnir Strike Radius in Blocks")
                .define("Thrown Mjölnir Strike Radius", 5f);
        THROWN_MJOLNIR_STRIKE_FORCE = BUILDER.comment("Thrown Mjölnir Strike Force")
                .define("Thrown Mjölnir Strike Force", 1.5f);
        THROWN_MJOLNIR_RETURNING_SPEED = BUILDER.comment("Thrown Mjölnir Returning Speed")
                .define("Thrown Mjölnir Returning Speed", 3.0f);
        THROWN_MJOLNIR_LIGHNTING_COUNT = BUILDER.comment("How many lightnings a Thrown Mjlölnir summons on hit")
                .define("Lightning Count", 4);

        LEVIATHAN_AXE_DAMAGE = BUILDER.comment("Leviathan Axe Melee Damage")
                .define("Leviathan Axe Damage", 18.0f);
        LEVIATHAN_AXE_ATTACK_SPEED = BUILDER.comment("Leviathan Axe Attack Speed")
                .define("Leviathan Axe Speed", -2.4f);
        LEVIATHAN_AXE_THROW_THRESHOLD = BUILDER.comment("Leviathan Axe Throw Threshold in Ticks")
                .define("Leviathan Axe Throw Threshold", 10);
        LEVIATHAN_AXE_SHOOT_POWER = BUILDER.comment("Leviathan Axe Shoot Power")
                .define("Leviathan Axe Shoot Power", 3f);
        LEVIATHAN_AXE_THROW_COOLDOWN = BUILDER.comment("Leviathan Axe Throw Cooldown in Ticks")
                .define("Leviathan Axe Throw Cooldown", 0);
        LEVIATHAN_AXE_FREEZE_TIME_HIT = BUILDER.comment("How many ticks of freezing adds to a target on a melee hit of Leviathan Axe")
                .define("Melee Freeze Time", 60);
        THROWN_LEVIATHAN_FROZEN_TICKS_HIT = BUILDER.comment("How many ticks of freezing a target gets on a hit of Thrown Leviathan Axe")
                .define("Thrown Hit Freeze Time", 400);
        THROWN_LEVIATHAN_RETURNING_SPEED = BUILDER.comment("Thrown Leviathan Returning Speed")
                .define("Thrown Leviathan Returning Speed", 3.0f);

        DRAUPNIR_SPEAR_DAMAGE = BUILDER.comment("Draupnir Spear Melee Damage")
                .define("Draupnir Spear Damage", 10.0f);
        DRAUPNIR_SPEAR_ATTACK_SPEED = BUILDER.comment("Draupnir Spear Attack Speed")
                .define("Draupnir Spear Speed", -2f);
        DRAUPNIR_SPEAR_THROW_THRESHOLD = BUILDER.comment("How fast you can throw spears")
                .define("Draupnir Spear Throw Threshold", 8);
        THROWN_DRAUPNIR_SPEAR_DAMAGE = BUILDER.comment("Damage of a thrown draupnir spear")
                .define("Thrown Draupnir Spear Damage", 7.0f);
        DRAUPNIR_SPEAR_SHOOT_POWER = BUILDER.comment("Speed of a thrown draupnir spear")
                .define("Draupnir Spear Shoot Power", 3.5f);
        DRAUPNIR_SPEARS_COUNT = BUILDER.comment("How many spears you can throw before explosion")
                .define("Draupnir Spears Count", 5);
        DRAUPNIR_SPEAR_EXPLOSION_DAMAGE = BUILDER.comment("Draupnir Spear explosion damage")
                .define("Draupnir Spear Explosion Damage", 9.0f);
        DRAUPNIR_SPEAR_EXPLOSION_RADIUS = BUILDER.comment("Draupnir Spear explosion radius")
                .define("Draupnir Spear Explosion Radius", 4.0f);

        DRAUGR_HP = BUILDER.comment("Draugr HP")
                .define("Draugr Health", 25.0f);
        DRAUGR_ARMOR = BUILDER.comment("Draugr Armor")
                .define("Draugr Armor", 8.0f);
        DRAUGR_KB_RESISTANCE = BUILDER.comment("Draugr Knockback Resistance")
                .define("Draugr KB Resistance", 0.35f);
        DRAUGR_DAMAGE = BUILDER.comment("Draugr Melee Damage")
                .define("Draugr Damage", 7.0f);
        DRAUGR_BURNING_TIME = BUILDER.comment("How many burning ticks will be added to a draugr's target on hit")
                .define("Draugr Burning Time", 60);
        DRAUGR_FOLLOW_RANGE = BUILDER.comment("How far a draugr can see its target")
                .define("Draugr Follow Range", 35.0f);
        DRAUGR_SPEED_MODIFIER = BUILDER.comment("How fast draugr will follow you")
                .define("Draugr Speed Modifier", 0.5f);

        HEL_WALKER_HP = BUILDER.comment("Hel Walker HP")
                .define("Hel Walker Health", 25.0f);
        HEL_WALKER_ARMOR = BUILDER.comment("Hel Walker Armor")
                .define("Hel Walker Armor", 8.0f);
        HEL_WALKER_KB_RESISTANCE = BUILDER.comment("Hel Walker Knockback Resistance")
                .define("Hel Walker KB Resistance", 0.35f);
        HEL_WALKER_DAMAGE = BUILDER.comment("Hel Walker Melee Damage")
                .define("Hel Walker Damage", 7.0f);
        HEL_WALKER_FREEZING_TIME = BUILDER.comment("How many frozen ticks will be added to a hel walker's target on hit")
                .define("Hel Walker Freeing Time", 80);
        HEL_WALKER_FOLLOW_RANGE = BUILDER.comment("How far a Hel Walker can see its target")
                .define("Hel Walker Follow Range", 35.0f);
        HEL_WALKER_SPEED_MODIFIER = BUILDER.comment("How fast Hel Walker will follow you")
                .define("Hel Walker Speed Modifier", 0.35f);

        THOR_HP = BUILDER.comment("Thor HP")
                .define("Thor Health", 600.0f);
        THOR_ARMOR = BUILDER.comment("Thor Armor")
                .define("Thor Armor", 20.0f);
        THOR_KB_RESISTANCE = BUILDER.comment("Thor Knockback Resistance")
                .define("Thor KB Resistance", 0.7f);
        THOR_DAMAGE = BUILDER.comment("Thor Melee Damage")
                .define("Thor Damage", 20.0f);
        THOR_CLAP_DAMAGE = BUILDER.comment("Thor's Clap Attack Damage")
                .define("Thor Clap Damage", 7.0f);
        THOR_CLAP_FORCE = BUILDER.comment("Thor's Clap Attack Knockback Force")
                .define("Thor Clap Force", 3.5f);
        THOR_CLAP_RADIUS = BUILDER.comment("Thor's Clap Attack Radius")
                .define("Thor Clap Radius", 6.0f);
        THOR_GROUND_DAMAGE = BUILDER.comment("Thor's Ground Attack Damage")
                .define("Thor Ground Damage", 17.0f);
        THOR_GROUND_FORCE = BUILDER.comment("Thor's Ground Attack Knockback Force")
                .define("Thor Ground Force", 5.0f);
        THOR_GROUND_RADIUS = BUILDER.comment("Thor's Ground Attack Radius")
                .define("Thor Ground Radius", 8.0f);
        THOR_FOLLOW_RANGE = BUILDER.comment("How far Thor can see its target")
                .define("Thor Follow Range", 50.0f);
        THOR_SPEED_MODIFIER = BUILDER.comment("How fast Thor will follow you")
                .define("Thor Speed Modifier", 0.3f);

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

        NO_WATER_NEARBY_USE = BUILDER.comment("Whether the structures are going to use NoWaterNearby type of structure to avoid spawning near or in the water. Results in a significantly slower generation speed.")
                .define("Use NoWaterNearby structure type", false);

        NO_WATER_NEARBY_DISTANCE = BUILDER.comment("The minimum distance from the structure to the nearest water biome in blocks. Used only if the NoWaterNearby flag is true. The bigger the value the slower is the generation.")
                .define("NoWaterNearby Distance", 40);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
