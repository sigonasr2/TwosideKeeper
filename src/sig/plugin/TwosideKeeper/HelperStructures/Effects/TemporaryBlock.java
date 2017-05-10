package sig.plugin.TwosideKeeper.HelperStructures.Effects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.APIUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.TextUtils;

public class TemporaryBlock {
	Material origmat;
	Material convertedmat;
	byte origdata;
	byte converteddata;
	int duration;
	String specialKey;
	Block bl;
	
	public Material getOriginalMaterial() {
		return origmat;
	}

	public Material getConvertedMaterial() {
		return convertedmat;
	}

	public byte getOriginalData() {
		return origdata;
	}

	public byte getConvertedData() {
		return converteddata;
	}

	public String getSpecialKey() {
		return specialKey;
	}

	public Block getBlock() {
		return bl;
	}

	public TemporaryBlock(Block b, Material convertedmat, int duration) {
		this.origmat = b.getType();
		this.convertedmat = convertedmat;
		this.origdata = b.getData();
		this.converteddata = b.getData();
		this.duration = duration;
		this.specialKey = "";
		this.bl=b;
		Block tempblock = getBlockToModify(b);
		if (tempblock!=null) {
			modifyBlock(this.bl);
		}
		setupStructureAndScheduler();
	}
	
	public TemporaryBlock(Block b, Material convertedmat, byte converteddata, int duration) {
		this.origmat = b.getType();
		this.convertedmat = convertedmat;
		this.origdata = b.getData();
		this.converteddata = converteddata;
		this.duration = duration;
		this.specialKey = "";
		this.bl=b;
		Block tempblock = getBlockToModify(b);
		if (tempblock!=null) {
			this.bl = tempblock;
			modifyBlock(this.bl);
		}
		setupStructureAndScheduler();
	}
	
	public TemporaryBlock(Block b, Material convertedmat, byte converteddata, int duration, String specialKey) {
		this.origmat = b.getType();
		this.convertedmat = convertedmat;
		this.origdata = b.getData();
		this.converteddata = converteddata;
		this.duration = duration;
		this.specialKey = specialKey;
		this.bl=b;
		Block tempblock = getBlockToModify(b);
		if (tempblock!=null) {
			this.bl = tempblock;
			modifyBlock(this.bl);
		}
		setupStructureAndScheduler();
	}

	private void modifyBlock(Block b) {
		if (b!=null && !APIUtils.isExplosionProof(b)) {
			b.setType(convertedmat);
			b.setData(converteddata);
		}
	}

