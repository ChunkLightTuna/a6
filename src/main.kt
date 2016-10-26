import java.io.FileInputStream

//main is now the resolver yo
fun main(args: Array<String>) {
    args.forEach {
        if (it.endsWith(".in")) {
            System.setIn(FileInputStream("sample-input/study2.in"))
        }
    }
    val reader = System.`in`
    val domain = Parser.parse(reader)
    reader.close()

//    println("DOMAIN::\n" +
//            "Predicates:\t${domain.predicates}\n" +
//            "Constants:\t${domain.constants}\n" +
//            "Actions:\t${domain.actions.size}")
}