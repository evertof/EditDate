package br.com.evertofabio

fun onlyNumbers(text: String): String {
    var numbers = ""

    text.forEach {
        numbers += if (it.isDigit()) it else ""
    }

    return numbers
}

