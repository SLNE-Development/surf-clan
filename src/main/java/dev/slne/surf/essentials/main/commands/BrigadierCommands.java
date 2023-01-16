package dev.slne.surf.essentials.main.commands;

import dev.slne.surf.essentials.main.commands.cheat.*;
import dev.slne.surf.essentials.main.commands.cheat.gui.*;
import dev.slne.surf.essentials.main.commands.general.*;
import dev.slne.surf.essentials.main.commands.general.other.poll.PollCommand;
import dev.slne.surf.essentials.main.commands.general.other.poll.VoteCommand;
import dev.slne.surf.essentials.main.commands.general.other.troll.TrollManager;
import dev.slne.surf.essentials.main.commands.minecraft.*;

public class BrigadierCommands {
    public static void register(){
        //world-broadcast command
        BroadcastWorldCommand.register();
        //list command
        ListCommand.register();
        //lighting command
        LightningCommand.register();
        //kill command
        KillCommand.register();
        //enchant command
        EnchantCommand.register();
        //workbench command
        WorkbenchCommand.register();
        //anvil command
        AnvilCommand.register();
        //smithing table command
        SmithingTableCommand.register();
        //cartographytable command
        CartographyTableCommand.register();
        //grindtsone command
        GrindstoneCommand.register();
        //loom command
        LoomCommand.register();
        //stonecutter
        StonecutterCommand.register();
        //give command
        GiveCommand.register();
        //getpos command
        GetPosCommand.register();
        //troll command
        TrollManager.register();
        //hurt command
        HurtCommand.register();
        //bossbar command
        BossbarCommand.register();
        //op command
        OpCommand.register();
        //deop command
        DeopCommand.register();
        //effect command
        EffectCommand.register();
        //default game mode command
        DefaultGamemodeCommand.register();
        //difficulty command
        DifficultyCommand.register();
        //experience command
        ExperienceCommand.register();
        //forceload command
        ForceloadCommand.register();
        //game mode command
        GamemodeCommand.register();
        //gamerule command
        GameruleCommand.register();
        //particle command
        ParticleCommand.register();
        //fill stack command
        FillStackCommand.register();
        //fly command
        FlyCommand.register();
        //feed command
        FoodCommand.register();
        //godmode command
        GodmodeCommand.register();
        //heahl command
        HealCommand.register();
        //repair command
        RepairCommand.register();
        //speed command
        SpeedCommand.register();
        //time command
        TimeCommand.register();
        //weather command
        WeatherCommand.register();
        //poll command
        PollCommand.register();
        //vote command
        VoteCommand.register();
        //alert command
        AlertCommand.register();
        //book command
        BookCommand.register();
        //info command
        InfoCommand.register();
        //rule command
        RuleCommand.register();
        //spawner command
        SpawnerChangeCommand.register();
    }
}
