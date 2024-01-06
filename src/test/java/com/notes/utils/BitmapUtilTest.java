package com.notes.utils;

import com.notes.utils.constants.NoteAccessBits;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.notes.utils.BitmapUtil.getBitmapWithBitsSetForPos;
import static com.notes.utils.BitmapUtil.getBitsSet;
import static com.notes.utils.constants.NoteAccessBits.*;
import static org.junit.Assert.*;

public class BitmapUtilTest {
    @Test
    public void bitmapForAccessPrivilegesTest() {
        List<NoteAccessBits> accessBits = new ArrayList<>();

        int bitmap = getBitmapWithBitsSetForPos(accessBits);
        assertEquals(toDecimal("0000"), bitmap);

        accessBits.add(READ);
        bitmap = getBitmapWithBitsSetForPos(accessBits);
        assertEquals(toDecimal("0001"), bitmap);

        accessBits.remove(READ);
        accessBits.add(EDIT);
        bitmap = getBitmapWithBitsSetForPos(accessBits);
        assertEquals(toDecimal("0011"), bitmap);

        accessBits.add(READ);
        bitmap = getBitmapWithBitsSetForPos(accessBits);
        assertEquals(toDecimal("0011"), bitmap);

        accessBits.add(READ);
        accessBits.add(EDIT);
        accessBits.add(DELETE);
        bitmap = getBitmapWithBitsSetForPos(accessBits);
        assertEquals(toDecimal("0111"), bitmap);
    }

    @Test
    public void isNthBitSetForNumberTest() {
        boolean isBitSet = BitmapUtil.isNthBitSet(toDecimal("0001"), 0);
        assertTrue(isBitSet);

        isBitSet = BitmapUtil.isNthBitSet(toDecimal("0001"), 3);
        assertFalse(isBitSet);

        isBitSet = BitmapUtil.isNthBitSet(toDecimal("0110"), 1);
        assertTrue(isBitSet);

        isBitSet = BitmapUtil.isNthBitSet(toDecimal("0110"), 2);
        assertTrue(isBitSet);

        isBitSet = BitmapUtil.isNthBitSet(toDecimal("1000"), 3);
        assertTrue(isBitSet);

        isBitSet = BitmapUtil.isNthBitSet(toDecimal("1000"), 1);
        assertFalse(isBitSet);

        isBitSet = BitmapUtil.isNthBitSet(toDecimal("000100001"), 0);
        assertTrue(isBitSet);

        isBitSet = BitmapUtil.isNthBitSet(toDecimal("000100001"), 5);
        assertTrue(isBitSet);

        isBitSet = BitmapUtil.isNthBitSet(toDecimal("000100001"), 4);
        assertFalse(isBitSet);
    }

    @Test
    public void numberWithNthBitSetTest() {
        int bitmap = getBitsSet(List.of(0));
        assertEquals(toDecimal("0001"), bitmap);

        bitmap = getBitsSet(List.of(2));
        assertEquals(toDecimal("0100"), bitmap);

        bitmap = getBitsSet(List.of(1, 2));
        assertEquals(toDecimal("0110"), bitmap);

        bitmap = getBitsSet(List.of(0, 1, 2));
        assertEquals(toDecimal("0111"), bitmap);

        bitmap = getBitsSet(List.of(3));
        assertEquals(toDecimal("1000"), bitmap);

        bitmap = getBitsSet(List.of(0, 3));
        assertEquals(toDecimal("1001"), bitmap);

        bitmap = getBitsSet(List.of(0, 2, 3));
        assertEquals(toDecimal("1101"), bitmap);

        bitmap = getBitsSet(List.of(1, 2, 3));
        assertEquals(toDecimal("1110"), bitmap);

        bitmap = getBitsSet(List.of(0, 1, 2, 3));
        assertEquals(toDecimal("1111"), bitmap);
    }

    private int toDecimal(String s) {
        return Integer.parseInt(s, 2);
    }
}