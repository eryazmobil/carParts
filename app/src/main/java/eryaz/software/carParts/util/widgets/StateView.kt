package eryaz.software.carParts.util.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import eryaz.software.carParts.R
import eryaz.software.carParts.util.extensions.getResourceId
import eryaz.software.carParts.util.extensions.isVisibleElseInVisible
import eryaz.software.carParts.util.widgets.ViewState.Companion.findById

class StateView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
    private var viewState = ViewState.CONTENT
    private var childVisibility = SvChildVisibility.GONE
    private var loadingViewId = 0
    private var errorViewId = 0
    private var emptyViewId = 0
    private var refreshLayoutId = 0
    private var ignoreReferencedIds = mutableListOf<Int>()

    private var retryViewId = 0
    private var onRetryClickListener: OnClickListener? = null

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (changed)
            layoutsCreated()
    }

    init {
        val t = context.obtainStyledAttributes(attrs, R.styleable.StateView)

        viewState = findById(t.getInt(R.styleable.StateView_sv_viewState, ViewState.CONTENT.id))
        childVisibility = SvChildVisibility.findById(
            t.getInt(
                R.styleable.StateView_sv_child_visibility,
                SvChildVisibility.INVISIBLE.id
            )
        )
        loadingViewId = t.getResourceId(R.styleable.StateView_sv_loadingViewId, R.id.svLoadingView)
        errorViewId = t.getResourceId(R.styleable.StateView_sv_errorViewId, R.id.svErrorView)
        emptyViewId = t.getResourceId(R.styleable.StateView_sv_emptyViewId, R.id.svEmptyView)
        retryViewId = t.getResourceId(R.styleable.StateView_sv_retryViewId, R.id.svRetryBtn)
//        refreshLayoutId =
//            t.getResourceId(R.styleable.StateView_sv_refreshLayoutId, R.id.refreshLayout)

        t.getString(R.styleable.StateView_sv_ignore_referenced_ids)?.let { string ->
            ignoreReferencedIds =
                string.split(",").map { context.getResourceId(it.trim()) }.toMutableList()
        }

        t.recycle()
    }

    private fun layoutsCreated() {
        initContentGroup()

        setViewState(viewState)
    }

    private fun initContentGroup() {
        for (i in 0..childCount)
            getChildAt(i)?.let {
                if (it.id != loadingViewId || it.id != errorViewId || it.id != emptyViewId) {
                    if (it.id == -1)
                        it.id = View.generateViewId()
                }

                if (it.id == retryViewId)
                    it.setOnClickListener(onRetryClickListener)
            }
    }

    fun setViewState(vs: ViewState) {
        viewState = vs

        val hasEmptyView = findViewById<View?>(emptyViewId) != null

        if (viewState == ViewState.EMPTY && !hasEmptyView)
            viewState = ViewState.CONTENT

        for (i in 0 until childCount) {
            getChildAt(i).apply {
                if (!ignoreReferencedIds.contains(this.id)) {
                    when (this.id) {
                        loadingViewId ->
                            this.isVisible = viewState == ViewState.LOADING
                        errorViewId ->
                            this.isVisible = viewState == ViewState.ERROR
                        emptyViewId ->
                            this.isVisible = viewState == ViewState.EMPTY
                        refreshLayoutId ->
                            this.isVisible =
                                viewState == ViewState.EMPTY || viewState == ViewState.CONTENT //show SwipeRefreshLayout when list is empty
                        else -> {
                            if (this.tag == null || this.isVisible)
                                this.tag = this.visibility

                            if (childVisibility == SvChildVisibility.GONE) {
                                if (viewState == ViewState.CONTENT) {
                                    this.isVisible = this.tag == View.VISIBLE
                                } else {
                                    this.isVisible = false
                                }
                            } else {
                                if (viewState == ViewState.CONTENT) {
                                    this.isVisible = this.tag == View.VISIBLE
                                } else {
                                    if (this.tag == View.VISIBLE) {
                                        this.isVisibleElseInVisible(false)
                                    } else {
                                        this.isVisible = this.tag == View.VISIBLE
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun setViewVisible(view: View, isVisible: Boolean) {
        view.tag = if (isVisible) View.VISIBLE else View.GONE

        view.isVisible = viewState == ViewState.CONTENT && isVisible
    }

    fun getViewState() = viewState

    fun setOnRetryClick(listener: OnClickListener?) {
        onRetryClickListener = listener
    }
}


enum class ViewState(val id: Int) {
    CONTENT(0),
    LOADING(1),
    ERROR(2),
    EMPTY(3);

    companion object {
        fun findById(id: Int) = values().find { it.id == id } ?: CONTENT
    }
}

enum class SvChildVisibility(val id: Int) {
    GONE(0),
    INVISIBLE(1);

    companion object {
        fun findById(id: Int) = values().find { it.id == id } ?: INVISIBLE
    }
}