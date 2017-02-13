package edu.jsu.mcis;

public class ShapeEvent {
    private boolean hexagon;
    private boolean octagon;


    public ShapeEvent() {
        this(false, false);
    }
    public ShapeEvent(boolean hexagon, boolean octagon) {
        this.hexagon = hexagon;
        this.octagon = octagon;
    }

    public boolean isHexagon() { return hexagon; }
    public boolean isOctagon() { return octagon; }
}