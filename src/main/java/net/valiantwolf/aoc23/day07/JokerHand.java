package net.valiantwolf.aoc23.day07;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

class JokerHand implements Comparable<JokerHand> {

    private final int[] cards = new int[5];
    private final Type type;
    private int bid;

    public JokerHand(String input) {
        var parts = input.split(" ");

        bid = Integer.parseInt(parts[1]);

        var cardString = parts[0];

        for (int i = 0; i < 5; i++) {
            var card = cardString.charAt(i);

            if (card > '0' && card <= '9') {
                cards[i] = card - '0';
            } else {
                cards[i] = switch (card) {
                    case 'T' -> 10;
                    case 'J' -> 0;
                    case 'Q' -> 12;
                    case 'K' -> 13;
                    case 'A' -> 14;
                    default -> throw new IllegalArgumentException();
                };
            }
        }

        var groupingMap = Arrays.stream(cards)
                              .boxed()
                              .collect(Collectors.groupingBy(Function.identity(),
                                                             Collectors.reducing(0, o -> 1, Integer::sum)));

        int jokers = Optional.ofNullable(groupingMap.remove(0)).orElse(0);

        if (jokers == 5) {
            type = Type.FIVE_OF_A_KIND;
            return;
        }

        var groupings = groupingMap.values()
                                   .stream()
                                   .sorted(Comparator.reverseOrder())
                                   .toList();

        type = switch (groupings.get(0) + jokers) {
            case 5 -> Type.FIVE_OF_A_KIND;
            case 4 -> Type.FOUR_OF_A_KIND;
            case 3 -> groupings.get(1) == 2
                      ? Type.FULL_HOUSE
                      : Type.THREE_OF_A_KIND;
            case 2 -> groupings.get(1) == 2
                      ? Type.TWO_PAIR
                      : Type.ONE_PAIR;
            default -> Type.HIGH_CARD;
        };
    }

    public int getBid() {
        return bid;
    }

    public int[] getCards() {
        return cards;
    }

    @Override
    public int compareTo(JokerHand that) {
        var typeCompare = this.type.compareTo(that.type);

        if (typeCompare != 0) {
            return typeCompare;
        }

        for (int i = 0; i < 5; i++) {
            var cardCompare = Integer.compare(this.cards[i], that.cards[i]);

            if (cardCompare != 0) {
                return cardCompare;
            }
        }

        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof JokerHand that)) {
            return false;
        }

        return compareTo(that) == 0;
    }

    enum Type {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        FIVE_OF_A_KIND;
    }
}
