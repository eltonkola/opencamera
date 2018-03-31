package com.aldroid.muzikashqipx.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.aldroid.coinhivesdk.model.IpCamera
import com.aldroid.opencamera.R
import java.text.NumberFormat
import java.util.*


class IpcameraAdapter(val mData: List<IpCamera>,
                        val onClick: IpcameraAdapter.OnClick) : RecyclerView.Adapter<IpcameraAdapter.ViewHolder>() {

    interface OnClick {
        fun onClick(element: IpCamera)
        fun onDelete(elemen: IpCamera)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, i: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.row_ipcamera, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData[position]
        holder.bind(item, onClick)
    }

    override fun getItemCount(): Int {
        if (mData == null) {
            return 0
        }
        return mData.size
    }

    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v) {

        var image: ImageView
        var title: TextView
        var description: TextView
        var but_delete : Button
        var view_container: ViewGroup

        init {
            image = v.findViewById(R.id.image)
            title = v.findViewById(R.id.title)
            description = v.findViewById(R.id.description)
            but_delete = v.findViewById(R.id.but_delete)
            view_container = v.findViewById(R.id.view_container)
        }

        fun bind(camera: IpCamera, eventz: IpcameraAdapter.OnClick) {

            title.text = camera.ip
            description.text = "TODO"

//            Picasso.with(iconCoin.context)
//                    .load(userCoins.logo_url)
//                    .into(iconCoin)

            view_container.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    eventz.onClick(camera)
                }
            })

            but_delete.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    eventz.onDelete(camera)
                }
            })

        }

    }

}
