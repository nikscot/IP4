package edu.jsu.mcis;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class CustomWidget extends JPanel implements MouseListener {
    private java.util.List<ShapeObserver> observers;


    private final Color HEX_SELECTED_COLOR = Color.green;
    private final Color HEX_DEFAULT_COLOR = Color.white;

    private final Color OCT_SELECTED_COLOR = Color.red;
    private final Color OCT_DEFAULT_COLOR = Color.white;

    private boolean hexagonSelected;
    private Point[] hexagonVertices;

    private boolean octagonSelected;
    private Point[] octagonVertices;


    public CustomWidget() {
        observers = new ArrayList<>();
        Dimension dim = getPreferredSize();

        hexagonSelected = true;
        hexagonVertices = new Point[6];
        for(int i = 0; i < hexagonVertices.length; i++) {
            hexagonVertices[i] = new Point();
        }
        calculatePolygonVertices(
            hexagonVertices, dim.width, dim.height, 0, dim.width/3, dim.height/2
        );

        octagonSelected = false;
        octagonVertices = new Point[8];
        for(int i = 0; i < octagonVertices.length; i++) {
            octagonVertices[i] = new Point();
        }
        calculatePolygonVertices(
            octagonVertices, dim.width, dim.height, Math.PI * 0.125, dim.width - (dim.width/3), dim.height/2
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
        ShapeEvent event = new ShapeEvent(hexagonSelected, octagonSelected);

        for(ShapeObserver obs : observers) {
            obs.shapeChanged(event);
        }
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    private void calculatePolygonVertices(
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

        calculatePolygonVertices(
            hexagonVertices, width, height, 0, width/3, height/2
        );
        Shape shape = getPolygonShape(hexagonVertices);
        g2d.setColor(Color.black);
        g2d.draw(shape);

        if (hexagonSelected) {
            g2d.setColor(HEX_SELECTED_COLOR);
        } else {
            g2d.setColor(HEX_DEFAULT_COLOR);
        }
        g2d.fill(shape);

        calculatePolygonVertices(
            octagonVertices, width, height, Math.PI * 0.125, width - (width/3), height/2
        );
        shape = getPolygonShape(octagonVertices);
        g2d.setColor(Color.black);
        g2d.draw(shape);

        if (octagonSelected) {
            g2d.setColor(OCT_SELECTED_COLOR);
        } else {
            g2d.setColor(OCT_DEFAULT_COLOR);
        }
        g2d.fill(shape);
    }

    public void mouseClicked(MouseEvent event) {
        Shape shape = getPolygonShape(hexagonVertices);

        if(shape.contains(event.getX(), event.getY())) {
            hexagonSelected = !hexagonSelected;
            octagonSelected = !hexagonSelected;
            notifyObservers();
        } else {
            shape = getPolygonShape(octagonVertices);
            if(shape.contains(event.getX(), event.getY())) {
                octagonSelected = !octagonSelected;
                hexagonSelected = !octagonSelected;
                notifyObservers();
            }
        }

        repaint(getBounds());
    }
    public void mousePressed(MouseEvent event) {}
    public void mouseReleased(MouseEvent event) {}
    public void mouseEntered(MouseEvent event) {}
    public void mouseExited(MouseEvent event) {}

    public Shape getPolygonShape(Point[] vertices) {
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
            getPolygonShape(hexagonVertices),
            getPolygonShape(octagonVertices)
        };
    }

    public boolean isSelected() {
        return isHexagonSelected() || isOctagonSelected();
    }
    public boolean isHexagonSelected() { return hexagonSelected; }
    public boolean isOctagonSelected() { return octagonSelected; }



	public static void main(String[] args) {
		JFrame window = new JFrame("Custom Widget");
        window.add(new CustomWidget());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(300, 300);
        window.setVisible(true);
	}
}