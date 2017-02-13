package edu.jsu.mcis;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class CustomWidget extends JPanel implements MouseListener {
    private java.util.List<ShapeObserver> observers;


    private final Color HEXAGON_SELECTED_COLOR = Color.green;
    private final Color HEXAGON_DEFAULT_COLOR = Color.white;
    private final Color OCTAGON_SELECTED_COLOR = Color.red;
    private final Color OCTAGON_DEFAULT_COLOR = Color.white;

    private boolean hexagon;
    private Point[] hexagonVertices;

    private boolean octagon;
    private Point[] octagonVertices;


    public CustomWidget() {
        observers = new ArrayList<>();
        Dimension size = getPreferredSize();

        hexagon = true;
        hexagonVertices = new Point[6];
        for(int i = 0; i < hexagonVertices.length; i++) {
            hexagonVertices[i] = new Point();
        }
        calculateVertices(
            hexagonVertices, size.width, size.height, 0, size.width/3, size.height/2
        );

        octagon = false;
        octagonVertices = new Point[8];
        for(int i = 0; i < octagonVertices.length; i++) {
            octagonVertices[i] = new Point();
        }
        calculateVertices(
            octagonVertices, size.width, size.height, Math.PI * 0.125, size.width - (size.width/3), size.height/2
        );

        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseListener(this);
    }


    public void addShapeObserver(ShapeObserver observer) {
        if(!observers.contains(observer)) observers.add(observer);
    }
    public void removeShapeObserver(ShapeObserver observer) {
        observers.remove(observer);
    }
    private void notifyObservers() {
        ShapeEvent event = new ShapeEvent(hexagon, octagon);

        for(ShapeObserver obs : observers) {
            obs.shapeChanged(event);
        }
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    private void calculateVertices(
        Point[] vertices, int width, int height, double offsetRad, int offsetX, int offsetY
    ) {
        int size = Math.min(width, height) / 2;

        for(int i = 0; i < vertices.length; i++) {
            double rads = offsetRad + (i * (Math.PI / (vertices.length / 2)));
            double x = Math.cos(rads);
            double y = Math.sin(rads);
            vertices[i].setLocation(
                offsetX + (x * (size/4)),
                offsetY + (y * (size/4))
            );
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        int width = getWidth();
        int height = getHeight();

        calculateVertices(
            hexagonVertices, width, height, 0, width/3, height/2
        );
        Shape shape = getPolygonShapes(hexagonVertices);
        g2d.setColor(Color.black);
        g2d.draw(shape);

        if (hexagon) {
            g2d.setColor(HEXAGON_SELECTED_COLOR);
        } else {
            g2d.setColor(HEXAGON_DEFAULT_COLOR);
        }
        g2d.fill(shape);

        calculateVertices(
            octagonVertices, width, height, Math.PI * 0.125, width - (width/3), height/2
        );
        shape = getPolygonShapes(octagonVertices);
        g2d.setColor(Color.black);
        g2d.draw(shape);

        if (octagon) {
            g2d.setColor(OCTAGON_SELECTED_COLOR);
        } else {
            g2d.setColor(OCTAGON_DEFAULT_COLOR);
        }
        g2d.fill(shape);
    }

    public void mouseClicked(MouseEvent event) {
        Shape shape = getPolygonShapes(hexagonVertices);

        if(shape.contains(event.getX(), event.getY())) {
            hexagon = !hexagon;
            octagon = !hexagon;
            notifyObservers();
        } else {
            shape = getPolygonShapes(octagonVertices);
            if(shape.contains(event.getX(), event.getY())) {
                octagon = !octagon;
                hexagon = !octagon;
                notifyObservers();
            }
        }

        repaint(getBounds());
    }
    public void mousePressed(MouseEvent event) {}
    public void mouseReleased(MouseEvent event) {}
    public void mouseEntered(MouseEvent event) {}
    public void mouseExited(MouseEvent event) {}

    public Shape getPolygonShapes(Point[] vertices) {
        int[] x = new int[vertices.length];
        int[] y = new int[vertices.length];
        for(int i = 0; i < vertices.length; i++) {
            x[i] = vertices[i].x;
            y[i] = vertices[i].y;
        }
        Shape shape = new Polygon(x, y, vertices.length);
        return shape;
    }
    public Shape[] getShapes() {
        return new Shape[] {
            getPolygonShapes(hexagonVertices),
            getPolygonShapes(octagonVertices)
        };
    }

    public boolean isSelected() {
        return isHexagon() || isOctagon();
    }
    public boolean isHexagon() { return hexagon; }
    public boolean isOctagon() { return octagon; }



	public static void main(String[] args) {
		JFrame window = new JFrame("Custom Widget");
        window.add(new CustomWidget());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(300, 300);
        window.setVisible(true);
	}
}