package monsterMelee;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MonsterMeleeGame {

	public static void main(String[] args) {
		int width = 800;
		int height = 600;
		MonsterPanel panel1 = new MonsterPanel();
		JFrame frame = new JFrame("Monster Melee");
		frame.addMouseListener(panel1);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(Color.white);
		frame.add(panel1);
		frame.setVisible(true);
		panel1.run();
	}
}

class MonsterPanel extends JPanel implements MouseListener,
								  			 ActionListener {
	
	// MonsterPanel Attributes
	// Integers
	private int mouseX, mouseY;
	// Frame rate variables
	private int frames = 30;
	private double averageFrames;
	private int frameCount = 0;
	private int maxFrameCount = 30;
	// Booleans
	private boolean isRunning = true;
	private boolean isTitle = true;
	private boolean isSelect = false;
	private boolean isBattle = false;
	private boolean isUserTurn = true;
	private boolean userAlive = true;
	private boolean compAlive = true;
	// Array Lists
	private ArrayList<UserMonster> userMonsters = new ArrayList<UserMonster>();
	private ArrayList<ComputerMonster> compMonsters = new ArrayList<ComputerMonster>();
	// Objects
	private UserMonster userMonster;
	private ComputerMonster compMonster;
	// Sounds
	private Clip megaNoteClip = null;
	private Clip oofClip = null;
	private Clip victoryClip = null;
	private Clip miiChannelClip = null;
	private Clip hitClip = null;
	// Images
	private BufferedImage title = null;
	private BufferedImage select = null;
	private BufferedImage background = null;
	private BufferedImage userArachneedleImg = null;
	private BufferedImage userDougImg = null;
	private BufferedImage userStarManImg = null;
	private BufferedImage userAxeolotlImg = null;
	private BufferedImage userBlobertImg = null;
	private BufferedImage compArachneedleImg = null;
	private BufferedImage compDougImg = null;
	private BufferedImage compStarManImg = null;
	private BufferedImage compAxeolotlImg = null;
	private BufferedImage compBlobertImg = null;
	private BufferedImage random = null;
	// Time variables
	private long start;
	private long millis;
	private long wait;
	private long total = 0;
	private long target = 1000/frames;
	
	// MonsterPanel Constructor
	public MonsterPanel() {
		super();
	}
	
	// MonsterPanel Functions
	// Run function that runs the game loop; time and frames used to cap the game loop at 30 FPS.
	public void run() {
		preload();
		while(isRunning) {
			start = System.nanoTime();
			update();
			repaint();
			millis = (System.nanoTime() - start) / 1000000;
			wait = target - millis;
			try{
				Thread.sleep(wait);
			}
			catch(Exception e) {
			}
			total += System.nanoTime() - start;
			frameCount++;
			if(frameCount == maxFrameCount) {
				averageFrames = 1000.0/((total/frames)/1000000);
				frameCount = 0;
				total = 0;
			}
		}
	}
	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		if(isTitle) {
			g2D.drawImage(title, 0, 0, 800, 561, null);
		}
		if(isSelect) {
			g2D.drawImage(select, 0, 0, 800, 561, null);
			g2D.drawImage(compArachneedleImg, 0, 90, 255, 182, null);
			g2D.drawImage(compDougImg, 271, 90, 253, 182, null);
			g2D.drawImage(compStarManImg, 540, 90, 243, 182, null);
			g2D.drawImage(compAxeolotlImg, 0, 288, 255, 182, null);
			g2D.drawImage(compBlobertImg, 271, 288, 253, 182, null);
			g2D.drawImage(random, 540, 288, 243, 182, null);
			g2D.setFont(new Font("Comic Sans MS", Font.TRUETYPE_FONT, 24));
			g2D.setColor(new Color(100, 100, 100));
			g2D.drawString("Arachneedle", 50, 265);
			g2D.drawString("Doug", 368, 265);
			g2D.drawString("Star Man", 610, 265);
			g2D.drawString("Axeolotl", 70, 469);
			g2D.drawString("Blobert", 350, 469);
			g2D.drawString("Random", 615, 469);
		}
		else if(isBattle && userAlive && compAlive) {
			miiChannelClip.stop();
			g2D.drawImage(background, 0, 0, 800, 561, null);
			userMonster.display(g2D);
			compMonster.display(g2D);
			megaNoteClip.start();
		}
		else if(userAlive && compAlive == false && isBattle == false) {
			g2D.setColor(new Color(160, 160, 160));
			g2D.fillRect(100, 100, 600, 400);
			g2D.setColor(Color.black);
			g2D.setStroke(new BasicStroke(5));
			g2D.drawRect(100, 100, 600, 400);
			g2D.setFont(new Font("Comic Sans MS", Font.TRUETYPE_FONT, 24));
			g2D.drawString("Congrats! You have won the battle with", 175, 290);
			g2D.drawString(userMonster.getHealth() + " HP to spare!", 175, 320);
			g2D.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
			g2D.drawString("GAME OVER", 290, 425);
			g2D.drawString("HOORAH!", 310, 205);
			megaNoteClip.stop();
			victoryClip.start();
		}
		else if(compAlive && userAlive == false && isBattle == false){
			g2D.setColor(new Color(160, 160, 160));
			g2D.fillRect(100, 100, 600, 400);
			g2D.setColor(Color.black);
			g2D.setStroke(new BasicStroke(5));
			g2D.drawRect(100, 100, 600, 400);
			g2D.setFont(new Font("Comic Sans MS", Font.TRUETYPE_FONT, 24));
			g2D.drawString("Oh No! You have lost the battle by", 175, 290);
			g2D.drawString(compMonster.getHealth() + " HP! What a tragedy!", 175, 320);
			g2D.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
			g2D.drawString("GAME OVER", 290, 425);
			g2D.drawString("UH OH!", 320, 205);
			megaNoteClip.stop();
			oofClip.start();
		}
	}
	public void update() {
		// Title screen selection
		if(mouseX >= 138 && mouseX <= 702 && mouseY >= 311 && mouseY <= 376 && isTitle) {
			isTitle = false;
			isSelect = true;
			mouseX = 0;
			mouseY = 0;
		}
		// User picks Arachneedle
		else if(mouseX >= 0 && mouseX <= 264 && mouseY >= 120 && mouseY <= 303 && isSelect) {
			isSelect = false;
			isBattle = true;
			mouseX = 0;
			mouseY = 0;
			userMonster = userMonsters.get(0);
			compMonster = compMonsters.get((int)(Math.random()*compMonsters.size()));
		}
		// User picks Doug
		else if(mouseX >= 280 && mouseX <= 531 && mouseY >= 120 && mouseY <= 303 && isSelect) {
			isSelect = false;
			isBattle = true;
			mouseX = 0;
			mouseY = 0;
			userMonster = userMonsters.get(1);
			compMonster = compMonsters.get((int)(Math.random()*compMonsters.size()));
		}
		// User picks Star Man
		else if(mouseX >= 548 && mouseX <= 800 && mouseY >= 120 && mouseY <= 303 && isSelect) {
			isSelect = false;
			isBattle = true;
			mouseX = 0;
			mouseY = 0;
			userMonster = userMonsters.get(2);
			compMonster = compMonsters.get((int)(Math.random()*compMonsters.size()));
		}
		// User picks Axeolotl
		else if(mouseX >= 0 && mouseX <= 264 && mouseY >= 317 && mouseY <= 502 && isSelect) {
			isSelect = false;
			isBattle = true;
			mouseX = 0;
			mouseY = 0;
			userMonster = userMonsters.get(3);
			compMonster = compMonsters.get((int)(Math.random()*compMonsters.size()));
		}
		// User picks Blobert
		else if(mouseX >= 280 && mouseX <= 531 && mouseY >= 317 && mouseY <= 502 && isSelect) {
			isSelect = false;
			isBattle = true;
			mouseX = 0;
			mouseY = 0;
			userMonster = userMonsters.get(4);
			compMonster = compMonsters.get((int)(Math.random()*compMonsters.size()));
		}
		// User picks random
		else if(mouseX >= 548 && mouseX <= 800 && mouseY >= 317 && mouseY <= 502 && isSelect) {
			isSelect = false;
			isBattle = true;
			mouseX = 0;
			mouseY = 0;
			userMonster = userMonsters.get((int)(Math.random()*userMonsters.size()));
			compMonster = compMonsters.get((int)(Math.random()*compMonsters.size()));
		}
		// Battle Sequence
		else if(userAlive && compAlive) {
			if(isUserTurn) {
				// User uses top left move
				if(mouseX >= 377 && mouseX <= 552 && mouseY >= 494 && mouseY <= 521 && isBattle) {
					compMonster.setHealth(userMonster.getAttacks().get(0).getAttackDamage());
					userMonster.getAttacks().get(0).setAttackDamage((int)((Math.random()*24)));
					isUserTurn = false;
					hitClip.setFramePosition(0);
					hitClip.start();
					mouseX = 0;
					mouseY = 0;
					if(compMonster.getHealth() <= 0) {
						compAlive = false;
					}
				}
				// User uses bottom left move
				if(mouseX >= 377 && mouseX <= 552 && mouseY >= 541 && mouseY <= 569 && isBattle) {
					compMonster.setHealth(userMonster.getAttacks().get(1).getAttackDamage());
					userMonster.getAttacks().get(1).setAttackDamage((int)((Math.random()*24)));
					isUserTurn = false;
					hitClip.setFramePosition(0);
					hitClip.start();
					mouseX = 0;
					mouseY = 0;
					if(compMonster.getHealth() <= 0) {
						compAlive = false;
					}
				}
				// User uses top right move
				if(mouseX >= 578 && mouseX <= 753 && mouseY >= 494 && mouseY <= 521 && isBattle) {
					compMonster.setHealth(userMonster.getAttacks().get(2).getAttackDamage());
					userMonster.getAttacks().get(2).setAttackDamage((int)((Math.random()*24)));
					isUserTurn = false;
					hitClip.setFramePosition(0);
					hitClip.start();
					mouseX = 0;
					mouseY = 0;
					if(compMonster.getHealth() <= 0) {
						compAlive = false;
					}
				}
				// User uses bottom right move
				if(mouseX >= 578 && mouseX <= 753 && mouseY >= 541 && mouseY <= 569 && isBattle) {
					compMonster.setHealth(userMonster.getAttacks().get(3).getAttackDamage());
					userMonster.getAttacks().get(3).setAttackDamage((int)((Math.random()*24)));
					isUserTurn = false;
					hitClip.setFramePosition(0);
					hitClip.start();
					mouseX = 0;
					mouseY = 0;
					if(compMonster.getHealth() <= 0) {
						compAlive = false;
					}
				}
			}
			else {
				userMonster.setHealth(compMonster.getAttacks().get(0).getAttackDamage());
				compMonster.getAttacks().get(0).setAttackDamage((int)((Math.random()*24)));
				isUserTurn = true;
				if(userMonster.getHealth() <= 0) {
					userAlive = false;
				}
			}
		}
		else {
			if(userAlive) {
				compMonster.setHealth(compMonster.getHealth());
				isBattle = false;
			}
			else {
				userMonster.setHealth(userMonster.getHealth());
				isBattle = false;
			}
		}
	}
	public void preload() {
		// Loads all user images for potential user monsters
		try {
			userArachneedleImg = ImageIO.read(new File("resources/images/arachneedle/ArachneedleBack.png"));
			userDougImg = ImageIO.read(new File("resources/images/doug/DougBack.png"));
			userStarManImg = ImageIO.read(new File("resources/images/starMan/StarManBack.png"));
			userAxeolotlImg = ImageIO.read(new File("resources/images/axeolotl/AxeolotlBack.png"));
			userBlobertImg = ImageIO.read(new File("resources/images/blobert/BlobertBack.png"));
			compArachneedleImg = ImageIO.read(new File("resources/images/arachneedle/ArachneedleFront.png"));
			compDougImg = ImageIO.read(new File("resources/images/doug/DougFront.png"));
			compStarManImg = ImageIO.read(new File("resources/images/starMan/StarManFront.png"));
			compAxeolotlImg = ImageIO.read(new File("resources/images/axeolotl/AxeolotlFront.png"));
			compBlobertImg = ImageIO.read(new File("resources/images/blobert/BlobertFront.png"));
			background = ImageIO.read(new File("resources/images/Battlefield(Night).png"));
			title = ImageIO.read(new File("resources/images/TitleScreen.png"));
			select = ImageIO.read(new File("resources/images/SelectScreen.png"));
			random = ImageIO.read(new File("resources/images/Random.png"));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		// Arachneedle attacks
		Attack nitroNeedle = new Attack("Nitro Needle", 1, (int)((Math.random()*24)+1));
		Attack poisonPunch = new Attack("Poison Punch", 1, (int)((Math.random()*24)+1));
		Attack cosmicConfusion = new Attack("Cosmic Confusion", 1, (int)((Math.random()*24)+1));
		Attack webWhacker = new Attack("Web Whacker", 1, (int)((Math.random()*24)+1));
		ArrayList<Attack> arachneedleAttacks = new ArrayList<Attack>();
		arachneedleAttacks.add(nitroNeedle);
		arachneedleAttacks.add(poisonPunch);
		arachneedleAttacks.add(cosmicConfusion);
		arachneedleAttacks.add(webWhacker);
		// Doug attacks
		Attack zebedeebop = new Attack("Zebedeebop", 1, (int)((Math.random()*24)+1));
		Attack cuddleCut = new Attack("Cuddle Cut", 1, (int)((Math.random()*24)+1));
		Attack dawgPound = new Attack("Dawg Pound", 1, (int)((Math.random()*24)+1));
		Attack rottenTomatoToss = new Attack("Rotten Tomato Toss", 1, (int)((Math.random()*24)+1));
		ArrayList<Attack> dougAttacks = new ArrayList<Attack>();
		dougAttacks.add(zebedeebop);
		dougAttacks.add(cuddleCut);
		dougAttacks.add(dawgPound);
		dougAttacks.add(rottenTomatoToss);
		// Star Man attacks
		Attack cosmicBeam = new Attack("Cosmic Beam", 1, (int)((Math.random()*24)+1));
		Attack meteorShower = new Attack("Meteor Shower", 1, (int)((Math.random()*24)+1));
		Attack interstellarSmash = new Attack("Interstellar Smash", 1, (int)((Math.random()*24)+1));
		Attack hypernovaHit = new Attack("Hypernova Hit", 1, (int)((Math.random()*24)+1));
		ArrayList<Attack> starManAttacks = new ArrayList<Attack>();
		starManAttacks.add(cosmicBeam);
		starManAttacks.add(meteorShower);
		starManAttacks.add(interstellarSmash);
		starManAttacks.add(hypernovaHit);
		// Axeolotl attacks
		Attack axeToss = new Attack("Axe Toss", 1, (int)((Math.random()*24)+1));
		Attack headbutt = new Attack("Headbutt", 1, (int)((Math.random()*24)+1));
		Attack toxicTantrum = new Attack("Toxic Tantrum", 1, (int)((Math.random()*24)+1));
		Attack sandWhirl = new Attack("Sand Whirl", 1, (int)((Math.random()*24)+1));
		ArrayList<Attack> axeolotlAttacks = new ArrayList<Attack>();
		axeolotlAttacks.add(axeToss);
		axeolotlAttacks.add(headbutt);
		axeolotlAttacks.add(toxicTantrum);
		axeolotlAttacks.add(sandWhirl);
		// Blobert attacks
		Attack slam = new Attack("Slam", 1, (int)((Math.random()*24)+1));
		Attack slimeShot = new Attack("Slime Shot", 1, (int)((Math.random()*24)+1));
		Attack superSwallow = new Attack("Super Swallow", 1, (int)((Math.random()*24)+1));
		Attack rollnrock = new Attack("Roll N' Rock", 1, (int)((Math.random()*24)+1));
		ArrayList<Attack> blobertAttacks = new ArrayList<Attack>();
		blobertAttacks.add(slam);
		blobertAttacks.add(slimeShot);
		blobertAttacks.add(superSwallow);
		blobertAttacks.add(rollnrock);
		// Random attacks
		Attack randomAtk = new Attack("RANDOM", 1, (int)(Math.random()*24)+1);
		ArrayList<Attack> randomAttacks = new ArrayList<Attack>();
		randomAttacks.add(randomAtk);
		randomAttacks.add(randomAtk);
		randomAttacks.add(randomAtk);
		randomAttacks.add(randomAtk);
		// Initialize User Monsters
		UserMonster userArachneedle = new UserMonster("Arachneedle", 60, 355, 225, 100,
												      arachneedleAttacks, userArachneedleImg);
		userMonsters.add(userArachneedle);
		UserMonster userDoug = new UserMonster("Doug", 55, 330, 225, 100,
											   dougAttacks, userDougImg);
		userMonsters.add(userDoug);
		UserMonster userStarMan = new UserMonster("Star Man", 60, 320, 225, 100,
				   starManAttacks, userStarManImg);
		userMonsters.add(userStarMan);
		UserMonster userAxeolotl = new UserMonster("Axeolotl", 70, 290, 225, 100,
				   axeolotlAttacks, userAxeolotlImg);
		userMonsters.add(userAxeolotl);
		UserMonster userBlobert = new UserMonster("Blobert", 55, 310, 225, 100,
				   blobertAttacks, userBlobertImg);
		userMonsters.add(userBlobert);
		UserMonster userRandom = new UserMonster("RANDOM", 75, 300, 200, 100,
				   randomAttacks, random);
		userMonsters.add(userRandom);
		// Initialize Enemy Monsters
		ComputerMonster compArachneedle = new ComputerMonster("Arachneedle", 530, 105, 250, 100,
				arachneedleAttacks, compArachneedleImg);
		compMonsters.add(compArachneedle);
		ComputerMonster compDoug = new ComputerMonster("Doug", 540, 90, 225, 100,
				dougAttacks, compDougImg);
		compMonsters.add(compDoug);
		ComputerMonster compStarMan = new ComputerMonster("Star Man", 540, 80, 225, 100,
				starManAttacks, compStarManImg);
		compMonsters.add(compStarMan);
		ComputerMonster compAxeolotl = new ComputerMonster("Axeolotl", 560, 80, 200, 100,
				   axeolotlAttacks, compAxeolotlImg);
		compMonsters.add(compAxeolotl);
		ComputerMonster compBlobert = new ComputerMonster("Blobert", 543, 80, 225, 100,
				   blobertAttacks, compBlobertImg);
		compMonsters.add(compBlobert);
		// Audio preparations
		File oof = new File("resources/sounds/oof.wav");
		File megaNote = new File("resources/sounds/MegalovaniaNoteblocks.wav");
		File victory = new File("resources/sounds/Victory.wav");
		File miiChannel = new File("resources/sounds/MiiChannel8Bit.wav");
		File hit = new File("resources/sounds/Hit.wav");
		AudioInputStream oofStream = null;
		AudioInputStream megaNoteStream = null;
		AudioInputStream victoryStream = null;
		AudioInputStream miiChannelStream = null;
		AudioInputStream hitStream = null;
		try {
			oofStream = AudioSystem.getAudioInputStream(oof);
			megaNoteStream = AudioSystem.getAudioInputStream(megaNote);
			victoryStream = AudioSystem.getAudioInputStream(victory);
			miiChannelStream = AudioSystem.getAudioInputStream(miiChannel);
			hitStream = AudioSystem.getAudioInputStream(hit);
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
		AudioFormat oofFormat = oofStream.getFormat();
		AudioFormat megaNoteFormat = megaNoteStream.getFormat();
		AudioFormat victoryFormat = victoryStream.getFormat();
		AudioFormat miiChannelFormat = miiChannelStream.getFormat();
		AudioFormat hitFormat = hitStream.getFormat();
		DataLine.Info oofInfo = new DataLine.Info(Clip.class, oofFormat);
		DataLine.Info megaNoteInfo = new DataLine.Info(Clip.class, megaNoteFormat);
		DataLine.Info victoryInfo = new DataLine.Info(Clip.class, victoryFormat);
		DataLine.Info miiChannelInfo = new DataLine.Info(Clip.class, miiChannelFormat);
		DataLine.Info hitInfo = new DataLine.Info(Clip.class, hitFormat);
		try {
			oofClip = (Clip) AudioSystem.getLine(oofInfo);
			megaNoteClip = (Clip) AudioSystem.getLine(megaNoteInfo);
			victoryClip = (Clip) AudioSystem.getLine(victoryInfo);
			miiChannelClip = (Clip) AudioSystem.getLine(miiChannelInfo);
			hitClip = (Clip) AudioSystem.getLine(hitInfo);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		try {
			oofClip.open(oofStream);
			megaNoteClip.open(megaNoteStream);
			victoryClip.open(victoryStream);
			miiChannelClip.open(miiChannelStream);
			hitClip.open(hitStream);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		miiChannelClip.start();
	}
	// Methods implemented for the MouseListener.
	// I only use the clicking, so that's why that is the only one that does anything
	public void mouseClicked(MouseEvent event) {
		mouseX = event.getX();
		mouseY = event.getY();
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}

	public void actionPerformed(ActionEvent e) {
	}
}