fun main() {
    val cities = listOf("Seoul", "Washington", "Berlin")
    val citiesCode = listOf("SEO", "DC", "BRL")

    println("1. map================================")
    cities
            .map(String::toUpperCase)
            .map { "city name : $it" }
            .forEach { println(it) }

    println("2. groupBy================================")
    cities
            .groupBy { if (it.length > 5) "A" else "B" }
            .also{ println(it) }
            .forEach { println(it) }

    println("3. filter================================")
    cities
            .filter { it.length ==5 }
            .forEach { println(it) }
    println("4. take================================")
    cities
            .take(2)
            .forEach { println(it) }
    println("5. zip================================")
    cities
            .zip(citiesCode)
            .forEach { println(it) }

    println("6. reduce================================")
    cities
            .reduce { current, next -> "$current $next" }
            .also{ println(it)}
    cities
            .fold("city names : "){current, next -> "$current $next"}
            .also{ println(it)}

}