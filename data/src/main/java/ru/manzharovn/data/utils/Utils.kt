package ru.manzharovn.data.utils

fun String.removeHtmlTags() =
    Regex("""<h2>[\w\s]*</h2>|<figcaption>[\w\s]*</figcaption>|<\/?[^>]+(>|${'$'})""")
        .replace(this, "")
        .trim()