package com.lamhx.amptaste.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.lamhx.amptaste.R

class RecyclerWebViewActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_web_view)
        configureRecyclerView()
    }

    private fun configureRecyclerView() {
        recyclerView = RecyclerView(this)
        addContentView(
                recyclerView,
                ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.MATCH_PARENT
                )
        )
        // configure
        val linearLayoutManager = ZoomRecyclerLayout(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        val snapHelper = PagerSnapHelper()

        recyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            isNestedScrollingEnabled = false
            snapHelper.attachToRecyclerView(this)
            adapter = WebViewHolderAdapter()
            addItemDecoration(CirclePagerIndicatorDecoration())
        }
    }

    override fun onDestroy() {
        recyclerView?.let {
            for (child in recyclerView!!.children) {
                val holder = recyclerView!!.getChildViewHolder(child)
                if (holder is WebViewHolderAdapter.WebViewHolder) {
                    holder.finish()
                }
            }
        }

        super.onDestroy()
    }
}