package monsterMelee;

public class Attack {
	
	// Attack Attributes
	private String name;
	private int damage;
	
	// Attack Constructor
	public Attack(String name, int minDamage, int damageRange) {
		this.name = name;
		this.damage = (int)((Math.random()*damageRange) + minDamage);
	}
	
	// Attack Getters & Setters
	public String getAttackName() {
		return name;
	}
	
	public int getAttackDamage() {
		return damage;
	}
	
	public int setAttackDamage(int newDamage) {
		damage = newDamage;
		return damage;
	}
}
