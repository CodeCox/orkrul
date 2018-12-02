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
        // enabled (true or false)
        // duration > 0, < Long.MAX_VALUE, or NODRAW(== MAX_VALUE)
        // optional custom initialisation that will override default  values
        var drcDR = DrawRateConfig(true, 325L)
        // optional custom initialisation for special case where x or y is 0 e.g. MIMINISED
        var drcM = DrawRateConfig(true, 525L)

        extend(DrawRate()) {
            drawRate = drcDR
            minRate = drcM
        }

        var x = 0.0
        extend {
            x += 50
            drawer.fill = ColorRGBa.PINK
            drawer.circle(x.rem(width), height / 2.0, 40.0)

            // DrawRate runtime config from ref vars in custom initialisation - Uncomment to test
            /*
            if (seconds.toInt() % 20 == 0)
                drcDR.duration = 150L
            if (seconds.toInt() % 20 == 10)
                drcDR.duration = 400L
            */
        }
    }
}

