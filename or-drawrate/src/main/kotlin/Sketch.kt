import org.openrndr.application
import org.openrndr.color.ColorRGBa

/*
 * Copyright (C) 2018.  CodeCox@NousMous - All Rights Reserved.
 */
fun main() = application {
    configure {
        title = "DrawRate"
    }
    program {

        /*
        This feature uses PresentationMode.AUTOMATIC & PresentationMode.MANUAL
        WARNING:  If you also use these, then it may clash and disrupt the functionality
         */
        /* permutations
        FALSE - FALSE
        FALSE - TRUE
        TRUE - FALSE
        TRUE - TRUE
         */

        /* test case
        1. no user config, ext defaults
        2. partial user config, partial ext defaults
        3. user config
        4. runtime config
         */

        /*
        <CTRL-SPACEBAR> toggles DrawRate ON/OFF(true/false)
        Window must be active to receive keyboard input
         */

        // optional custom initialisation that will override default  values
        val dcDrawRate = DrawConfig("myDrawRate", true, 150L)
        // optional custom initialisation for special case where x or y is 0 e.g. window MINIMISED, SIZED
        val dcMinimiseRate = DrawConfig("myMinimiseRate", true, 250L)

        extend(DrawRate()) {
            drawRate = dcDrawRate
            minimiseRate = dcMinimiseRate
        }

        var toggle = false
        extend {
            val dbgStart = seconds

            // DrawRate runtime config from ref vars in custom initialisation - Uncomment to test
            /*
            if (seconds.toInt() % 30 == 0)
                dcDrawRate.duration = 1L
            if (seconds.toInt() % 30 == 15)
                dcDrawRate.duration = 200L
            */
            toggle = !toggle
            if (toggle)
                drawer.fill = ColorRGBa.YELLOW
            else
                drawer.fill = ColorRGBa.RED
            drawer.circle(width / 2.0, height / 2.0, 200.0)
            println("$dbgStart-$seconds")
        }
    }
}

