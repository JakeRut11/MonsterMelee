package monsterMelee;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

public class UserMonster extends Monster {

	public UserMonster(String name, int x, int y, int size, int maxHealth,
			ArrayList<Attack> attacks, Image img) {
		super(name, x, y, size, maxHealth, attacks, img);
	}
	
	public void display(Graphics2D g2D) {
		int boxX = 345;
		int boxY = 383;
		int boxWidth = 425;
		int boxHeight = 169;
		// Draws user monster
		g2D.drawImage(getImg(), getX(), getY(), getSize(), getSize(), null);
		// Draws info box next to user monster
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
		g2D.fillRect(boxX+125, boxY+40, (int)((getHealth()/(double)getMaxHealth())*275),
					 boxHeight/10);
		g2D.setStroke(new BasicStroke(2));
		g2D.setColor(Color.black);
		g2D.drawRect(boxX+125, boxY+40, 275, boxHeight/10);
		// Starts drawing attack boxes
		for(int i=0; i<getAttacks().size(); i++) {
			if(i%2 == 0) {
				g2D.setColor(new Color(128, 128, 128));
				g2D.fillRect(boxX+25+(i*100), boxY+80, 175, boxHeight/6);
				g2D.setColor(Color.black);
				g2D.drawRect(boxX+25+(i*100), boxY+80, 175, boxHeight/6);
				g2D.setFont(new Font("Comic Sans MS", Font.TRUETYPE_FONT, 18));
				g2D.drawString(getAttacks().get(i).getAttackName(), boxX+30+(i*100), boxY+100);
			}
			else {
				g2D.setColor(new Color(128, 128, 128));
				g2D.fillRect(boxX+25+((i-1)*100), boxY+100+boxHeight/6, 175, boxHeight/6);
				g2D.setColor(Color.black);
				g2D.drawRect(boxX+25+((i-1)*100), boxY+100+boxHeight/6, 175, boxHeight/6);
				g2D.setFont(new Font("Comic Sans MS", Font.TRUETYPE_FONT, 18));
				g2D.drawString(getAttacks().get(i).getAttackName(), boxX+30+((i-1)*100), boxY+120+boxHeight/6);
			}
		}
		// Only used to test "hit box" of monster.
		// g2D.setColor(Color.red);
		// g2D.drawRect(getX(), getY(), getSize(), getSize());
	}
}
