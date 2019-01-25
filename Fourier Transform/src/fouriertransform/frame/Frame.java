package fouriertransform.frame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import fouriertransform.fourier.Fourier;
import fouriertransform.utils.Cycle;

/**
 * @author Fabian
 */

@SuppressWarnings("serial")
public class Frame extends JPanel implements ActionListener {

	JFrame frame;

	public Frame() {
		frame = new JFrame("Fourier Transform");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setSize(1200, 690);
		frame.setVisible(true);
		frame.add(this);

		init();
	}

	Timer timer;

	public static final int beginGraphX = 270;

	public static double xVal = 0;
	public static ArrayList<Double> function;
	public static ArrayList<Double> values;
	public static ArrayList<Cycle> cycles;

	int c = 1;

	private void init() {

		this.setLayout(null);

		function = new ArrayList<>();
		values = new ArrayList<>();
		cycles = new ArrayList<>();

		for (int i = 0; i < 600; i++) {
			function.add(0d);
		}
		updateCycleValue();

		this.setBackground(Color.BLACK);

		frame.addMouseMotionListener(new mouseListener());
		frame.addMouseListener(new mouseClick());

		timer = new Timer(1000 / 60, this);
		timer.start();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2D = (Graphics2D) g;

		g2D.setColor(Color.WHITE);
		g2D.fillRect(beginGraphX, 20, 900, 300);
		g2D.fillRect(beginGraphX, 340, 900, 300);

		g2D.setColor(Color.BLACK);
		g2D.drawLine(beginGraphX + 600, 20, beginGraphX + 600, 320);
		g2D.drawLine(beginGraphX, 170, beginGraphX + 900, 170);
		g2D.drawLine(beginGraphX, 490, beginGraphX + 900, 490);

		g2D.setStroke(new BasicStroke(2));
		if (cycles.size() > 0) {
			cycles.get(0).drawCycle(g2D, xVal);
		}

		g2D.setColor(new Color(255, 0, 0));

		for (int i = 1; i < 900; i++) {
			double y = function.get(i % 600);
			double ly = function.get((i - 1) % 600);
			g2D.drawLine(beginGraphX + i, (int) (-ly * 100) + 170, beginGraphX + i, (int) (-y * 100) + 170);
		}

		for (int i = values.size() - 1; i > 0; i--) {
			double x = values.get(i);
			double lx = values.get(i - 1);
			g2D.drawLine(beginGraphX + values.size() - i, (int) x, beginGraphX + values.size() - i, (int) lx);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		xVal -= Math.PI / 300;
		repaint();
	}

	public void updateCycleValue() {
		ArrayList<double[]> fourier = Fourier.fourier(function);
		fourier.sort(new Comparator<double[]>() {
			@Override
	        public int compare(double[] d2, double[] d1)
	        {
				if (d1[1] > d2[1]) {
					return 1;
				}
				if (d1[1] < d2[1]) {
					return -1;
				}
	            return  0;
	        }
		});
		cycles.clear();
		for (int i = 0; i < fourier.size(); i++) {
			double[] data = fourier.get(i);
			if (cycles.isEmpty()) {
				cycles.add(new Cycle(130, 490, data[0], data[1], data[2]));
			} else {
				cycles.add(new Cycle(data[0], data[1], data[2]));
				cycles.get(i - 1).addCycle(cycles.get(i));
			}
		}
	}

	private class mouseListener extends JComponent implements MouseMotionListener {

		int lastX = -1;

		@Override
		public void mouseDragged(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			if (x >= beginGraphX && x <= beginGraphX + 900 && y >= 20 + 27 && y <= 320 + 27) {
				if (lastX == -1 || Math.abs(lastX - x) <= 1) {
					int posX = x - beginGraphX - 3;
					int posY = -y + 170 + 27;
					if (posX >= 0) {
						function.set(posX % 600, posY / 100d);
					}
				} else if (lastX != -1 && Math.abs(lastX - x) != 0) {
					if (x > lastX) {
						int range = Math.abs(x - lastX);
						for (int i = 0; i <= range; i++) {
							int posX = x - beginGraphX - 3 - i;
							int posY = -y + 170 + 27;
							if (posX >= 0) {
								function.set(posX % 600, posY / 100d);
							}
						}
					}
					if (x < lastX) {
						int range = Math.abs(x - lastX);
						for (int i = 0; i <= range; i++) {
							int posX = x - beginGraphX - 3 + i;
							int posY = -y + 170 + 27;
							if (posX >= 0) {
								function.set(posX % 600, posY / 100d);
							}
						}
					}
				}
				lastX = x;
			} else {
				lastX = -1;
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			lastX = -1;
		}
	}
	
	private class mouseClick extends JComponent implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			updateCycleValue();
		}
		
	}
}
