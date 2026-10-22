package bus.util;

import java.util.Collection;

import bus.model.BaseEntity;

public class EntityPrinter {

    private EntityPrinter() {
    }

    public static void printAll(Collection<? extends BaseEntity> items) {
        if (items.isEmpty()) {
            System.out.println("   (порожньо)");
            return;
        }
        for (BaseEntity item : items) {
            System.out.println("   " + item);
        }
    }
}
