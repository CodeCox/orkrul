import org.openrndr.application
import org.openrndr.color.ColorRGBa
import kotlin.random.Random

/*
 * Copyright (C) 2018.  CodeCox@NousMous - All Rights Reserved.
 */
fun main() = application {
    configure {
        title = "throttle"
    }
    program {
        extend(Throttle()) {
            // spacebar toggles the throttle ON/OFF
            // ToDo configure events
        }
        extend {
            drawer.fill = ColorRGBa.PINK
            drawer.circle(width * Random.nextDouble(), height * Random.nextDouble(), 40.0)
        }
    }
}
