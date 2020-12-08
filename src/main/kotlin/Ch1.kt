fun main(){
//    test1()
    val list1 : MutableList<Int>  = mutableListOf(1,2,3)
    val lst = list1.add(9)
    val list2 = list1.addAll(listOf<Int>(4,5,6))
//    val list3=  list1.addAll(list2)

    println(list1)
    println(list2)
//    println(list3)
}

private fun test1() {
    val list = listOf(
            Temp("1", "kim", "서울시", 1000),
            Temp("2", "lee", "대전시", 300),
            Temp("3", "lee", "경기도", 700),
            Temp("4", "park", "대전시", 300)
    )

    funcTest(list)
}

fun funcTest(list : List<Temp>) {
    var result = emptyList<Temp>()
    list
        .also { println("1. $list") }
        .groupBy { it.addr }
        .also { println("2. $it") }
        .values
        .also { println("3. $it") }
        .map {
            it.reduce{current, next -> Temp("00", "total" , "addr not neede", current.price + next.price)}
        }
        .also { println("4. $it") }
}


data class Temp(val id: String, val name: String, val addr: String, val price : Int)