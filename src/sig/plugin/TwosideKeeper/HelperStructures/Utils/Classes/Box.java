package sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes;

public class Box {
	int x,y,z,w,h,d;
	public Box(int x, int y, int z, int w, int h, int d) {
		this.x=x;
		this.y=y;
		this.z=z;
		this.w=w;
		this.h=h;
		this.d=d;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getZ() {
		return z;
	}
	public int getWidth() {
		return w;
	}
	public int getHeight() {
		return h;
	}
	public int getDepth() {
		return d;
	}
	public Box clone() {
		return new Box(x,y,z,w,h,d);
	}
}
