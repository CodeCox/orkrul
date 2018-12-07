import kotlinx.coroutines.*
import kotlinx.coroutines.NonCancellable.isActive
import org.openrndr.*
import org.openrndr.math.Vector2

/*
 * Copyright (C) 2018.  CodeCox@NousMous - All Rights Reserved.
 */

// special cases of delay duration
const val MINDELAY = 1L   //can it be 0?
const val MAXDELAY = Long.MAX_VALUE

data class DrawConfig(var id: String, var isEnable: Boolean = false, var duration: Long = MINDELAY)

class DrawRate : Extension {
    override var enabled = true

    @Volatile
    var drawRate = DrawConfig("DrawRate")
    @Volatile
    var minimiseRate = DrawConfig("MinimiseRate")

    private lateinit var drawRateJob: Job
    private lateinit var minRateJob: Job
    private var restoreWindow: Vector2? = null
    private var restoreDrawRate = false


    override fun setup(program: Program) {
        // 1.a setup DEFAULT case DRAWRATE
        if (drawRate.isEnable)
            drawRateJob = program.launch {
                pulse(program, drawRate)
            }

        // 1.b setup ON/OFF toggle for DRAWRATE
        program.keyboard.keyDown.listen {
            if (it.key == KEY_SPACEBAR && KeyboardModifier.CTRL in it.modifiers) {
                if (drawRate.isEnable) {
                    if (::drawRateJob.isInitialized && drawRateJob.isActive)
                        runBlocking {
                            drawRateJob.cancelAndJoin()
                        }
                } else
                    drawRateJob = program.launch {
                        pulse(program, drawRate)
                    }
                drawRate.isEnable = !drawRate.isEnable
            }
        }

        // 2. setup special case #1 xor y is 0 e.g. MINIMISE, SIZE events
        program.window.sized.listen {
            if (it.size.x == 0.0 || it.size.y == 0.0) {
                println("Window SIZE: width or height is 0")
                if (minimiseRate.isEnable) {
                    restoreWindow = it.size
                    if (::drawRateJob.isInitialized && drawRateJob.isActive) {
                        runBlocking {
                            drawRateJob.cancelAndJoin()
                        }
                        restoreDrawRate = true
                    }
                    minRateJob = program.launch {
                        pulse(program, minimiseRate)
                    }
                }
            } else if (restoreWindow?.x == 0.0 || restoreWindow?.y == 0.0) {
                println("Window RESTORE: x or y no longer 0")
                restoreWindow = null
                if (::minRateJob.isInitialized && minRateJob.isActive)
                    runBlocking {
                        minRateJob.cancelAndJoin()
                    }
                if (restoreDrawRate) {
                    restoreDrawRate = false
                    drawRateJob = program.launch {
                        pulse(program, drawRate)
                    }
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

    @UseExperimental(InternalCoroutinesApi::class)
    suspend fun pulse(program: Program, dc: DrawConfig) {
        program.window.presentationMode = PresentationMode.MANUAL
        try {
            while (isActive) {
                delay(dc.duration)   /// delay or request first
                println("request draw() $dc")
                program.window.requestDraw()
            }
        } finally {
            program.window.presentationMode = PresentationMode.AUTOMATIC
        }
    }
}
