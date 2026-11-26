package rpg.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 수정 동굴 RPG의 한 게임을 관리하는 공개 API.
 * 프론트엔드에서 사용자 행동을 호출하고 현재 상태와 행동 결과를 읽는다.
 */
public final class GameEngine {
    /** 던전의 마지막 층. */
    public static final int TOTAL_FLOORS = 3;

    private final Player player;
    private int floor = 1;
    private Battle battle;
    private boolean cleared;

    /**
     * 체력이 가득 찬 플레이어와 시작 물약 두 개로 새 게임을 만든다.
     *
     * @param playerName 화면에 표시할 이름
     * @throws IllegalArgumentException 이름이 없거나 공백인 경우
     */
    public GameEngine(String playerName) {
        if (playerName == null || playerName.isBlank()) {
            throw new IllegalArgumentException("캐릭터 이름을 입력해 주세요.");
        }
        player = new Player(playerName.strip());
    }

    /**
     * 현재 층의 몬스터와 전투를 시작한다.
     *
     * @return 조우 메시지
     * @throws IllegalStateException 이미 전투 중이거나 게임이 종료된 경우
     */
    public String startBattle() {
        if (isGameOver() || hasActiveBattle()) {
            throw new IllegalStateException("지금은 전투를 시작할 수 없습니다.");
        }
        battle = new Battle(player, Monster.forFloor(floor));
        return String.format("%d층 전투 시작! 등장한 적은 %s입니다.", floor, battle.monster.name);
    }

    /**
     * 0~6의 주사위에 현재 레벨에서 1을 뺀 보너스를 더해 적을 공격한다.
     * 적이 살아 있으면 반격과 방어 주사위를 각각 굴려 받는 피해를 계산한다.
     *
     * @return 공격, 반격, 승패 등 화면에 표시할 메시지
     * @throws IllegalStateException 진행 중인 전투가 없는 경우
     */
    public List<String> attack() {
        return finishTurn(requireBattle().attack());
    }

    /**
     * 전투 중 최대 체력을 넘지 않는 범위에서 물약으로 체력을 최대 4 회복한다.
     * 이후 적의 반격과 플레이어의 방어 주사위를 굴린다.
     *
     * @param slot 인벤토리의 0부터 시작하는 위치
     * @return 물약 사용 및 반격 메시지
     * @throws IllegalStateException 진행 중인 전투가 없는 경우
     * @throws NoSuchElementException 인벤토리가 비어 있는 경우
     * @throws IndexOutOfBoundsException 아이템 위치가 목록 범위를 벗어난 경우
     */
    public List<String> useItem(int slot) {
        return finishTurn(requireBattle().useItem(slot));
    }

    /**
     * 현재 층의 일반 전투에서 후퇴한다. 플레이어의 남은 체력을 유지한다.
     *
     * @return 후퇴 메시지
     * @throws IllegalStateException 진행 중인 전투가 없는 경우
     * @throws UnsupportedOperationException 보스와 전투 중인 경우
     */
    public String flee() {
        if (requireBattle().monster.boss) {
            throw new UnsupportedOperationException("보스 전투에서는 후퇴할 수 없습니다.");
        }

        battle = null;
        return String.format("%d층 전투에서 후퇴했습니다.", floor);
    }

    /** @return 전투가 진행 중이면 true */
    public boolean hasActiveBattle() {
        return battle != null;
    }

    /** @return 보스를 쓰러뜨렸으면 true */
    public boolean isCleared() {
        return cleared;
    }

    /** @return 플레이어가 쓰러졌거나 보스를 쓰러뜨렸으면 true */
    public boolean isGameOver() {
        return cleared || !player.isAlive();
    }

    /** @return 현재 층 번호 */
    public int getFloor() {
        return floor;
    }

    /** @return 플레이어 이름 */
    public String getPlayerName() {
        return player.name;
    }

    /** @return 플레이어의 현재 체력 */
    public int getPlayerHp() {
        return player.getHp();
    }

    /** @return 플레이어의 최대 체력 */
    public int getPlayerMaxHp() {
        return player.getMaxHp();
    }

    /** @return 플레이어의 현재 레벨 */
    public int getPlayerLevel() {
        return player.getLevel();
    }

    /** @return 현재 레벨에서 쌓은 경험치 */
    public int getPlayerExperience() {
        return player.getExperience();
    }

    /** @return 현재 레벨에서 다음 레벨에 필요한 총 경험치 */
    public int getExperienceToLevelUp() {
        return player.getExperienceToLevelUp();
    }

    /** @return 현재 인벤토리의 수정 불가능한 아이템 이름 목록 */
    public List<String> getItemNames() {
        return player.inventory.getItemNames();
    }

    /**
     * @return 전투 중인 적의 이름
     * @throws IllegalStateException 진행 중인 전투가 없는 경우
     */
    public String getEnemyName() {
        return requireBattle().monster.name;
    }

    /**
     * @return 전투 중인 적의 현재 체력
     * @throws IllegalStateException 진행 중인 전투가 없는 경우
     */
    public int getEnemyHp() {
        return requireBattle().monster.getHp();
    }

    /**
     * @return 전투 중인 적의 최대 체력
     * @throws IllegalStateException 진행 중인 전투가 없는 경우
     */
    public int getEnemyMaxHp() {
        return requireBattle().monster.getMaxHp();
    }

    /**
     * @return 현재 적이 보스이면 true
     * @throws IllegalStateException 진행 중인 전투가 없는 경우
     */
    public boolean isBossBattle() {
        return requireBattle().monster.boss;
    }

    private Battle requireBattle() {
        if (!hasActiveBattle()) {
            throw new IllegalStateException("진행 중인 전투가 없습니다.");
        }
        return battle;
    }

    private List<String> finishTurn(ArrayList<String> events) {
        if (!battle.monster.isAlive()) {
            awardVictory(events);
        } else if (!player.isAlive()) {
            events.add("플레이어가 쓰러졌습니다. 게임 오버!");
            battle = null;
        }
        return List.copyOf(events);
    }

    private void awardVictory(List<String> events) {
        Monster defeated = battle.monster;
        int oldLevel = player.getLevel();
        player.gainExperience(defeated.experienceReward);
        events.add(String.format("%s 처치, 경험치 %d 획득", defeated.name, defeated.experienceReward));

        if (player.getLevel() > oldLevel) {
            events.add(String.format("레벨 %d 달성!", player.getLevel()));
        }

        if (defeated.boss) {
            cleared = true;
            events.add("수정 동굴을 클리어했습니다!");
        } else {
            floor++;
            events.add(String.format("%d층에 도전할 수 있습니다.", floor));
        }
        battle = null;
    }
}
