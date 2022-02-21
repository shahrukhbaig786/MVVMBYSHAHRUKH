package com.example.mvvmbyshahrukh.ui.dashboard.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.mvvmbyshahrukh.R
import com.example.mvvmbyshahrukh.ui.model.MaterialMaster

/**
 * Created by shahrukh.baig on 12/2/22.
 */

class DashBoardListAdapter(mContext: Context?, data: List<MaterialMaster>) :
    BaseQuickAdapter<MaterialMaster, BaseViewHolder>(
        R.layout.raw_list, data
    ) {
    override fun convert(helper: BaseViewHolder, model: MaterialMaster?) {
        helper.setText(R.id.title, model!!.materialName)
    }
}