package uz.xushnudbek.flowersshop.adapters.recyclerview

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color.green
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uz.xushnudbek.flowersshop.R
import uz.xushnudbek.flowersshop.databinding.RecyclerViewAllOrdersItemBinding
import uz.xushnudbek.flowersshop.model.Order
import uz.xushnudbek.flowersshop.util.Constants.Companion.ORDER_CONFIRM_STATE
import uz.xushnudbek.flowersshop.util.Constants.Companion.ORDER_Delivered_STATE
import uz.xushnudbek.flowersshop.util.Constants.Companion.ORDER_PLACED_STATE
import uz.xushnudbek.flowersshop.util.Constants.Companion.ORDER_SHIPPED_STATE
import java.text.SimpleDateFormat

class AllOrdersAdapter : RecyclerView.Adapter<AllOrdersAdapter.AllOrdersAdapterViewHolder>() {

    inner class AllOrdersAdapterViewHolder(val binding: RecyclerViewAllOrdersItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrdersAdapterViewHolder {
        return AllOrdersAdapterViewHolder(
            RecyclerViewAllOrdersItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: AllOrdersAdapterViewHolder, position: Int) {
        val order = differ.currentList[position]
        holder.binding.apply {
            val date = SimpleDateFormat("yyyy-MM-dd").format(order.date)
            tvOrderId.text = holder.itemView.context.resources.getText(R.string.g_order).toString()
                .plus(" #${order.id}")
            tvOrderDate.text = date

            when(order.state){
                ORDER_PLACED_STATE -> changeOrderStateColor(imgOrderState, color = imgOrderState.context.resources.getColor(R.color.g_orange))
                ORDER_CONFIRM_STATE -> changeOrderStateColor(imgOrderState,imgOrderState.context.resources.getColor(R.color.green))
                ORDER_SHIPPED_STATE -> changeOrderStateColor(imgOrderState,imgOrderState.context.resources.getColor(R.color.green))
                ORDER_Delivered_STATE -> changeOrderStateColor(imgOrderState,imgOrderState.context.resources.getColor(R.color.g_blue))
            }

        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(order)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private fun changeOrderStateColor(imageView:ImageView,color:Int){
        imageView.imageTintList = ColorStateList.valueOf(color)
    }

    var onItemClick: ((Order) -> Unit)? = null
}