package rpg.backend;

/**
 * 플레이어의 체력을 회복하는 소비 아이템을 나타낸다.
 */
final class Item implements Usable {
    private final String name;
    private final int healing;

    Item(String name, int healing) {
        this.name = name;
        this.healing = healing;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String use(Player player) {
        int recovered = player.heal(healing);
        return String.format("%s 사용, 체력 %d 회복", name, recovered);
    }
}
