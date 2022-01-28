package com.example.firebasestorage


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class CustomAdapter(var context: Context, var data:ArrayList<Upload>):BaseAdapter() {
    private class ViewHolder(row:View?){
        var mTxtName:TextView
        var mImgProducImage:ImageView
        init {
            this.mTxtName = row?.findViewById(R.id.mTxTname) as TextView
            this.mImgProducImage = row?.findViewById(R.id.mImgProduct) as ImageView
        }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view:View?
        var viewHolder:ViewHolder
        if (convertView == null){
            var layout = LayoutInflater.from(context)
            view = layout.inflate(R.layout.item_layout,parent,false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }else{
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        var item:Upload = getItem(position) as Upload
        viewHolder.mTxtName.text = item.name
        Picasso.get().load(item.image_url).into(viewHolder.mImgProducImage);
        return view as View
    }

    override fun getItem(position: Int): Any {
        return  data.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.count()
    }
}