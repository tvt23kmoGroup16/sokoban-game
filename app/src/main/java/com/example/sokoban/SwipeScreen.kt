package com.example.sokoban

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

class SwipeScreen(
    context: Context,
    private val swipeCallback: SwipeCallback
) : View.OnTouchListener {

    private val gestureDetector: GestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

        // Handle the swipe gesture
        override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            val diffX = e2?.x?.minus(e1?.x ?: 0f) ?: 0f
            val diffY = e2?.y?.minus(e1?.y ?: 0f) ?: 0f

            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (diffX > 0) {
                    swipeCallback.onSwipeRight() // Swipe Right
                } else {
                    swipeCallback.onSwipeLeft() // Swipe Left
                }
            } else {
                if (diffY > 0) {
                    swipeCallback.onSwipeDown() // Swipe Down
                } else {
                    swipeCallback.onSwipeUp() // Swipe Up
                }
            }
            return true
        }

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
    })

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        // Ensure event is not null before passing it to gestureDetector
        event?.let {
            return gestureDetector.onTouchEvent(it)  // Pass non-null event to gestureDetector
        }
        return false  // Return false if event is null
    }


    interface SwipeCallback {
        fun onSwipeUp()
        fun onSwipeDown()
        fun onSwipeLeft()
        fun onSwipeRight()
    }
}
