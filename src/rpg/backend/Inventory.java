package rpg.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 소비 아이템을 보관하고, 사용한 아이템을 목록에서 제거한다.
 */
final class Inventory {
    private final ArrayList<Usable> items = new ArrayList<>();

    void add(Usable item) {
        items.add(item);
    }

    List<String> getItemNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Usable item : items) {
            names.add(item.getName());
        }
        return List.copyOf(names);
    }

    String use(int slot, Player player) {
        if (items.isEmpty()) {
            throw new NoSuchElementException("인벤토리가 비어 있습니다.");
        }
        if (slot < 0 || slot >= items.size()) {
            throw new IndexOutOfBoundsException("해당 번호에 아이템이 없습니다.");
        }

        Usable item = items.get(slot);
        String result = item.use(player);
        items.remove(slot);
        return result;
    }
}
