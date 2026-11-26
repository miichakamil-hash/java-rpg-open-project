package rpg.backend;

/**
 * 층별 적의 최대 체력과 승리 시 획득할 경험치를 정의한다.
 */
final class Monster extends GameCharacter {
    final int experienceReward;
    final boolean boss;

    private Monster(String name, int hp, int experienceReward, boolean boss) {
        super(name, hp);
        this.experienceReward = experienceReward;
        this.boss = boss;
    }

    static Monster forFloor(int floor) {
        switch (floor) {
            case 1:
                return new Monster("동굴 슬라임", 24, 20, false);
            case 2:
                return new Monster("유적 파수꾼", 36, 25, false);
            case GameEngine.TOTAL_FLOORS:
                return new Monster("수정 수호자", 60, 35, true);
            default:
                throw new IllegalArgumentException("존재하지 않는 층입니다.");
        }
    }
}
