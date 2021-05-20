package com.itspecial.simpleweatherapp.common

import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import kotlin.math.abs

class SwipeDetector(private val v: View) : OnTouchListener {
    private var minDistance = 100
    private var downX = 0f
    private var downY = 0f
    private var upX = 0f
    private var upY = 0f
    private var swipeEventListener: OnSwipeEvent? = null

    fun setOnSwipeListener(listener: OnSwipeEvent?) {
        try {
            swipeEventListener = listener
        } catch (e: ClassCastException) {

        }
    }

    fun onRightToLeftSwipe() {
        if (swipeEventListener != null) swipeEventListener!!.swipeEventDetected(
            v,
            SwipeTypeEnum.RIGHT_TO_LEFT
        )
    }

    fun onLeftToRightSwipe() {
        if (swipeEventListener != null) swipeEventListener!!.swipeEventDetected(
            v,
            SwipeTypeEnum.LEFT_TO_RIGHT
        )
    }

    fun onTopToBottomSwipe() {
        if (swipeEventListener != null) swipeEventListener!!.swipeEventDetected(
            v,
            SwipeTypeEnum.TOP_TO_BOTTOM
        )
    }

    fun onBottomToTopSwipe() {
        if (swipeEventListener != null) swipeEventListener!!.swipeEventDetected(
            v,
            SwipeTypeEnum.BOTTOM_TO_TOP
        )
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        v.performClick()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                return true
            }
            MotionEvent.ACTION_UP -> {
                upX = event.x
                upY = event.y
                val deltaX = downX - upX
                val deltaY = downY - upY

                //HORIZONTAL SCROLL
                if (abs(deltaX) > abs(deltaY)) {
                    return false
                } else {
                    //VERTICAL SCROLL
                    if (abs(deltaY) > minDistance) {
                        // top or down
                        if (deltaY < 0) {
                            onTopToBottomSwipe()
                            return true
                        }
                        if (deltaY > 0) {
                            onBottomToTopSwipe()
                            return true
                        }
                    } else {
                        //not long enough swipe...
                        return false
                    }
                }
                return true
            }
        }
        return false
    }

    interface OnSwipeEvent {
        fun swipeEventDetected(v: View?, swipeType: SwipeTypeEnum?)
    }

    fun setMinDistanceInPixels(min_distance: Int): SwipeDetector {
        this.minDistance = min_distance
        return this
    }

    enum class SwipeTypeEnum {
        RIGHT_TO_LEFT, LEFT_TO_RIGHT, TOP_TO_BOTTOM, BOTTOM_TO_TOP
    }

    init {
        v.setOnTouchListener(this)
    }
}