package rpg.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 한 층의 턴제 전투에서 공격과 방어 주사위를 굴리고 피해를 처리한다.
 */
final class Battle {
    private static final int MAX_ROLL = 6;

    final Monster monster;
    private final Player player;

    Battle(Player player, Monster monster) {
        this.player = player;
        this.monster = monster;
    }

    ArrayList<String> attack() {
        ArrayList<String> events = new ArrayList<>();
        int attackRoll = roll();
        int levelBonus = player.getLevel() - 1;
        int damage = monster.takeDamage(attackRoll + levelBonus);
        events.add(String.format("공격 주사위 %d + 레벨 보너스 %d · %s에게 %d 피해",
                attackRoll, levelBonus, monster.name, damage));
        if (monster.isAlive()) {
            counterattack(events);
        }
        return events;
    }

    ArrayList<String> useItem(int slot) {
        ArrayList<String> events = new ArrayList<>();
        events.add(player.inventory.use(slot, player));
        counterattack(events);
        return events;
    }

    private void counterattack(List<String> events) {
        int monsterAttack = roll();
        int playerDefense = roll();
        int damage = player.takeDamage(Math.max(0, monsterAttack - playerDefense));
        events.add(String.format("%s 반격 주사위 %d · 방어 주사위 %d · 받은 피해 %d",
                monster.name, monsterAttack, playerDefense, damage));
    }

    private static int roll() {
        return ThreadLocalRandom.current().nextInt(MAX_ROLL + 1);
    }
}
