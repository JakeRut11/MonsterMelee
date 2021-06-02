package monsterMelee;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

public class ComputerMonster extends Monster {

	public ComputerMonster(String name, int x, int y, int size, int maxHealth,
			ArrayList<Attack> attacks, Image img) {
		super(name, x, y, size, maxHealth, attacks, img);
	}
	
	public void display(Graphics2D g2D) {
		int boxX = 230;
		int boxY = 135;
		int boxWidth = 250;
		int boxHeight = 112;
		// Draws the enemy monster
		g2D.drawImage(getImg(), getX(), getY(), getSize(), getSize(), null);
		// Draws the info box next to enemy monster
		g2D.setColor(new Color(160, 160, 160));
		g2D.fillRect(boxX, boxY, boxWidth, boxHeight);
		g2D.setColor(Color.black);
		g2D.setStroke(new BasicStroke(5));
		g2D.drawRect(boxX, boxY, boxWidth, boxHeight);
		g2D.setFont(new Font("Comic Sans MS", Font.TRUETYPE_FONT, 24));
		g2D.drawString(getName(), boxX+10, boxY+30);
		g2D.setFont(new Font("Comic Sans MS", Font.TRUETYPE_FONT, 16));
		g2D.drawString("HP: " + getHealth() + "/" + getMaxHealth(), boxX+10, boxY+55);
		g2D.setColor(new Color(0, 204, 0));
		g2D.fillRect(boxX+10, boxY+80, (int)((getHealth()/(double)getMaxHealth())*230), boxHeight/10);
		g2D.setStroke(new BasicStroke(2));
		g2D.setColor(Color.black);
		g2D.drawRect(boxX+10, boxY+80, 230, boxHeight/10);
		// Only used to test "hit box" of monster.
		// g2D.setColor(Color.red);
		// g2D.drawRect(x, y, size, size);
	}

}
