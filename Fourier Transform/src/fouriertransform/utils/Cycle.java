package fouriertransform.utils;

import java.awt.Color;
import java.awt.Graphics2D;

import fouriertransform.frame.Frame;

/**
 * @author Fabian
 */

public class Cycle {

	public double offsetX;
	public double offsetY;

	public double freq;
	public double amp;
	public double phase;

	public Cycle child;

	public Cycle(double offsetX, double offsetY, double freq, double amp, double phase) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;

		this.freq = freq;
		this.amp = amp;
		this.phase = phase;
	}

	public Cycle(double freq, double amp, double phase) {
		this.freq = freq;
		this.amp = amp;
		this.phase = phase;
	}

	public void addCycle(Cycle cycle) {
		this.child = cycle;
	}

	public void drawCycle(Graphics2D g2D, double xVal) {
		g2D.setColor(Color.WHITE);
		double radius = amp * 100; // 100 = height
		g2D.setColor(new Color(255, 255, 255, 100));
		g2D.drawOval((int) offsetX - (int) radius, (int) offsetY - (int) radius, (int) radius * 2, (int) radius * 2);
		double angle = (xVal * freq) + phase - Math.PI / 2;
		double x = radius * Math.cos(angle);
		double y = radius * Math.sin(angle);
		g2D.setColor(Color.WHITE);
		g2D.drawLine((int) offsetX, (int) offsetY, (int) (x + offsetX), (int) (y + offsetY));

		if (this.child == null) {
			g2D.setColor(new Color(255, 0, 0, 126));
			g2D.drawLine((int) (x + offsetX), (int) (y + offsetY), Frame.beginGraphX, (int) (y + offsetY));
			Frame.values.add(y + offsetY);
			if (Frame.values.size() > 900) {
				Frame.values.remove(0);
			}
		} else {
			child.offsetX = x + offsetX;
			child.offsetY = y + offsetY;
			child.drawCycle(g2D, xVal);
		}
	}
}
