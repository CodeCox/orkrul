import org.openrndr.application
import org.openrndr.color.ColorRGBa

/*
 * Copyright (C) 2018.  CodeCox@NousMous - All Rights Reserved.
 */
fun main() = application {
    configure {
        title = "Color Perception based on DrawRate"
    }
    program {
        /* test cases
        1. no user config, ext defaults
        2. partial user config, partial ext defaults
        3. user config
        4. runtime config
         */
        val dcDrawRate = DrawConfig("myDrawRate", true, 200L)
        val dcMinimiseRate = DrawConfig("myMinimiseRate", true, 400L)
        extend(DrawRate()) {
            drawRate = dcDrawRate
            minimiseRate = dcMinimiseRate
        }

        var toggle = false
        extend {
            val dbgStart = seconds   //debug

            toggle = !toggle
            if (toggle)
                drawer.fill = ColorRGBa.BLUE
            else
                drawer.fill = ColorRGBa.RED
            drawer.circle(width / 2.0, height / 2.0, 200.0)

            // DrawRate runtime config - REMOVE COMMENTS TO TEST!
            /*
            if (seconds.toInt() % 20 == 0)
                dcDrawRate.duration = 1L
            if (seconds.toInt() % 20 == 10)
                dcDrawRate.duration = 225L
            */
            println("$dbgStart-$seconds") //debug
        }
    }
}

