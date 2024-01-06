package com.notes.utils.constants;

import lombok.Getter;

import java.util.List;

@Getter
public enum NoteAccessBits {
    READ(0),
    EDIT(1),
    DELETE(2),
    SHARE(3);

    private final int bitPosition;

    NoteAccessBits(int bitPosition) {
        this.bitPosition = bitPosition;
    }
}
