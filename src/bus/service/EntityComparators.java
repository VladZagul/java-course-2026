package bus.service;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import bus.model.BaseEntity;

public class EntityComparators {
    public static final Comparator<BaseEntity> BY_ID = Comparator.comparingLong(BaseEntity::getId);

    private EntityComparators() {
    }

    public static <T extends BaseEntity> Comparator<T> byName() {
        Collator collator = Collator.getInstance(Locale.of("uk", "UA"));
        return Comparator.comparing(BaseEntity::getName, collator);
    }
}
