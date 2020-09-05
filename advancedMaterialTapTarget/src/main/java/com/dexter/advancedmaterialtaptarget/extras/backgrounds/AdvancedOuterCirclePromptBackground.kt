package com.dexter.advancedmaterialtaptarget.extras.backgrounds

import android.graphics.*
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import com.dexter.advancedmaterialtaptarget.extras.PromptBackground
import com.dexter.advancedmaterialtaptarget.extras.PromptOptions
import com.dexter.advancedmaterialtaptarget.extras.PromptUtils

/**
 * [PromptBackground] implementation that renders the prompt background as a circle.
 */
class AdvancedOuterCirclePromptBackground(radius: Float) : PromptBackground() {
    /**
     * The current circle centre position.
     */
    var mPosition: PointF

    /**
     * The current radius for the circle.
     */
    var mRadius: Float

    /**
     * The position for circle centre at 1.0 scale.
     */
    var mBasePosition: PointF

    /**
     * The radius for the circle at 1.0 scale.
     */
    var mBaseRadius = 0f

    /**
     * The paint to use to render the circle.
     */
    var mPaint: Paint

    /**
     * The alpha value to use at 1.0 scale.
     */
    @IntRange(from = 0, to = 255)
    var mBaseColourAlpha = 0
    override fun setColour(@ColorInt colour: Int) {
        mPaint.color = colour
        mBaseColourAlpha = Color.alpha(colour)
        mPaint.alpha = mBaseColourAlpha
    }

    override fun prepare(
        options: PromptOptions<*>, clipToBounds: Boolean,
        clipBounds: Rect
    ) {
        // Obtain values from the prompt options.
        val promptText = options.promptText
        val focalBounds = options.promptFocal.bounds
        val focalCentreX = focalBounds.centerX()
        val focalCentreY = focalBounds.centerY()
        val focalPadding = options.focalPadding
        val textBounds = promptText.bounds
        val textPadding = options.textPadding
        val clipBoundsInset88dp = RectF(clipBounds)
        // Default material design offset prompt when more than 88dp inset
        val inset88dp = 88f * options.resourceFinder.resources.displayMetrics.density
        clipBoundsInset88dp.inset(inset88dp, inset88dp)

        // Is the focal centre more than 88dp from the clip bounds edge
        if ((focalCentreX > clipBoundsInset88dp.left
                    && focalCentreX < clipBoundsInset88dp.right)
            || (focalCentreY > clipBoundsInset88dp.top
                    && focalCentreY < clipBoundsInset88dp.bottom)
        ) {
            // The circle position and radius is calculated based on three points placed around the
            // prompt: XY1, XY2 and XY3.
            // XY2: the text left side
            // XY3: the text right side
            // XY1: the furthest point on the focal target from the text centre x point

            // XY1
            val textWidth = textBounds.width()
            // Calculate the X distance from the text centre x to focal centre x
            val distanceX = focalCentreX - textBounds.left + textWidth / 2
            // Calculate how much percentage wise the focal centre x is from the text centre x to
            // the nearest text edge
            val percentageOffset = 100 / textWidth * distanceX
            // Angle is the above percentage of 90 degrees
            var angle = 90 * (percentageOffset / 100)
            // 0 degrees is right side middle
            // If text above target
            angle = if (textBounds.top < focalBounds.top) {
                180 - angle
            } else {
                180 + angle
            }
            val furthestPoint = options.promptFocal.calculateAngleEdgePoint(
                angle,
                focalPadding
            )
            val x1 = furthestPoint.x
            val y1 = furthestPoint.y

            // XY2
            val x2 = textBounds.left - textPadding
            val y2: Float
            // If text is above the target
            y2 = if (textBounds.top < focalBounds.top) {
                textBounds.top
            } else {
                textBounds.bottom
            }

            // XY3
            var x3 = textBounds.right + textPadding

            // If the focal width is greater than the text width
            if (focalBounds.right > x3) {
                x3 = focalBounds.right + focalPadding
            }

            // Calculate the position and radius
            val offset = Math.pow(x2.toDouble(), 2.0) + Math.pow(y2.toDouble(), 2.0)
            val bc =
                (Math.pow(x1.toDouble(), 2.0) + Math.pow(y1.toDouble(), 2.0) - offset) / 2.0
            val cd =
                (offset - Math.pow(x3.toDouble(), 2.0) - Math.pow(y2.toDouble(), 2.0)) / 2.0
            val det = (x1 - x2) * (y2 - y2) - (x2 - x3) * (y1 - y2).toDouble()
            val idet = 1 / det
            mBasePosition[((bc * (y2 - y2) - cd * (y1 - y2)) * idet).toFloat()] =
                ((cd * (x1 - x2) - bc * (x2 - x3)) * idet).toFloat()
            mBaseRadius = Math.sqrt(
                Math.pow(x2 - mBasePosition.x.toDouble(), 2.0)
                        + Math.pow(y2 - mBasePosition.y.toDouble(), 2.0)
            ).toFloat()
        } else {
            mBasePosition[focalCentreX] = focalCentreY
            // Calculate the furthest distance from the center based on the text size.
            val length = Math.max(
                Math.abs(textBounds.right - focalCentreX),
                Math.abs(textBounds.left - focalCentreX)
            ) + textPadding
            // Calculate the height based on the distance from the focal centre to the furthest text y position.
            val height = focalBounds.height() / 2 + focalPadding + textBounds.height()
            // Calculate the radius based on the calculated width and height
            mBaseRadius = Math.sqrt(
                Math.pow(length.toDouble(), 2.0) + Math.pow(
                    height.toDouble(),
                    2.0
                )
            ).toFloat()
        }
        mPosition.set(mBasePosition)
    }

    override fun update(
        options: PromptOptions<*>,
        revealModifier: Float,
        alphaModifier: Float
    ) {
        val focalBounds = options.promptFocal.bounds
        val focalCentreX = focalBounds.centerX()
        val focalCentreY = focalBounds.centerY()
        //        mRadius = mBaseRadius * revealModifier;
        mPaint.alpha = (mBaseColourAlpha * alphaModifier).toInt()
        // Change the current centre position to be a position scaled from the focal to the base.
        mPosition[focalCentreX + (mBasePosition.x - focalCentreX) * revealModifier] =
            focalCentreY + (mBasePosition.y - focalCentreY) * revealModifier
    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(mPosition.x, mPosition.y, mRadius, mPaint)
    }

    override fun contains(x: Float, y: Float): Boolean {
        return PromptUtils.isPointInCircle(x, y, mPosition, mRadius)
    }

    /**
     * Constructor.
     */
    init {
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPosition = PointF()
        mBasePosition = PointF()
        mRadius = radius
    }
}