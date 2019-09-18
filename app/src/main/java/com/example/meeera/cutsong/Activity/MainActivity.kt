package com.example.meeera.cutsong.Activity

import android.content.Context
import android.os.Bundle
import android.support.v4.app.*
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.meeera.cutsong.Fragment.Music
import com.example.meeera.cutsong.Fragment.Recorder
import com.example.meeera.cutsong.R
import com.google.android.material.tabs.TabLayout
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import java.util.ArrayList



class MainActivity : AppCompatActivity(), Recorder.recordingFlag {

    lateinit var viewpager : ViewPager
    lateinit var tabLayout : TabLayout
    companion object {
        var context : Context ?= null
    }
    var viewpagerAdapter : viewPagerAdapter = viewPagerAdapter(supportFragmentManager)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Fabric.with(this, Crashlytics())
        context = baseContext
        val configuration = ImageLoaderConfiguration.Builder(this).build()
        ImageLoader.getInstance().init(configuration)
        viewpager = findViewById(R.id.viewpager) as ViewPager
        tabLayout = findViewById(R.id.tabLayout) as TabLayout
        setUpViewPager(viewpager)
        tabLayout.setupWithViewPager(viewpager)
        setUpTabIcons()
    }

    private fun setUpViewPager(viewPager: ViewPager) {
        viewpagerAdapter.addFrag(Music())
        viewpagerAdapter.addFrag(Recorder(this))
        viewPager.adapter = viewpagerAdapter
    }

    private fun setUpTabIcons() {
            tabLayout.setupWithViewPager(viewpager)
            var count = tabLayout.tabCount-1
            for (i in 0..count) {
                val tab = tabLayout.getTabAt(i)
                tab?.setCustomView(viewpagerAdapter.getTabView(i))
            }
            tabLayout.getTabAt(1)!!.customView!!.isSelected = true
    }

    override fun recording(flag: Boolean) {
        viewpager.addOnPageChangeListener(object  : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if(position == 0) {
                    if(flag) {
                        (viewpagerAdapter.getItem(1) as Recorder).stopRecording()
                        Log.d("called", "called")
                    }
                }
            }

        })
    }

    class viewPagerAdapter(fm : FragmentManager) : FragmentStatePagerAdapter(fm) {

        private val mFragmentList = ArrayList<Fragment>()
        private val mTabsTitle = arrayOf("Music", "Recoder")
        private val mTabsIcons = intArrayOf(R.drawable.ic_music, R.drawable.ic_recorder)

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun getTabView(position: Int) : View {
            var view  : View = LayoutInflater.from(context).inflate(R.layout.custom_tab_text, null)
            val title = view.findViewById<TextView>(R.id.titletab)
            title.text = mTabsTitle[position]
            val icon = view.findViewById<ImageView>(R.id.icon)
            icon.setImageResource(mTabsIcons[position])
            return view
        }

        fun addFrag(fragment: Fragment) {
            mFragmentList.add(fragment)
        }
    }
}
