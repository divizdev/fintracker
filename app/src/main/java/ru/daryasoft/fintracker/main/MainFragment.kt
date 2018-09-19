package ru.daryasoft.fintracker.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*
import ru.daryasoft.fintracker.R

class MainFragment : Fragment() {

    private var tabPosition = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabLayout()
        activity?.title = getString(R.string.app_name)
    }

    private fun initTabLayout() {
        val adapter = MainFragmentPagerAdapter(activity, childFragmentManager)
        view_pager.adapter = adapter
        view_pager.currentItem = tabPosition

        sliding_tabs.setupWithViewPager(view_pager)
    }

    companion object {
        @JvmStatic
        fun newInstance(tabPosition: Int = 0) : MainFragment {
            val mainFragment = MainFragment()
            mainFragment.tabPosition = tabPosition
            return mainFragment
        }
    }
}
