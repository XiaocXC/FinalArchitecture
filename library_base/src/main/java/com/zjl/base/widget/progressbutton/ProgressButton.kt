package com.zjl.base.widget.progressbutton

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.core.content.res.use
import com.zjl.base.widget.progressbutton.ProgressButton.Config.Companion.GRAVITY_TEXT_CENTER
import com.zjl.base.widget.progressbutton.ProgressButton.Config.Companion.GRAVITY_TEXT_END
import com.zjl.base.widget.progressbutton.ProgressButton.Config.Companion.GRAVITY_TEXT_START
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable
import com.zjl.lib_base.R

/**
 * @author Xiaoc
 * @since 2021/5/22
 *
 * 带加载条的按钮
 */
class ProgressButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.style.Widget_MaterialComponents_Button
) : MaterialButton(context, attrs, defStyleAttr) {

    private var isLoading = false
    private val drawable: IndeterminateDrawable<CircularProgressIndicatorSpec>

    private val config = Config()

    private var cacheText = ""

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressButton)
        ta.use {
            config.progressColor = it.getColor(R.styleable.ProgressButton_progressColor,Color.WHITE)
            config.progressRadius = it.getDimensionPixelSize(R.styleable.ProgressButton_progressRadius,0)
            // 获取默认Drawable与文字间的Padding
            config.progressPadding = it.getDimensionPixelSize(R.styleable.ProgressButton_progressPadding,compoundDrawablePadding)
            config.progressGravity = it.getInt(R.styleable.ProgressButton_progressGravity,
                GRAVITY_TEXT_CENTER)
        }
        drawable = generateProgressDrawable()
    }

    /**
     * 设置是否显示加载状态
     */
    fun setLoading(loading: Boolean){
        if(isLoading == loading){
            return
        }
        if(loading){
            showDrawable()
        } else {
            hideDrawable()
        }
        isLoading = loading
    }

    /**
     * 得到当前是否处于加载状态
     */
    fun getLoading(): Boolean = isLoading

    /**
     * 设置进度按钮点击事件
     * 会自动处理是否处于加载状态，如果不处于加载状态才响应点击事件
     */
    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener {
            if(!isLoading){
                l?.onClick(it)
            }
        }
    }

    /**
     * 设置进度按钮长按点击事件
     * 会自动处理是否处于加载状态，如果不处于加载状态才响应长按点击事件
     */
    override fun setOnLongClickListener(l: OnLongClickListener?) {
        super.setOnLongClickListener(OnLongClickListener {
            if(!isLoading){
                l?.onLongClick(it)
            }
            return@OnLongClickListener true
        })
    }

    private fun generateProgressDrawable(): IndeterminateDrawable<CircularProgressIndicatorSpec>{
        val spec = CircularProgressIndicatorSpec(context,null,0,
            R.style.Widget_MaterialComponents_CircularProgressIndicator_ExtraSmall).apply {
            indicatorColors = intArrayOf(config.progressColor)
            trackCornerRadius = config.progressRadius
        }
        return IndeterminateDrawable.createCircularDrawable(context,spec).apply {
            val size = spec.indicatorSize + spec.trackThickness / 2
            setBounds(0,0, size, size)
        }
    }

    private fun showDrawable(){
        when(config.progressGravity){
            GRAVITY_TEXT_END ->{
                setCompoundDrawables(null,null,drawable,null)
                compoundDrawablePadding = config.progressPadding
            }
            GRAVITY_TEXT_CENTER ->{
                setCompoundDrawables(null,drawable,null,null)
                compoundDrawablePadding = -drawable.bounds.bottom
                cacheText = text.toString()
                text = ""
            }
            GRAVITY_TEXT_START ->{
                setCompoundDrawables(drawable,null,null,null)
                compoundDrawablePadding = config.progressPadding
            }
        }
        drawable.setVisible(true,false)
    }

    private fun hideDrawable(){
        setCompoundDrawables(null,null,null,null)
        drawable.setVisible(false,false,false)

        if(config.progressGravity == GRAVITY_TEXT_CENTER){
            text = cacheText
        }
    }

    data class Config(
        /**
         * 加载条圆角
         */
        var progressRadius: Int = 0,

        /**
         * 加载条颜色
         */
        var progressColor: Int = Color.WHITE,

        /**
         * 加载条位置
         */
        var progressGravity: Int = GRAVITY_TEXT_CENTER,

        /**
         * 加载条与文字的Padding
         */
        var progressPadding: Int = 20

    ){
        companion object{
            const val GRAVITY_TEXT_END = 1
            const val GRAVITY_TEXT_CENTER = 2
            const val GRAVITY_TEXT_START = 3
        }
    }

}