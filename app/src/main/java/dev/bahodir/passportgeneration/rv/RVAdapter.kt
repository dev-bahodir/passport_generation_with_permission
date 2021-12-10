package dev.bahodir.passportgeneration.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.bahodir.passportgeneration.databinding.RvLayoutBinding
import dev.bahodir.passportgeneration.user.User
import java.text.FieldPosition

class RVAdapter(var list: MutableList<User>, var listener: OnTouchMore) :  RecyclerView.Adapter<RVAdapter.VH>() {

    interface OnTouchMore {
        fun onMore(user: User, position: Int, view: View)
    }

    inner class VH(var binding: RvLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(user: User, position: Int) {
            binding.number.text = "${position + 1}."
            binding.name.text = "${user.name} ${user.surname}"
            binding.passport.text = "${user.serial_number}"

            binding.more.setOnClickListener {
                listener.onMore(user, position, it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(RvLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}