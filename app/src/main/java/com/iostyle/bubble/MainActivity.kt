package com.iostyle.bubble

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val images = arrayListOf(
        R.mipmap.caomei,
        R.mipmap.lizi,
        R.mipmap.huolongguo,
        R.mipmap.xigua,
        R.mipmap.shiliu,
        R.mipmap.ningmeng,
        R.mipmap.lanmei,
        R.mipmap.boluo,
        R.mipmap.chengzi,
        R.mipmap.yingtao,
        R.mipmap.putao,
        R.mipmap.xiangjiao,
        R.mipmap.mihoutao,
        R.mipmap.lizhi,
        R.mipmap.liulian,
        R.mipmap.taozi,
        R.mipmap.pingguo,
        R.mipmap.shizi,
        R.mipmap.mangguo
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        iv.setOnClickListener {
            val res = images[(0 until images.size).random()]
            val bitmap = BitmapFactory.decodeResource(resources, res, null)
            bubble.startAnim(bitmap, 1)
        }
    }
}
