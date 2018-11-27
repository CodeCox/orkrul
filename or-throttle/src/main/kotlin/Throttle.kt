import kotlinx.coroutines.delay
import org.openrndr.*
import org.openrndr.draw.Drawer

/*
 * Copyright (C) 2018.  CodeCox@NousMous - All Rights Reserved.
 */

/**
 *  Throttle Event config data - highlevel criteria needs to be standard across events?
 */
data class ThrottleEvent(val id: String, var enabled: Boolean, var duration: Long)

class Throttle : Extension {
    override var enabled = true
    var b4draw = 0.0 //debug logging

    @Volatile
    var isChoked = false // Throttle is On(true) or Off(false)
    val chokelist = listOf<ThrottleEvent>(
            ThrottleEvent("Default", true, 1000L),
            ThrottleEvent("Other1", false, 500L),
            ThrottleEvent("Other2", true, 300L),
            ThrottleEvent("Other4", true, 600L)
    )

    override fun setup(program: Program) {
        program.launch {
            choker(program)
        }
        // toggle the throttle? NB! this is not an interrupt -won't respond immediatley to keypress!
        program.keyboard.keyDown.listen {
            if (it.key == KEY_SPACEBAR) {
                isChoked = !isChoked
                if (isChoked)
                    program.launch {
                        choker(program)
                    }
            }
        }
    }

    override fun beforeDraw(drawer: Drawer, program: Program) {
        b4draw = program.seconds
    }

    override fun afterDraw(drawer: Drawer, program: Program) {
        println("$b4draw - ${program.seconds}")
    }

    suspend fun choker(program: Program) {
        val enabledlist = chokelist.filter { t -> t.enabled }
        // insert more event specific checks here to whittle down list?
        val chkEvent = enabledlist.maxBy { t -> t.duration }
        if (chkEvent != null) {
            isChoked = true
            program.window.presentationMode = PresentationMode.MANUAL
            while (isChoked) {
                delay(chkEvent.duration)
                println("sending draw request after ${chkEvent.duration}L for ${chkEvent.id} event")
                program.window.requestDraw()
            }
            program.window.presentationMode = PresentationMode.AUTOMATIC
        }
    }

}
