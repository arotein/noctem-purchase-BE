package noctem.purchaseService.global.enumeration;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Amount {
    STORE_CUP("매장컵"),
    DISPOSABLE_CUP("일회용컵"),

    // ...

    ONE("1"),
    TWO("2"),
    THREE("3"),
    // ...
    NINE("9");

    private String value;

    Amount(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private static final Map<String, Amount> VALUE_MAP = Stream.of(values()).collect(Collectors.toMap(Amount::getValue, e -> e));

    public static Amount findByValue(String value) {
        return VALUE_MAP.get(value);
    }
}
