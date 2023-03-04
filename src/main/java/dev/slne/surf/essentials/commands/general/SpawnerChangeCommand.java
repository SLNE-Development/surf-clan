package dev.slne.surf.essentials.commands.general;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.slne.surf.api.utils.message.SurfColors;
import dev.slne.surf.essentials.SurfEssentials;
import dev.slne.surf.essentials.utils.EssentialsUtil;
import dev.slne.surf.essentials.utils.permission.Permissions;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static dev.slne.surf.api.utils.message.SurfColors.SUCCESS;

public class SpawnerChangeCommand{

    public static void register(){
        SurfEssentials.registerPluginBrigadierCommand("spawner", SpawnerChangeCommand::literal).setUsage("/spawner [<location>] [<entity>] [<minSpawnDelay>] [<maxSpawnDelay>] [<spawnRange>] [<requiredPlayerRange>]")
                .setDescription("Allows you to change the type, speed, radius, etc. of a spawner.");
    }

    private static void literal(LiteralArgumentBuilder<CommandSourceStack> literal) {
        literal.requires(sourceStack -> sourceStack.hasPermission(2, Permissions.SPAWNER_PERMISSION));

        literal.executes(context -> giveSpawner(context.getSource()));

        literal.then(Commands.argument("position", BlockPosArgument.blockPos())
                .executes(context -> querySpawner(context.getSource(), BlockPosArgument.getLoadedBlockPos(context, "position")))

                .then(Commands.argument("entity", ResourceArgument.resource(EssentialsUtil.buildContext(), Registries.ENTITY_TYPE))
                        .executes(context -> modifySpawner(context.getSource(), BlockPosArgument.getLoadedBlockPos(context, "position"), ResourceArgument.getSummonableEntityType(context, "entity").value(),
                                null, null, null, null))

                        .then(Commands.argument("minSpawnDelay", IntegerArgumentType.integer(1))
                                .executes(context -> modifySpawner(context.getSource(), BlockPosArgument.getLoadedBlockPos(context, "position"), ResourceArgument.getSummonableEntityType(context, "entity").value(),
                                        IntegerArgumentType.getInteger(context, "minSpawnDelay"), null, null, null))

                                .then(Commands.argument("maxSpawnDelay", IntegerArgumentType.integer(1))
                                        .executes(context -> modifySpawner(context.getSource(), BlockPosArgument.getLoadedBlockPos(context, "position"), ResourceArgument.getSummonableEntityType(context, "entity").value(),
                                                IntegerArgumentType.getInteger(context, "minSpawnDelay"), IntegerArgumentType.getInteger(context, "maxSpawnDelay"),
                                                null, null))

                                        .then(Commands.argument("spawnRange", IntegerArgumentType.integer(1, 50))
                                                .executes(context -> modifySpawner(context.getSource(), BlockPosArgument.getLoadedBlockPos(context, "position"), ResourceArgument.getSummonableEntityType(context, "entity").value(),
                                                        IntegerArgumentType.getInteger(context, "minSpawnDelay"), IntegerArgumentType.getInteger(context, "maxSpawnDelay"),
                                                        IntegerArgumentType.getInteger(context, "spawnRange"), null))

                                                .then(Commands.argument("requiredPlayerRange", IntegerArgumentType.integer(0))
                                                        .executes(context -> modifySpawner(context.getSource(), BlockPosArgument.getLoadedBlockPos(context, "position"), ResourceArgument.getSummonableEntityType(context, "entity").value(),
                                                                IntegerArgumentType.getInteger(context, "minSpawnDelay"), IntegerArgumentType.getInteger(context, "maxSpawnDelay"),
                                                                IntegerArgumentType.getInteger(context, "spawnRange"), IntegerArgumentType.getInteger(context, "requiredPlayerRange")))))))));
    }

    private static int giveSpawner(CommandSourceStack source) throws CommandSyntaxException {
        source.getPlayerOrException().getInventory().add(new net.minecraft.world.item.ItemStack(Items.SPAWNER));
        EssentialsUtil.sendSuccess(source, "Dir wurde ein spawner gegeben!");
        return 1;
    }

