package rpg.backend;

/**
 * 인벤토리에 담을 수 있는 소비 아이템의 사용 규약을 정의한다.
 */
interface Usable {
    String getName();

    String use(Player player);
}