	private void setupStructureAndScheduler() {
		if (bl!=null) {
			if (!TwosideKeeper.temporaryblocks.containsKey(getLocationKey(bl))) {
				AddToTemporaryBlockStructure(this);
			}
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, 
					new ReplaceBlockTask(getLocationKey(this)), 
					duration);
			//TwosideKeeper.log(TextUtils.outputHashmap(TwosideKeeper.temporaryblocks),0);
		}
	}
	
	private static void AddToTemporaryBlockStructure(TemporaryBlock tb) {
		if (tb.getSpecialKey().length()>0) {
			TwosideKeeper.temporaryblocks.put(getLocationKey(tb), tb);
		}
		TwosideKeeper.temporaryblocks.put(getLocationKey(tb,false), tb);
	}

	private static String getLocationKey(Block bl) {
		return getLocationKey(bl, "");
	}
	
	public static String getLocationKey(TemporaryBlock tb) {
		return tb.getLocationKey(tb.getBlock(), tb.getSpecialKey());
	}
	
	public static String getLocationKey(TemporaryBlock tb, boolean includeSpecialKey) {
		return tb.getLocationKey(tb.getBlock(), (includeSpecialKey)?tb.getSpecialKey():"");
	}

	private static String getLocationKey(Block bl, String specialKey) {
		if (bl!=null) {
			StringBuilder sb = new StringBuilder(Integer.toString(bl.getLocation().getBlockX()));
			sb.append("_");
			sb.append(Integer.toString(bl.getLocation().getBlockY()));
			sb.append("_");
			sb.append(Integer.toString(bl.getLocation().getBlockZ()));
			sb.append("_");
			sb.append(bl.getLocation().getWorld().getName());
			sb.append("_");
			sb.append(specialKey);
			return sb.toString();
		} else {
			return "";
		}
	}

	private Block getBlockToModify(Block b) {
		//Attempt to detect areas above and below ground to place this modified block.
		int yoffset = 0;
		while (yoffset<=5) {
			Block checkblock = b.getRelative(0, yoffset, 0);
			Block checkblock2 = b.getRelative(0, yoffset+1, 0);
			if (checkblock!=null && checkblock2!=null) {
				if ((checkblock.getType()==Material.AIR || checkblock.isLiquid() || checkblock.getType().isTransparent())) {
					yoffset = adjustYOffset(yoffset);
					//TwosideKeeper.log(Integer.toString(yoffset), 0);
				} else {
					if ((checkblock2.getType()==Material.AIR || checkblock2.isLiquid())) {
						this.origmat = checkblock.getType();
						this.origdata = checkblock.getData();
						return b.getRelative(0, yoffset, 0);
					} else {
						yoffset = adjustYOffset(yoffset);
					}
				}
			} else {
				yoffset = adjustYOffset(yoffset);
			}
		}
		return null;
	}

	private int adjustYOffset(int yoffset) {
		yoffset=(yoffset>-5 && yoffset<=0)?(yoffset-1):(yoffset<0)?1:(yoffset+1);
		return yoffset;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("TemporaryBlock{");
		sb.append("orig:(");sb.append(origmat);sb.append(",");sb.append(origdata);sb.append(")");
		sb.append(",");
		sb.append("converted:(");sb.append(convertedmat);sb.append(",");sb.append(converteddata);sb.append(")");
		sb.append(",");
		sb.append("duration:");sb.append(duration);
		sb.append(",");
		sb.append("key:");sb.append(specialKey);
		sb.append(",");
		sb.append("block:");sb.append(bl);
		sb.append("}");
		return sb.toString();
	}
	
	public static boolean isTemporaryBlock(Block b) {
		return TwosideKeeper.temporaryblocks.containsKey(TemporaryBlock.getLocationKey(b));
	}
	public static boolean isStandingOnSpecialBlock(Player p, String specialKey) {
		//return TwosideKeeper.temporaryblocks.containsKey(TemporaryBlock.getLocationKey(b));
		Block b = p.getLocation().getBlock();
		if (b!=null) {
			return TwosideKeeper.temporaryblocks.containsKey(TemporaryBlock.getLocationKey(b,specialKey));
		} else {
			return false;
		}
	}
	public static boolean isInRangeOfSpecialBlock(Player p, double range, String specialKey) {
		Block b = p.getLocation().getBlock();
		while (range-->0 && b.getLocation().getBlockY()>0) {
			if (TwosideKeeper.temporaryblocks.containsKey(TemporaryBlock.getLocationKey(b,specialKey))) {
				return true;
			} else {
				b = b.getRelative(0, -1, 0);
			}
		}
		return false;
	}
	public static TemporaryBlock getTemporaryBlock(Block b) {
		if (TwosideKeeper.temporaryblocks.containsKey(TemporaryBlock.getLocationKey(b))) {
			return TwosideKeeper.temporaryblocks.get(TemporaryBlock.getLocationKey(b));
		} else {
			return null;
		}
	}
	
	public static void createTemporaryBlockCircle(Location center, int radius, Material convertedmat, byte converteddata, int duration, String specialKey) {
		int width=0;
		for (int i=-radius;i<=radius;i++) {
			Location offset = center.clone().add(0,0,i);
			for (int j=-width;j<=width;j++) {
				Block b = offset.getBlock().getRelative(j, 0, 0);
				new TemporaryBlock(b, convertedmat, converteddata, duration, specialKey);
			}
			if (i<0) {
				width++;
			} else {
				width--;
			}
		}
	}
}
