package com.notes.utils;

import com.notes.utils.constants.NoteAccessBits;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Objects;

@UtilityClass
public class BitmapUtil {
    public static int getBitmapWithBitsSetForPos(List<NoteAccessBits> bitPositions) {
        if (bitPositions == null || bitPositions.isEmpty()) {
            return 0;
        }

        for(NoteAccessBits noteAccessBits : bitPositions) {
            if (noteAccessBits.equals(NoteAccessBits.EDIT) || noteAccessBits.equals(NoteAccessBits.DELETE)) {
                bitPositions.add(NoteAccessBits.READ);
                break;
            }
        }

        List<Integer> bits = bitPositions.stream()
                .filter(Objects::nonNull)
                .map(NoteAccessBits::getBitPosition)
                .distinct()
                .toList();

        return getBitsSet(bits);
    }

    public static int getBitsSet(List<Integer> bits) {
        int bitwiseNumber = 0;

        for (Integer bit : bits) {
            if (bit >= 0 && bit < 31) {
                bitwiseNumber |= (1 << bit);
            }
        }

        return bitwiseNumber;
    }

    public static boolean isNthBitSet(Integer number, int n) {
        if (number == null) {
            return false;
        }

        return (number & (1 << n)) != 0;
    }
}
