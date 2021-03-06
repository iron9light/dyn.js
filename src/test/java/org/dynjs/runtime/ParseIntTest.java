package org.dynjs.runtime;

import org.fest.assertions.DoubleAssert;
import org.junit.Test;

import static org.dynjs.runtime.DynNumber.NAN;
import static org.dynjs.runtime.DynNumber.parseInt;
import static org.fest.assertions.Assertions.assertThat;

public class ParseIntTest {

    // notes to myself while developing:
    // we have some cases here:
    // 1. spaces will be trimmed
    // 2. an empty string returns NaN
    // 3. a valid number starts with +, - or [0-9]
    // 4. a number starting with 0x with default radix is hexa
    // 5. a number starting with 0x with not default radix is NaN
    // 6. a number starting with 0 with default radix is octal
    // 7. anything else after valid numbers will be removed
    // 8.

    @Test
    public void spacesWillBeTrimmed() {
        assertThat(parseInt(new DynString("  "))).as("blank spaces").isEqualTo(NAN);
        assertThatValueOf(parseInt(new DynString("  0 "))).as("integer number").isEqualTo(0);
        assertThatValueOf(parseInt(new DynString("  0.0 "))).as("float number").isEqualTo(0);
    }

    @Test
    public void emptyStringReturnsNaN() {
        assertThat(parseInt(new DynString(""))).isEqualTo(NAN);
    }

    @Test
    public void validNumbersStartWithPlusMinusOrDigits() {
        assertThat(parseInt(new DynString("a"))).isEqualTo(NAN);
        assertThat(parseInt(new DynString("x"))).isEqualTo(NAN);
        assertThat(parseInt(new DynString("FE"))).isEqualTo(NAN);
        assertThat(parseInt(new DynString("*3"))).isEqualTo(NAN);
        assertThat(parseInt(new DynString(".1"))).isEqualTo(NAN);

        assertThat(parseInt(new DynString("0.1"))).isNotEqualTo(NAN);
        assertThat(parseInt(new DynString("9999999"))).isNotEqualTo(NAN);
        assertThat(parseInt(new DynString("+2"))).isNotEqualTo(NAN);
        assertThat(parseInt(new DynString("+ 2"))).isNotEqualTo(NAN);
        assertThat(parseInt(new DynString("-3"))).isNotEqualTo(NAN);
        assertThat(parseInt(new DynString("- 3"))).isNotEqualTo(NAN);
    }

    @Test
    public void numbersStartingWith0xIsHexadecimal() {
        assertThatValueOf(parseInt(new DynString("0xff"))).isEqualTo(0xff);
    }

    private DoubleAssert assertThatValueOf(DynNumber number) {
        return assertThat(number.getValue());
    }
}