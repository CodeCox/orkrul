import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.delay
import org.openrndr.*
import org.openrndr.draw.Drawer

/*
 * Copyright (C) 2018.  CodeCox@NousMous - All Rights Reserved.
 */

// special case of delay duration
const val NODRAW = Long.MAX_VALUE

data class DrawRateConfig(var enabled: Boolean, var duration: Long)

class DrawRate : Extension {
    override var enabled = true
    var b4draw = 0.0 //debug logging

    lateinit var drawRateJob: Job
    lateinit var minRateJob: Job
    var drawRate = DrawRateConfig(false, 1000L)  // what default values?
    var minRate = DrawRateConfig(false, 2000L)   // what default values?
    var restore = false

    override fun setup(program: Program) {
        // 1.a setup DEFAULT case DRAWRATE
        if (drawRate.enabled) {
            drawRateJob = program.launch {
                pulse(program, drawRate)
            }
        }

        // 1.b setup ON/OFF toggle for DRAWRATE
        program.keyboard.keyDown.listen {
            if (it.key == KEY_SPACEBAR) {           // use more obscure key for production!
                drawRate.enabled = !drawRate.enabled
                if (drawRate.enabled) {
                    drawRateJob = program.launch {
                        pulse(program, drawRate)
                    }
                } else {
                    if (::drawRateJob.isInitialized)
                        drawRateJob.cancel()
                    program.window.presentationMode = PresentationMode.AUTOMATIC
                }
            }
        }


        // 2. setup special case #1 NOTVISIBLE e.g. MINIMISED
        program.window.sized.listen {
            if (it.size.x == 0.0 || it.size.x == 0.0) {
                println("width or height is 0")
                if (minRate.enabled) {
                    if (::drawRateJob.isInitialized) {
                        if (drawRateJob.isActive) {
                            restore = true
                            drawRateJob.cancel()
                        }
                    }
                    minRateJob = program.launch {
                        pulse(program, minRate)
                    }
                }
            } else {  // messy - WindowEvent does not have enough info, need this only for 'restore' event
                if (::minRateJob.isInitialized)
                    minRateJob.cancel()
                if (restore) {
                    restore = false
                    drawRateJob = program.launch {
                        pulse(program, drawRate)
                    }
                } else {
                    //bug - must restore to prev NODRAW DrawRate state
                    program.window.presentationMode = PresentationMode.AUTOMATIC
                }
            }
        }

        // 3. setup special case #2  UNFOCUSED - only on MacOS? why not Windows10?
        program.window.unfocused.listen {
            println("UNFOCUSED: I'm not getting this callback on Windows10!!! Are you?")
        }
        program.window.focused.listen {
            println("FOCUSED: I'm not getting this callback on Windows10!!! Are you?")
        }

    }

    override fun beforeDraw(drawer: Drawer, program: Program) {
        b4draw = program.seconds
    }

    override fun afterDraw(drawer: Drawer, program: Program) {
        println("$b4draw - ${program.seconds}")
    }

    @UseExperimental(InternalCoroutinesApi::class)
    suspend fun pulse(program: Program, drc: DrawRateConfig) {
        program.window.presentationMode = PresentationMode.MANUAL
        if (drc.duration != NODRAW) {
            while (isActive) {
                delay(drc.duration)
                println("request Draw after ${drc.duration}L")
                program.window.requestDraw()
            }
            program.window.presentationMode = PresentationMode.AUTOMATIC
        }
    }
}

