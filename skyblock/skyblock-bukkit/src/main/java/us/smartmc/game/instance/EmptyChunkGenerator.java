package us.smartmc.game.instance;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

public class EmptyChunkGenerator extends ChunkGenerator {

    @Override
    @Nonnull
    public ChunkData generateChunkData(@Nonnull World world, @Nonnull Random random, int chunkX, int chunkZ, @Nonnull BiomeGrid biome) {

        // For everyblock in the chunk set the biome to plains
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                biome.setBiome(x, z, Biome.PLAINS);
            }
        }
        return createChunkData(world);
    }

}
