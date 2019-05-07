//package com.jasoncavinder.inpen.demo.ui
//
//import android.content.Context
//import android.os.Build
//import android.util.AttributeSet
//import android.view.Gravity
//import android.view.LayoutInflater
//import android.widget.ImageView
//import android.widget.LinearLayout
//import androidx.annotation.RequiresApi
//import com.jasoncavinder.inpen.demo.R
//
//const val DEFAULT_RATING: Float = 3.5f
//const val DEFAULT_STARS: Int = 5
//const val DEFAULT_DIMEN: Int = 16
//
//class RatingView : LinearLayout {
//
//    private var _rating = DEFAULT_RATING
//
//    private var _outOf = DEFAULT_STARS
//
//    private var _dimen = DEFAULT_DIMEN
//
//    private val _starsOn: Int
//        get() = _rating.toInt()
//
//    private val _partialStarPadding: Int
//        get() = _rating.rem(1).times(_dimen).toInt()
//
//    private enum class State {ON, OFF}
//
//    var rating: Float
//        get() = _rating
//        set(rating) {
//            this._rating = when {
//                rating > _outOf -> _outOf.toFloat()
//                else -> rating
//            }
//            updateView()
//        }
//
//    var outOf: Int
//        get() = _outOf
//        set(outOf) {
//            when {
//                _rating > outOf -> _rating = _outOf.toFloat()
//            }
//        }
//
//    var dimen: String
//        get() = _dimen.toString().plus("dp")
//        set(dimen) { _dimen = dimen.filter {it.isDigit()}.toInt()}
//
//    constructor(context: Context) : super(context) {
//
//        init(context, null)
//    }
//
//    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
//
//        init(context, attrs)
//    }
//
//    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
//
//        init(context, attrs)
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
//        context,
//        attrs,
//        defStyleAttr,
//        defStyleRes
//    ) {
//
//        init(context, attrs)
//    }
//
//    private fun getStar(state: State = State.OFF): ImageView {
//
//        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//
//        val star: ImageView = inflater.inflate(R.layout.rating_star_item, this, false) as ImageView
//
//        star.id = childCount
//
//        return star
//    }
//
//    private fun init(context: Context, attrs: AttributeSet?) {
//
//        orientation = LinearLayout.HORIZONTAL
//        gravity = Gravity.START
//
//        addRatingStars()
//        updateView()
//    }
//
//    private fun addRatingStars() {
//        for (i in 0 until _outOf + 1) {
//            addView(getStar())
//        }
//    }
//
//    private fun updateView() {
//        for (i in 0 until childCount) {
//            when {
//                i < _starsOn -> (getChildAt(i) as ImageView).apply {
//                    setImageResource(android.R.drawable.btn_star_big_on)
//                    setPadding(0,0,0,0)
//                }
//                i == _starsOn -> (getChildAt(i) as ImageView).apply {
//                    setImageResource(android.R.drawable.btn_star_big_on)
//                    setPadding(0,0,0.minus(_dimen.minus(_partialStarPadding)),0)
//                }
//                i == _starsOn+1 -> (getChildAt(i) as ImageView).apply {
//                    setImageResource(android.R.drawable.btn_star_big_off)
//                    setPadding(0.minus(_partialStarPadding),0,0,0)
//                }
//                else -> (getChildAt(i) as ImageView).apply {
//                    setImageResource(android.R.drawable.btn_star_big_off)
//                    setPadding(0,0,0,0)
//                }
//            }
//        }
//    }
//}