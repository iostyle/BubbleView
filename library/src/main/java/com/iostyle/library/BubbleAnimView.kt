package com.iostyle.library

import android.content.Context
import android.graphics.*
import android.graphics.PathMeasure.POSITION_MATRIX_FLAG
import android.util.AttributeSet
import android.view.View
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

public class BubbleAnimView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private var bubbleList: ArrayList<Bubble> = ArrayList()
    private val scheduleStep = 0.01f
    private val scalePercent = 0.5f
    private val minScale = 0.15f
    private val alphaPercent = 0.8f

    class Bubble(var pathMeasure: PathMeasure, var bitmap: Bitmap?) {
        var schedule = 0f
    }

    @Synchronized
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var hasNext = false
        synchronized(bubbleList) {
            for (bubble in bubbleList) {
                if (drawItem(canvas, bubble)) hasNext = true
            }
        }
        if (hasNext) invalidate()
        else {
            synchronized(bubbleList) {
                if (bubbleList.isNotEmpty()) {
                    bubbleList.clear()
                }
            }
        }
    }

    @Synchronized
    fun startAnim(bitmap: Bitmap, size: Int) {
        var repeat = size
        GlobalScope.launch {
            while (repeat > 0) {
                addItem(bitmap)
                if (repeat == size) {
                    postInvalidate()
                }
                delay((10L..150L).random())
                repeat--
            }
        }
    }

    @Synchronized
    private fun addItem(bitmap: Bitmap) {
        val path = Path()
        val direction = Random().nextBoolean()
        path.moveTo((width / 2f), height.toFloat())
        path.cubicTo(randomX(direction), height / 4f * 3f, randomX(!direction), height / 2f, randomX(null), 0f)
        val pathMeasure = PathMeasure(path, false)
        synchronized(bubbleList) {
            bubbleList.add(Bubble(pathMeasure, bitmap))
        }
    }

    private fun drawItem(canvas: Canvas?, bubble: Bubble?): Boolean {
        bubble?.run {
            if (bubble.bitmap != null && schedule < 1) {
                var martrix = Matrix()
                pathMeasure.getMatrix(pathMeasure.length * schedule, martrix, POSITION_MATRIX_FLAG)
                if ((schedule + minScale) < scalePercent) {
                    val scale = (schedule + minScale) / scalePercent
                    martrix.preScale(scale, scale)
                }
                if (schedule > alphaPercent) {
                    val alpha = ((1 - ((schedule - alphaPercent) / (1 - alphaPercent))) * 255).toInt()
                    paint.alpha = alpha
                } else {
                    paint.alpha = 255
                }
                martrix.preTranslate(-bubble.bitmap!!.width / 2f, -bubble.bitmap!!.height / 2f)
                canvas?.drawBitmap(bubble.bitmap, martrix, paint)

                schedule += scheduleStep
                return true
            } else {
                bubble.bitmap = null
            }
        }
        return false
    }

    private fun randomX(direction: Boolean?): Float {
        return when (direction) {
            null -> {
                ((width / 4)..(width / 4 * 3)).random().toFloat()
            }
            true -> {
                (-(width / 4)..(width / 4)).random().toFloat()
            }
            false -> {
                ((width / 4 * 3)..width / 4 * 5).random().toFloat()
            }
        }
    }
}