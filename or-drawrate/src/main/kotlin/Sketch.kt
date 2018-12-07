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

        // this configurable feature supports creative use of the Draw() loop.
        // n > 0, < Long.MAX_VALUE, or NODRAW(Long.MAX_VALUE)

        /* permutations
        AUTODRAW : AUTODRAW
        AUTODRAW : 0 to MAX_VALUE - 1
        AUTODRAW : NODRAW

        1 to MAX_VALUE - 1 : AUTODRAW
        1 to MAX_VALUE - 1 : 0 to MAX_VALUE - 1
        1 to MAX_VALUE - 1  : NODRAW

        NODRAW : AUTODRAW
        NODRAW : 0 to MAX_VALUE - 1
        NODRAW : NODRAW
        */

        // optional custom initialisation that will override default  values
        val drcDR = DrawConfig(100L)
        // optional custom initialisation for special case where x or y is 0 e.g. window MINIMISED, SIZED
        val drcM = DrawConfig(200L)

        extend(DrawRate()) {
            drawRate = drcDR
            minimiseRate = drcM
        }

        var x = 0.0
        extend {
            val dbgStart = seconds

            // DrawRate runtime config from ref vars in custom initialisation - Uncomment to test
            /*
            if (seconds.toInt() % 10 == 0)
                drcDR.duration = 10L
            if (seconds.toInt() % 10 == 2)
                drcDR.duration = 20L
            if (seconds.toInt() % 10 == 4)
                drcDR.duration = 40L
            if (seconds.toInt() % 10 == 6)
                drcDR.duration = 60L
            if (seconds.toInt() % 10 == 8)
                drcDR.duration = 80L
            */

            x += 40
            drawer.fill = ColorRGBa.PINK
            drawer.circle(x.rem(width), height / 2.0, 40.0)

            // test-fix this
            /*
            if (seconds > 20.0)
                drcDR = AUTODRAW
            */

            println("$dbgStart-$seconds")
        }
    }
}

