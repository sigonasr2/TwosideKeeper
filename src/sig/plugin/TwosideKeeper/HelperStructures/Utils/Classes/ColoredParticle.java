package sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public enum ColoredParticle {
  
    MOB_SPELL("SPELL_MOB"), MOB_SPELL_AMBIENT("SPELL_MOB_AMBIENT"), RED_DUST("REDSTONE");
  
    private ColoredParticle(String name) {
        this.name = name;
    }
    String name;
    public void send(Location location, List<Player> players, int r, int g, int b) {
        ParticleEffect.valueOf(name).display(r/255, g / 255, b / 255, 1, 0, location, players);
    }
    public void send(Location location, int Distance, int r, int g, int b) {
    	ParticleEffect.valueOf(name).display(r/255, g / 255, b / 255, 1, 0, location, Distance);
    }

}