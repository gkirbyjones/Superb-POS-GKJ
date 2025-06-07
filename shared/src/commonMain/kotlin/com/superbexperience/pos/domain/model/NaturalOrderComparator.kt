package com.superbexperience.pos.domain.model

// This sorts a collection using natural sort order so that we don't have the situation
// where entries are sorted in an unnatural order
// eg The ascii-betical order would be: a0124, a123, a1211
//    The human sort order would be: a123, a0124, a1211

// This algorithm is based on this stack exchange post:
// https://codereview.stackexchange.com/a/161664
// It is the solution posted by user Luis on Apr 25 '17
// Ported to Kotlin and refactored
// More detailed explanation of the algorithm noted in a separate project
// Other solutions may also be available

class NaturalOrderComparator : Comparator<String> {
    override fun compare(
        a: String,
        b: String,
    ): Int {
        // Take case out of the issue by converting to lower
        val firstComparable = NaturalOrderComparableGroup(a.lowercase())
        val secondComparable = NaturalOrderComparableGroup(b.lowercase())
        return firstComparable.compareTo(secondComparable)
    }
}

private const val COMPARE_EQUAL = 0 // defined for clarity in the code

internal class NaturalOrderComparableGroup(private val originalString: String) : Comparable<NaturalOrderComparableGroup> {
    private val elements: List<NaturalOrderComparableElement>

    /**
     *  order is important eg space is before numbers is before alphabet
     */
    enum class ElementPriority {
        PRIORITY_NON_ALPHANUMERIC,
        PRIORITY_NUMERIC,
        PRIORITY_ALPHABETIC,
    }

    init {
        val elementList = ArrayList<NaturalOrderComparableElement>()
        var i = 0
        // Go through the string character by character, stripping out the components
        while (originalString.length > i) {
            val character: Char = originalString[i]
            // Its either alphabetical (a-z), numerical (0-9), non alphanumerical (everything else)
            when {
                character.isLetter() -> {
                    elementList.add(AlphabeticComparableElement(character, ElementPriority.PRIORITY_ALPHABETIC))
                    i++
                }
                character.isDigit() -> {
                    val numericComparableElement = NumericComparableElement(character, ElementPriority.PRIORITY_NUMERIC)
                    i++
                    // Go through the subsequent characters until the end or a non digit is found
                    while (originalString.length >= i + 1 && originalString[i].isDigit()) {
                        numericComparableElement.add(originalString[i])
                        i++
                    }
                    // We now have the whole string of numbers in this group
                    elementList.add(numericComparableElement)
                }
                else -> {
                    elementList.add(NonAlphanumericComparableElement(character, ElementPriority.PRIORITY_NON_ALPHANUMERIC))
                    i++
                }
            }
        }
        elements = elementList
    }

    /**
     * Groups are compared using their own special rules.
     * @param other group to compare against
     * @return -1 if other is considered larger than this, 0 if equal, 1 if other is less
     */
    override fun compareTo(other: NaturalOrderComparableGroup): Int {
        var i = 0
        var compareResult: Int
        // Go through until we run out of matching pairs of elements or a not equal pair
        while (bothGroupsHaveEnoughElements(other, i)) {
            // Compare elements and quit if non equal is found, otherwise
            // keep going until we run out of matching pairs
            compareResult = elements[i].compareTo(other.elements[i])
            if (compareResult != COMPARE_EQUAL) {
                return compareResult
            }
            i++
        }
        // We get here if we run out of matching elements before finding a difference
        compareResult = compareElementSize(other)

        // If we are still equal then we need to check for reduced numbers as 004 should come after 04
        if (compareResult == COMPARE_EQUAL) {
            // Go through the loop again but this time we need to look for numerical condensing
            i = 0
            while (bothGroupsHaveEnoughElements(other, i)) {
                // Get the original sizes of each element and bomb out when an inequality is found
                val compareSizes = elements[i].compareOriginalSizes(other.elements[i])
                if (compareSizes != COMPARE_EQUAL) {
                    return compareSizes
                }
                i++
            }
        }
        return compareResult
    }

    /**
     * Compare the elements to ensure they have an element at the passed position
     * @param other other element
     * @param requiredLength position to check for
     * @return true if they are both good, false otherwise
     */
    private fun bothGroupsHaveEnoughElements(
        other: NaturalOrderComparableGroup,
        requiredLength: Int,
    ): Boolean {
        // is the size of the elements less than required?
        return elements.size >= requiredLength + 1 && other.elements.size >= requiredLength + 1
    }

    /**
     * Returns whether the passed group has more, less or equal items in this element
     * @param other other element
     * @return -1 if this is smaller than passed group, 0 if equal, 1 if greater
     */
    private fun compareElementSize(other: NaturalOrderComparableGroup): Int = elements.size.compareTo(other.elements.size)

