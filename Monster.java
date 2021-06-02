package monsterMelee;

import java.awt.Image;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Monster {
	
	// Monster Attributes
	private String name;
	private int health, size, x, y, maxHealth;
	private ArrayList<Attack> attacks;
	private Image img;
	
	// Monster Constructor
	public Monster(String name, int x, int y, int size, int maxHealth,
				   ArrayList<Attack> attacks, Image img) {
		this.name = name;
		this.health = maxHealth;
		this.maxHealth = maxHealth;
		this.size = size;
		this.x = x;
		this.y = y;
		this.attacks = attacks;
		this.img = img;
	}
	
	// Monster Getters
	public String getName() {
		return name;
	}

	public int getHealth() {
		return health;
	}

	public int getSize() {
		return size;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public ArrayList<Attack> getAttacks() {
		return attacks;
	}

	public Image getImg() {
		return img;
	}
	
	public int setHealth(int damage) {
		health -= damage;
		return health;
	}
	
	public int setY(int yChange) {
		y += yChange;
		return y;
	}
	public int setX(int xChange) {
		x += xChange;
		return x;
	}
}