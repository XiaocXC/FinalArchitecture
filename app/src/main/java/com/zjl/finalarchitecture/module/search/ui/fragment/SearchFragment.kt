package com.zjl.finalarchitecture.module.search.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zjl.finalarchitecture.R


class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val txt : TextView = view.findViewById(R.id.txt)
        txt.text = "Listen baby\n" +
                "不用去在意\n" +
                "这到底是不是个陷阱\n" +
                "我清楚明白\n" +
                "我睁开眼睛就是想见你\n" +
                "我的爱很精致\n" +
                "绝不是烂大街的赝品\n" +
                "我对你做的一切\n" +
                "胜过所有爱的电影\n" +
                "我是你要的绝配\n" +
                "这很绝对\n" +
                "床上铺的\n" +
                "也都是你最爱的棉被\n" +
                "拉着你的手\n" +
                "不是为了让爱凑合\n" +
                "抱你一整天都可以\n" +
                "怎样才算够呢\n" +
                "我的爱很丰富\n" +
                "不会让你感到单一\n" +
                "这是独特的感觉\n" +
                "没什么能够攀比\n" +
                "我想要随时把你装进我的\n" +
                "我口袋里\n" +
                "已经数不清楚\n" +
                "说了多少遍我爱你\n" +
                "Oh babe babe babe\n" +
                "今晚不想让你回去回去\n" +
                "如果你想让我陪你陪你\n" +
                "就在此刻让我成为你的唯一"
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}