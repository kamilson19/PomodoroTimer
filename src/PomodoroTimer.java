import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.awt.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;


public class PomodoroTimer implements ActionListener {
	
	JFrame frame = new JFrame();
	JButton startButton = new JButton("START");
	JButton minusButton = new JButton("-");
	JButton plusButton = new JButton("+");
	JButton resetButton = new JButton("RESET");
	JLabel timeLabel = new JLabel();
	int userLearnTime;
	int learnTime = 1500000;
	int breakTime = 300000;
	int timeLeft = learnTime;
	int seconds;
	int minutes;
	boolean started = false;
	boolean breakStatus = false;
	String seconds_string = String.format("%02d", seconds);
	String minutes_string = String.format("%02d", minutes);
	Font fontBebas;
	
	Timer timer = new Timer(1000, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (learnTime>0) {
				frame.getContentPane().setBackground(new Color(0x6cb38e));
				timeLeft = learnTime;
				learnTime-=1000;
				timeLeft-=1000;
				breakTime = 300000+1000; 				
				
			} else if (learnTime==0) {
				if (learnTime==0 & timeLeft==0) {
					playSound("sounds/sound_learn_finished.wav");
				}
				frame.getContentPane().setBackground(new Color(0x34baf5));
				breakStatus = true;
				timeLeft = breakTime;
				
				if (breakStatus) {
					timeLeft-=1000;
					breakTime-=1000;
				}
				if (breakTime==0) {
					learnTime = userLearnTime+1000;
					breakStatus = false;
					playSound("sounds/sound_break_finished.wav");
				}
			}
			minutes = (timeLeft/60000) % 60;
			seconds = (timeLeft/1000) % 60;
			seconds_string = String.format("%02d", seconds);
			minutes_string = String.format("%02d", minutes);
			timeLabel.setText(minutes_string+":"+seconds_string);
		}
	});
	
	
	PomodoroTimer() {
		try {
			InputStream is = getClass().getResourceAsStream("/fonts/BebasNeue-Regular.ttf");
			fontBebas = Font.createFont(Font.TRUETYPE_FONT, is);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fonts/BebasNeue-Regular.ttf")));
		} catch (IOException | FontFormatException e){
			System.out.println(e.getMessage());
		}
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400,300);
		frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(0xea5b61));
        frame.setResizable(false);
		
		//startButton 
		startButton.setBounds(100,150,200,50);
		startButton.setFocusable(false);
		startButton.addActionListener(this);
		startButton.setFont(new Font(fontBebas.getFamily(),Font.BOLD,26));
		startButton.setForeground(new Color(0x484848));
		
		//minusButton
		minusButton.setBounds(100,200,100,50);
		minusButton.setFocusable(false);
		minusButton.addActionListener(this);
		minusButton.setFont(new Font(fontBebas.getFamily(),Font.BOLD,26));
		minusButton.setForeground(new Color(0x484848));

		//plusButton
		plusButton.setBounds(200,200,100,50);
		plusButton.setFocusable(false);
		plusButton.addActionListener(this);
		plusButton.setFont(new Font(fontBebas.getFamily(),Font.BOLD,26));
		plusButton.setForeground(new Color(0x484848));
		
		//resetButton
		resetButton.setBounds(100,200,200,50);
		resetButton.setFocusable(false);
		resetButton.addActionListener(this);
		resetButton.setVisible(false);
		resetButton.setFont(new Font("Bebas Neue",Font.BOLD,26));
		resetButton.setForeground(new Color(0x484848));
		//label
		timeLabel.setBounds(50,50,300,120);
		timeLabel.setFont(new Font(fontBebas.getFamily(),Font.BOLD,110));
		timeLabel.setHorizontalAlignment(JLabel.CENTER);
		timeLabel.setOpaque(false);
		timeLabel.setForeground(Color.white);
		minutes = (timeLeft/60000) % 60;
		seconds = (timeLeft/1000) % 60;
		seconds_string = String.format("%02d", seconds);
		minutes_string = String.format("%02d", minutes);
		
		timeLabel.setText(minutes_string+":"+seconds_string);
		
		frame.add(timeLabel);
		frame.add(startButton);
		frame.add(plusButton);
		frame.add(minusButton);
		frame.add(resetButton);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==startButton) {
			playSound("sounds/start_sound.wav");
			if(started==false) {
				started=true;
				start();
				startButton.setText("PAUSE");
				//Checking if BREAK is running
				if (breakStatus==false) {
					frame.getContentPane().setBackground(new Color(0x6cb38e));
				} else {
					frame.getContentPane().setBackground(new Color(0x34baf5));
				}
				minusButton.setVisible(false);
				plusButton.setVisible(false);
				resetButton.setVisible(true);
				
			} else if (started==true) {
				started=false;
				pause();
				startButton.setText("START");
		        frame.getContentPane().setBackground(new Color(0xea5b61));
			}
		}
		if (e.getSource()==minusButton) {
			if (learnTime>60000 ) {
				playSound("sounds/sound_click.wav");
				learnTime-=60000;
			}
			userLearnTime = learnTime;
			timeLeft = learnTime;
			minutes = (timeLeft/60000) % 60;
			seconds = (timeLeft/1000) % 60;
			seconds_string = String.format("%02d", seconds);
			minutes_string = String.format("%02d", minutes);
			timeLabel.setText(minutes_string+":"+seconds_string);
		}
		if (e.getSource()==plusButton) {
			if (learnTime<1800000 ) {
				playSound("sounds/sound_click.wav");
				learnTime+=60000;
			}
			userLearnTime = learnTime;
			timeLeft = learnTime;
			minutes = (timeLeft/60000) % 60;
			seconds = (timeLeft/1000) % 60;
			seconds_string = String.format("%02d", seconds);
			minutes_string = String.format("%02d", minutes);
			timeLabel.setText(minutes_string+":"+seconds_string);
		}
		if (e.getSource()==resetButton) {
			playSound("sounds/sound_reset.wav");
			reset();
		}
	}
	
	public void start() {
		timer.start();
	}
	
	public void pause() {
		timer.stop();
	}
	
	public void reset() {
		timer.stop();
		learnTime = 1500000;
		breakTime = 300000;
		timeLeft = learnTime;
		started = false;
		breakStatus = false;
		minutes = (timeLeft/60000) % 60;
		seconds = (timeLeft/1000) % 60;
		seconds_string = String.format("%02d", seconds);
		minutes_string = String.format("%02d", minutes);
		timeLabel.setText(minutes_string+":"+seconds_string);
		resetButton.setVisible(false);
		plusButton.setVisible(true);
		minusButton.setVisible(true);
		startButton.setText("START");
		frame.getContentPane().setBackground(new Color(0xea5b61));
	}
	
	public static synchronized void playSound(String url) {
		  new Thread(new Runnable() {
		    public void run() {
		      try {
		        Clip clip = AudioSystem.getClip();
		        AudioInputStream inputStream = AudioSystem.getAudioInputStream(
		          Main.class.getResource(url));
		        clip.open(inputStream);
		        clip.start(); 
		      } catch (Exception e) {
		        System.err.println(e.getMessage());
		      }
		    }
		  }).start();
		}

}
