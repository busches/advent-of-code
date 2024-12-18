package `2015`

import utils.println

fun main() {

    fun validPassword(password: String): Boolean {
        val has3IncrementingCharacters = password
            .map { it.hashCode() }
            .windowed(size = 3)
            .any { it[0] + 1 == it[1] && it[1] + 1 == it[2] }

        val containsBadCharacters = password.contains("[iou]".toRegex())

        val doubleLettersRegex = "(.)\\1".toRegex()
        val hasTwoDoubleLetterPairs = doubleLettersRegex.findAll(password)
            .map { it.groupValues }
            .distinct()
            .count() > 1

        return has3IncrementingCharacters && !containsBadCharacters && hasTwoDoubleLetterPairs
    }
    check(!validPassword("hijklmmn"))
    check(!validPassword("abbceffg"))
    check(!validPassword("abbcegjk"))
    check(!validPassword("abcdffff"))
    check(validPassword("abcdffaa"))

    fun part1(startingPassword: String): String {
        val newPassword = StringBuilder(startingPassword)
        do {
            var spotToChange = 7
            while (newPassword[spotToChange] == 'z') {
                newPassword[spotToChange] = 'a'
                spotToChange--
            }
            newPassword[spotToChange] = newPassword[spotToChange] + 1

        } while (!validPassword(newPassword.toString()))

        return newPassword.toString()
    }

    check(part1("abcdefgh") == "abcdffaa")
    check(part1("ghijklmn") == "ghjaabcc")

    part1("cqjxjnds").println()
    part1(part1("cqjxjnds")).println()
}
