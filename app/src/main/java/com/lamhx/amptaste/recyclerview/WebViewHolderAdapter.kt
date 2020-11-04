package com.lamhx.amptaste.recyclerview

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.lamhx.amptaste.entity.DummyData
import com.lamhx.amptaste.network.WebResourcesManager
import com.lamhx.amptaste.webmodule.WebViewBase

class WebViewHolderAdapter : RecyclerView.Adapter<WebViewHolderAdapter.WebViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebViewHolder {
        val container: ConstraintLayout = ConstraintLayout(parent.context)
        container.layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        return WebViewHolder(container)
    }

    override fun getItemCount(): Int {
        return DummyData.dummyData.size
    }

    override fun onBindViewHolder(holder: WebViewHolder, position: Int) {
        holder.bindingUrl(DummyData.dummyData[position])
    }

    override fun onViewRecycled(holder: WebViewHolder) {
        holder.reset()
        super.onViewRecycled(holder)
    }

    class WebViewHolder(private val container: ConstraintLayout) : RecyclerView.ViewHolder(container) {
        private var webView: WebViewBase = WebViewBase(container.context)
        init {
            webView.setResourceRequester(WebResourcesManager.getInstance())
            val lp = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            container.addView(webView, lp)
        }
        fun bindingUrl(url: String) {
            webView.loadUrl(url)
        }
        fun finish() {
            webView.startDestroy()
        }

        fun reset() {
            webView.clearCache(true);
            webView.clearHistory();
            webView.loadUrl("about:blank")
        }

    }
}