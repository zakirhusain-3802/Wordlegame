import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import kotlin.random.Random

class FireworksView(context: Context) : View(context) {
    private val particles = mutableListOf<Particle>()
    private val paint = Paint()
    private val animatorSet = AnimatorSet()

    init {
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        particles.forEach { it.draw(canvas, paint) }
    }

    fun explode(x: Float, y: Float) {
        particles.clear()
        repeat(50) {
            particles.add(Particle(x, y))
        }

        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1500
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            addUpdateListener { animation ->
                particles.forEach { it.update(animation.animatedValue as Float) }
                invalidate()
            }
        }

        val fadeOut = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f).apply {
            duration = 1500
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
        }

        animatorSet.playTogether(animator, fadeOut)
        animatorSet.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animatorSet.cancel()
    }
}

class Particle(private val startX: Float, private val startY: Float) {
    private val endX = startX + Random.nextFloat() * 200 - 100
    private val endY = startY + Random.nextFloat() * 200 - 100
    private val color = Random.nextInt(0xFFFFFF) or 0xFF000000.toInt()
    private var currentFraction = 0f

    fun update(fraction: Float) {
        currentFraction = fraction
    }

    fun draw(canvas: Canvas, paint: Paint) {
        paint.color = color
        canvas.drawCircle(
            startX + (endX - startX) * currentFraction,
            startY + (endY - startY) * currentFraction,
            5f, paint
        )
    }
}