    /**
     * Output the group in an easy to read format
     */
    override fun toString(): String {
        val output = StringBuilder()

        for (element in elements) {
            output.append(element.toString())
        }
        return if (originalString == output.toString()) {
            originalString
        } else {
            // Display both the original and modified
            "$originalString ( $output)"
        }
    }
}

internal abstract class NaturalOrderComparableElement(private val priority: NaturalOrderComparableGroup.ElementPriority) {
    fun compareTo(other: NaturalOrderComparableElement): Int {
        val priorityCompare = priority.compareTo(other.priority)
        return if (priorityCompare != COMPARE_EQUAL) {
            priorityCompare
        } else {
            compareToInstanceOfSamePriority(other)
        }
    }

    /**
     * comparison to item that is guaranteed to be the same type
     * @param other element to compare
     * @return -1 if this is considered smaller than passed element, 0 if equal, 1 if greater
     */
    abstract fun compareToInstanceOfSamePriority(other: NaturalOrderComparableElement): Int

    /**
     * Returns the sizes of the elements before any condensing (applies to numeric only)
     * @return original size of string before condensing
     */
    abstract fun getOriginalSize(): Int

    /**
     * Compares the original sizes of the elements before condensing
     * @param other the other element to compare
     * @return -1 if this is smaller than passed element, 0 if equal, 1 if greater
     */
    fun compareOriginalSizes(other: NaturalOrderComparableElement): Int {
        return getOriginalSize().compareTo(other.getOriginalSize())
    }
}

/**
 * Letters such as a-z and foreign language characters, eg à, a, ê, ß
 */
internal class AlphabeticComparableElement(
    private val character: Char,
    priority: NaturalOrderComparableGroup.ElementPriority,
) :
    NaturalOrderComparableElement(priority) {
    override fun compareToInstanceOfSamePriority(other: NaturalOrderComparableElement): Int {
        // If both the same type of element then compare otherwise assume equal
        (other as? AlphabeticComparableElement)?.let { return character.compareTo(it.character) }
        return COMPARE_EQUAL
    }

    override fun getOriginalSize(): Int {
        return 1
    } // always only 1 character
}

/**
 * Numeric - ie 0-9
 */
internal class NumericComparableElement(
    character: Char,
    priority: NaturalOrderComparableGroup.ElementPriority,
) :
    NaturalOrderComparableElement(priority) {
    // note that we know this is a digit from the callee but just in case default to zero on error
    private var integer = character.toString().toIntOrNull() ?: 0

    // This will contain the whole length, including leading zeros
    private var originalIntegerLength: Int = 1

    override fun getOriginalSize(): Int {
        return originalIntegerLength
    }

    override fun compareToInstanceOfSamePriority(other: NaturalOrderComparableElement): Int {
        // If both the same type of element then compare otherwise assume equal
        (other as? NumericComparableElement)?.let { return integer.compareTo(it.integer) }
        return COMPARE_EQUAL
    }

    fun add(character: Char) {
        originalIntegerLength++
        // Bump up the current contents and add the new digit
        integer *= 10
        integer += character.toString().toIntOrNull() ?: 0
    }

    override fun toString(): String {
        return "$integer ($originalIntegerLength)"
    }
}

/**
 * Anything that the use can enter that isn't 0-9, a-z
 */
internal class NonAlphanumericComparableElement(
    private val character: Char,
    priority: NaturalOrderComparableGroup.ElementPriority,
) :
    NaturalOrderComparableElement(priority) {
    override fun getOriginalSize(): Int {
        return 1
    } // Always only 1 character

    override fun compareToInstanceOfSamePriority(other: NaturalOrderComparableElement): Int {
        // If both the same type of element then compare otherwise assume equal
        (other as? NonAlphanumericComparableElement)?.let { return character.compareTo(it.character) }
        return COMPARE_EQUAL
    }

    override fun toString(): String {
        return character.toString()
    }
}

/**
 * Returns `true` if this character is a letter.
 *
 * A character is considered to be a letter if its [category] is [CharCategory.UPPERCASE_LETTER],
 * [CharCategory.LOWERCASE_LETTER], [CharCategory.TITLECASE_LETTER], [CharCategory.MODIFIER_LETTER], or [CharCategory.OTHER_LETTER].
 *
 * @sample samples.text.Chars.isLetter
 */
fun Char.isLetter(): Boolean {
    return this in 'a'..'z' || this in 'A'..'Z'
}

/**
 * Returns `true` if this character is user-visible (i.e. not whitespace)
 */
fun Char.isPrintable(): Boolean {
    return !this.isWhitespace()
}

/**
 * Returns `true` if this character is a digit.
 *
 * A character is considered to be a digit if its [category] is [CharCategory.DECIMAL_DIGIT_NUMBER].
 *
 * @sample samples.text.Chars.isDigit
 */
fun Char.isDigit(): Boolean {
    return this in '0'..'9'
}
