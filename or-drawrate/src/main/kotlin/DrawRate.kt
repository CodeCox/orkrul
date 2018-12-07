import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.delay
import org.openrndr.*
import org.openrndr.math.Vector2

/*
 * Copyright (C) 2018.  CodeCox@NousMous - All Rights Reserved.
 */

// special cases of delay duration
const val AUTODRAW = 0L                 // equivalent to PresentationMode.AUTOMATIC
const val NODRAW = Long.MAX_VALUE       // equivalent to PresentationMode.MANUAL

data class DrawConfig(var duration: Long)

class DrawRate : Extension {
    override var enabled = true
    @Volatile var drawRate = DrawConfig(AUTODRAW)
    @Volatile var minimiseRate = DrawConfig(AUTODRAW)
    private var prevRate = NODRAW
    private lateinit var drawRateJob: Job
    private lateinit var minRateJob: Job
    private var restoreWindow: Vector2? = null
    private var restoreDrawRate = false


    override fun setup(program: Program) {
        // 1.a setup DEFAULT case DRAWRATE
        setupDrawRate(program)

        // 1.b setup ON/OFF toggle for DRAWRATE
        program.keyboard.keyDown.listen {
            if (it.key == KEY_SPACEBAR && KeyboardModifier.CTRL in it.modifiers) {
                if (::drawRateJob.isInitialized && drawRateJob.isActive) { //Toggle OFF
                    drawRateJob.cancel()
                    program.window.presentationMode = PresentationMode.AUTOMATIC
                    prevRate = drawRate.duration
                    drawRate.duration = AUTODRAW
                } else if (drawRate.duration == NODRAW) { //Toggle OFF
                    program.window.presentationMode = PresentationMode.AUTOMATIC
                    prevRate = drawRate.duration
                    drawRate.duration = AUTODRAW
                } else if (drawRate.duration == AUTODRAW) { //Toggle ON
                    drawRate.duration = prevRate
                    prevRate = AUTODRAW
                    setupDrawRate(program)
                }
            }
        }

        // 2. setup special case #1 NOTVISIBLE e.g. MINIMISED
        program.window.sized.listen {
            if (it.size.x == 0.0 || it.size.y == 0.0) {
                println("Window SIZE: width or height is 0")
                if (minimiseRate.duration > 0) {
                    restoreWindow = it.size
                    program.window.presentationMode = PresentationMode.MANUAL
                    if (::drawRateJob.isInitialized && drawRateJob.isActive) {
                        drawRateJob.cancel()
                        restoreDrawRate = true
                    } else if (drawRate.duration == NODRAW)
                        restoreDrawRate = true
                    if (minimiseRate.duration != NODRAW) {
                        minRateJob = program.launch {
                            pulse(program, minimiseRate)
                        }
                    }
                }
            } else {
                if (restoreWindow?.x == 0.0 || restoreWindow?.y == 0.0) {
                    println("Window RESTORE: x or y no longer 0")
                    restoreWindow = null
                    if (::minRateJob.isInitialized && minRateJob.isActive)
                        minRateJob.cancel()
                    if (restoreDrawRate) {
                        restoreDrawRate = false
                        setupDrawRate(program)
                    } else
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

    private fun setupDrawRate(program: Program) {
        if (drawRate.duration > 0) {
            program.window.presentationMode = PresentationMode.MANUAL
            if (drawRate.duration != NODRAW) {
                drawRateJob = program.launch {
                    pulse(program, drawRate)
                }
            }
        }
    }

    @UseExperimental(InternalCoroutinesApi::class)
    suspend fun pulse(program: Program, dc: DrawConfig) {
        while (isActive) {
            delay(dc.duration)
            println("request draw() after ${dc.duration}L")
            program.window.requestDraw()
        }
    }

}