    private static int querySpawner(CommandSourceStack source, BlockPos blockPos) throws CommandSyntaxException {
        if (!isSpawner(source, blockPos)) return 0;

        BlockEntity blockEntity = source.getLevel().getBlockEntity(blockPos);
        SpawnerBlockEntity spawnerTileEntity = (SpawnerBlockEntity) blockEntity;
        BaseSpawner baseSpawner = spawnerTileEntity.getSpawner();

        String entityName = baseSpawner.nextSpawnData.getEntityToSpawn().getString("id");

        EssentialsUtil.sendSuccess(source, Component.text("Spawner", SurfColors.TERTIARY)
                .hoverEvent(HoverEvent.showText(Component.text("Entity: ", SurfColors.INFO)
                        .append(Component.text(entityName, SurfColors.TERTIARY))
                        .append(Component.newline())
                        .append(Component.text("minSpawnDelay: ", SurfColors.INFO)
                                .append(Component.text(baseSpawner.minSpawnDelay, SurfColors.TERTIARY)))
                        .append(Component.newline())
                        .append(Component.text("maxSpawnDelay: ", SurfColors.INFO)
                                .append(Component.text(baseSpawner.maxSpawnDelay, SurfColors.TERTIARY)))
                        .append(Component.newline())
                        .append(Component.text("spawnRange: ", SurfColors.INFO)
                                .append(Component.text(baseSpawner.spawnRange, SurfColors.TERTIARY)))
                        .append(Component.newline())
                        .append(Component.text("requiredPlayerRange: ", SurfColors.INFO)
                                .append(Component.text(baseSpawner.requiredPlayerRange, SurfColors.TERTIARY)))))
                .append(Component.text(" bei ", SurfColors.INFO)
                        .append(Component.text("%d %d %d".formatted(blockPos.getX(), blockPos.getY(), blockPos.getX()), SurfColors.TERTIARY))));
        return 1;

    }

    private static int modifySpawner(CommandSourceStack source, BlockPos blockPos, EntityType<?> type, Integer minSpawnDelay, Integer maxSpawnDelay, Integer spawnRange, Integer requiredPlayerRange) throws CommandSyntaxException {
        if (!isSpawner(source, blockPos)) return 0;

        BlockEntity blockEntity = source.getLevel().getBlockEntity(blockPos);
        BlockState oldState = blockEntity.getBlockState();
        SpawnerBlockEntity spawnerTileEntity = (SpawnerBlockEntity) blockEntity;
        BaseSpawner baseSpawner = spawnerTileEntity.getSpawner();

        spawnerTileEntity.setEntityId(type, RandomSource.create());

        baseSpawner.minSpawnDelay = minSpawnDelay == null ? baseSpawner.minSpawnDelay : minSpawnDelay;
        baseSpawner.maxSpawnDelay = maxSpawnDelay == null ? baseSpawner.maxSpawnDelay : maxSpawnDelay;
        baseSpawner.spawnRange = spawnRange == null ? baseSpawner.spawnRange : spawnRange;
        baseSpawner.requiredPlayerRange = requiredPlayerRange == null ? baseSpawner.requiredPlayerRange : requiredPlayerRange;

        source.getLevel().sendBlockUpdated(blockEntity.getBlockPos(), oldState, blockEntity.getBlockState(), 0);

        if (source.isPlayer()){
            EssentialsUtil.sendSuccess(source, Component.text("Der ", SUCCESS)
                    .append(Component.text("Spawner", SurfColors.TERTIARY)
                            .hoverEvent(HoverEvent.showText(Component.text("Entity: ", SurfColors.INFO)
                                    .append(PaperAdventure.asAdventure(type.getDescription()).colorIfAbsent(SurfColors.TERTIARY))
                                    .append(Component.newline())
                                    .append(Component.text("minSpawnDelay: ", SurfColors.INFO)
                                            .append(Component.text(baseSpawner.minSpawnDelay, SurfColors.TERTIARY)))
                                    .append(Component.newline())
                                    .append(Component.text("maxSpawnDelay: ", SurfColors.INFO)
                                            .append(Component.text(baseSpawner.maxSpawnDelay, SurfColors.TERTIARY)))
                                    .append(Component.newline())
                                    .append(Component.text("spawnRange: ", SurfColors.INFO)
                                            .append(Component.text(baseSpawner.spawnRange, SurfColors.TERTIARY)))
                                    .append(Component.newline())
                                    .append(Component.text("requiredPlayerRange: ", SurfColors.INFO)
                                            .append(Component.text(baseSpawner.requiredPlayerRange, SurfColors.TERTIARY))))))
                    .append(Component.text(" wurde erfolgreich geändert!")));
        }else {
            source.sendSuccess(net.minecraft.network.chat.Component.literal("The spawner was successfully modified"), false);
        }
        return 1;
    }

    private static boolean isSpawner(CommandSourceStack source, BlockPos blockPos) throws CommandSyntaxException {
        if (source.getLevel().getBlockIfLoaded(blockPos) != Blocks.SPAWNER){
            if (source.isPlayer()){
                EssentialsUtil.sendError(source, "An der Position befindet sich kein Spawner");
            }else {
                source.sendFailure(net.minecraft.network.chat.Component.literal("There is no spawner at this location."));
            }
            return false;
        }
        return true;
    }
}
