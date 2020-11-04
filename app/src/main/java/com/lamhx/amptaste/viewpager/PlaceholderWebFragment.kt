package com.lamhx.amptaste.viewpager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.lamhx.amptaste.databinding.FragmentViewPagerBinding
import com.lamhx.amptaste.network.WebResourcesManager
import com.lamhx.amptaste.webmodule.WebViewBase

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderWebFragment : Fragment() {

    private lateinit var webPageViewModel: WebPageViewModel

    // hacky var
    var index: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webPageViewModel = ViewModelProvider(this).get(WebPageViewModel::class.java).apply {
            index = arguments?.getInt(ARG_SECTION_NUMBER) ?: -1
            setIndex(index)
            Log.d("AMP-Taste", "onCreate ${index}")
        }
    }

    lateinit var binding: FragmentViewPagerBinding
    lateinit var webview: WebViewBase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("AMP-Taste", "onCreateView ${index}")
        binding = FragmentViewPagerBinding.inflate(inflater)
        webview = WebViewBase(context)
        webview.setResourceRequester(WebResourcesManager.getInstance())
        val lp = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        binding.container.addView(webview, lp)
        webPageViewModel.url.observe(viewLifecycleOwner) {
            Log.d("WebBaseLoad", "startLoad: ${System.currentTimeMillis()}")
            webview.loadUrl(it)
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.d("AMP-Taste", "onDestroyView ${index}")
        webview.startDestroy()
        super.onDestroyView()
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(numSession: Int): PlaceholderWebFragment {
            Log.d("AMP-Taste", "PlaceHolderFragment create with ${numSession}")
            return PlaceholderWebFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, numSession)
                }
            }
        }
    }
}