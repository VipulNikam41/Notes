package com.notes.utils.constants;

import lombok.Getter;

import java.util.List;

@Getter
public enum NoteAccessBits {
    READ(2),
    EDIT(3),
    DELETE(4),
    SHARE(5);

    private final int bitPosition;

    NoteAccessBits(int bitPosition) {
        this.bitPosition = bitPosition;
    }

    public static Integer getAccessBitmap(List<NoteAccessBits> bitPositions) {
        if (bitPositions == null || bitPositions.isEmpty()) {
            return null;
        }

        int bitwiseNumber = 1;

        for (NoteAccessBits accessBits : bitPositions) {
            int position = accessBits.getBitPosition();
            if (position > 1 && position < 31) {
                bitwiseNumber |= (1 << position);
            }
        }

        return bitwiseNumber;
    }
}
