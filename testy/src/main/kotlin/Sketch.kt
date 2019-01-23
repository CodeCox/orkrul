import org.openrndr.application

fun main() = application {
    configure {
        title = "or"
    }
    program {
        extend {
            drawer.circle(width / 2.0, height / 2.0, 100.0)
        }
    }
}
