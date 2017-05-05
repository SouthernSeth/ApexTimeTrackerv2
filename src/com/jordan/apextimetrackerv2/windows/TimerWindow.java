package com.jordan.apextimetrackerv2.windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class TimerWindow {

	private JWindow frame;
	private JFrame window;
	private Timer timer;
	private Timer timer2;

	private JLabel timer_button;

	private boolean loading = false;

	private JButton start;
	private JLabel clock;
	
	private JLabel title;

	private JComboBox<String> preset_time;

	private int i = 0;

	public TimerWindow(final JFrame window) {
		this.window = window;
		frame = new JWindow();
		Dimension dim = new Dimension(425, 225);
		URL iconURL = getClass().getResource("/favicon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		frame.setIconImage(icon.getImage());
		frame.setSize(dim);
		frame.setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		Color color = new Color(240,240,240);
		panel.setBorder(new LineBorder(Color.BLACK));
		panel.setBackground(color);
		
		frame.setContentPane(panel);

		start = new JButton("Start");
		clock = new JLabel("00:00:00");

		preset_time = new JComboBox<String>();
		preset_time.addItem("Please select a time");
		preset_time.addItem("5 minutes");
		preset_time.addItem("10 minutes");
		preset_time.addItem("13 minutes");
		preset_time.addItem("15 minutes");
		preset_time.addItem("20 minutes");
		preset_time.addItem("30 minutes");
		preset_time.addItem("45 minutes");
		preset_time.addItem("1 hour");
		preset_time.addItem("1.25 hours");
		preset_time.addItem("1.5 hours");
		preset_time.addItem("2 hours");

		Font f = new Font("Arial", Font.BOLD, 24);
		clock.setFont(f);
		clock.setHorizontalAlignment(SwingConstants.CENTER);

		title = new JLabel("Timer");
		title.setFont(new Font("Arial", Font.BOLD, 32));
		title.setHorizontalAlignment(SwingConstants.CENTER);

		frame.getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0,0,25,0);
		frame.getContentPane().add(title, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets(0,0,25,0);
		frame.getContentPane().add(clock, gbc);

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.insets = new Insets(0,0,0,0);
		frame.getContentPane().add(preset_time, gbc);

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.insets = new Insets(15,0,0,0);
		frame.getContentPane().add(start, gbc);

		timer = new Timer();
		
		preset_time.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String str = (String) preset_time.getSelectedItem();
				if (str.equalsIgnoreCase("Please select a time")) {
					if (timer2 != null) {
						timer2.cancel();
					}
					start.setText("Start");
					clock.setText("00:00:00");
					clock.setForeground(Color.BLACK);
				} else if (str.equalsIgnoreCase("5 minutes")) {
					if (timer2 != null) {
						timer2.cancel();
					}
					start.setText("Start");
					clock.setText("00:05:00");
					clock.setForeground(Color.BLACK);
				} else if (str.equalsIgnoreCase("10 minutes")) {
					if (timer2 != null) {
						timer2.cancel();
					}
					start.setText("Start");
					clock.setText("00:10:00");
					clock.setForeground(Color.BLACK);
				} else if (str.equalsIgnoreCase("13 minutes")) {
					if (timer2 != null) {
						timer2.cancel();
					}
					start.setText("Start");
					clock.setText("00:13:00");
					clock.setForeground(Color.BLACK);
				} else if (str.equalsIgnoreCase("15 minutes")) {
					if (timer2 != null) {
						timer2.cancel();
					}
					start.setText("Start");
					clock.setText("00:15:00");
					clock.setForeground(Color.BLACK);
				} else if (str.equalsIgnoreCase("20 minutes")) {
					if (timer2 != null) {
						timer2.cancel();
					}
					start.setText("Start");
					clock.setText("00:20:00");
					clock.setForeground(Color.BLACK);
				} else if (str.equalsIgnoreCase("30 minutes")) {
					if (timer2 != null) {
						timer2.cancel();
					}
					start.setText("Start");
					clock.setText("00:30:00");
					clock.setForeground(Color.BLACK);
				} else if (str.equalsIgnoreCase("45 minutes")) {
					if (timer2 != null) {
						timer2.cancel();
					}
					start.setText("Start");
					clock.setText("00:45:00");
					clock.setForeground(Color.BLACK);
				} else if (str.equalsIgnoreCase("1 hour")) {
					if (timer2 != null) {
						timer2.cancel();
					}
					start.setText("Start");
					clock.setText("01:00:00");
					clock.setForeground(Color.BLACK);
				} else if (str.equalsIgnoreCase("1.25 hours")) {
					if (timer2 != null) {
						timer2.cancel();
					}
					start.setText("Start");
					clock.setText("01:15:00");
					clock.setForeground(Color.BLACK);
				} else if (str.equalsIgnoreCase("1.5 hours")) {
					if (timer2 != null) {
						timer2.cancel();
					}
					start.setText("Start");
					clock.setText("01:30:00");
					clock.setForeground(Color.BLACK);
				} else if (str.equalsIgnoreCase("2 hours")) {
					if (timer2 != null) {
						timer2.cancel();
					}
					start.setText("Start");
					clock.setText("02:00:00");
					clock.setForeground(Color.BLACK);
				}
			}
		});

		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (start.getText().equalsIgnoreCase("Stop")) {
					start.setText("Start");
					clock.setText("00:00:00");
					clock.setForeground(Color.BLACK);

					if (timer2 != null) {
						timer2.cancel();
					}
				} else {
					String str = (String) preset_time.getSelectedItem();

					if (str.equalsIgnoreCase("5 minutes")) {
						startTimer(5);
					} else if (str.equalsIgnoreCase("10 minutes")) {
						startTimer(10);
					} else if (str.equalsIgnoreCase("13 minutes")) {
						startTimer(13);
					} else if (str.equalsIgnoreCase("15 minutes")) {
						startTimer(15);
					} else if (str.equalsIgnoreCase("20 minutes")) {
						startTimer(20);
					} else if (str.equalsIgnoreCase("30 minutes")) {
						startTimer(30);
					} else if (str.equalsIgnoreCase("45 minutes")) {
						startTimer(45);
					} else if (str.equalsIgnoreCase("1 hour")) {
						startTimer(60);
					} else if (str.equalsIgnoreCase("1.25 hours")) {
						startTimer(75);
					} else if (str.equalsIgnoreCase("1.5 hours")) {
						startTimer(90);
					} else if (str.equalsIgnoreCase("2 hours")) {
						startTimer(120);
					}
				}
			}
		});

		window.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				if (timer_button != null) {
					if (timer_button.getName().equalsIgnoreCase("enabled")) {
						frame.setVisible(true);
					}
				}
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				if (timer_button != null) {
					if (timer_button.getName().equalsIgnoreCase("enabled")) {
						frame.setVisible(false);
					}
				}
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
			}

		});

		window.addComponentListener(new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent arg0) {
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				if (!loading) {
					frame.setLocation(window.getX() + 3, (window.getY() + window.getHeight()) - 2);
				}
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
			}
		});
	}

	public void startTimer(int minutes) {
		i = 1000 * 60 * minutes;

		if (timer2 != null) {
			timer2.cancel();
		}

		start.setText("Stop");

		timer2 = new Timer();
		timer2.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (i <= 0) {
					clock.setForeground(Color.BLACK);
					title.setForeground(Color.BLACK);
					clock.setText("00:00:00");
					start.setText("Start");
					timer2.cancel();
					return;
				}

				i = i - 1000;

				long hour = TimeUnit.MILLISECONDS.toHours( i );
				long minutes = TimeUnit.MILLISECONDS.toMinutes( i ) - TimeUnit.HOURS.toMinutes( TimeUnit.MILLISECONDS.toHours( i ));
				long seconds = TimeUnit.MILLISECONDS.toSeconds( i ) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes( i ));

				if (i < 20000) {
					if (!frame.isVisible()) {
						setVisible(true, timer_button);
					}

					Toolkit.getDefaultToolkit().beep();

					if (clock.getForeground() == Color.RED) {
						clock.setForeground(Color.YELLOW.darker());
						title.setForeground(Color.YELLOW.darker());
					} else {
						clock.setForeground(Color.RED);
						title.setForeground(Color.RED);
					}
				}

				String strHour = String.valueOf(hour);
				String strMinute = String.valueOf(minutes);
				String strSecond = String.valueOf(seconds);

				if (hour < 10) {
					strHour = "0" + strHour;
				}

				if (minutes < 10) {
					strMinute = "0" + strMinute;
				}

				if (seconds < 10) {
					strSecond = "0" + strSecond;
				}

				clock.setText(strHour + ":" + strMinute + ":" + strSecond);
			}
		}, 0, 1000);
	}

	public void setVisible(boolean visible, JLabel label) {
		timer.cancel();
		timer = new Timer();

		timer_button = label;

		if (visible) {
			window.setState(Frame.NORMAL);
			label.setText(Character.toString((char) 9660));
			label.setName("enabled");
			frame.setVisible(true);
			frame.setAlwaysOnTop(false);
			loading = true;
			frame.setLocation(window.getX() + 3, window.getY());
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					if (frame.getY() < ((window.getY() + window.getHeight()) - 2)) {
						frame.setLocation(window.getX() + 3, frame.getY() + 1);
						return;
					}
					timer.cancel();
					loading = false;
					frame.setAlwaysOnTop(true);
				}
			}, 0, 1);
		} else {
			label.setText(Character.toString((char) 9650));
			label.setName("disabled");
			loading = true;
			frame.setAlwaysOnTop(false);
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					if (frame.getY() != window.getY()) {
						frame.setLocation(window.getX() + 3, frame.getY() - 1);
						return;
					}
					frame.setVisible(false);
					loading = false;
					frame.setAlwaysOnTop(true);
					timer.cancel();
				}
			}, 0, 1);
		}
	}
}

