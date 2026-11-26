package rpg.backend;

/**
 * 플레이어의 경험치, 레벨, 인벤토리를 관리한다.
 */
final class Player extends GameCharacter {
    private static final int POTION_HEALING = 4;

    final Inventory inventory = new Inventory();
    private int level = 1;
    private int experience;

    Player(String name) {
        super(name, 60);
        inventory.add(new Item("회복 물약", POTION_HEALING));
        inventory.add(new Item("회복 물약", POTION_HEALING));
    }

    int getLevel() {
        return level;
    }

    int getExperience() {
        return experience;
    }

    int getExperienceToLevelUp() {
        return level * 20;
    }

    void gainExperience(int amount) {
        experience += amount;
        while (experience >= getExperienceToLevelUp()) {
            experience -= getExperienceToLevelUp();
            level++;
            increaseMaxHp(10);
        }
    }
}
