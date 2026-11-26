package rpg.backend;

/**
 * 플레이어와 몬스터가 공유하는 이름과 체력을 관리한다.
 */
abstract class GameCharacter {
    final String name;
    private int maxHp;
    private int hp;

    GameCharacter(String name, int maxHp) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
    }

    final int getHp() {
        return hp;
    }

    final int getMaxHp() {
        return maxHp;
    }

    final boolean isAlive() {
        return hp > 0;
    }

    final int takeDamage(int damage) {
        int actualDamage = Math.min(damage, hp);
        hp -= actualDamage;
        return actualDamage;
    }

    final int heal(int amount) {
        int restored = Math.min(amount, maxHp - hp);
        hp += restored;
        return restored;
    }

    final void increaseMaxHp(int extraHp) {
        maxHp += extraHp;
        heal(extraHp);
    }
}
