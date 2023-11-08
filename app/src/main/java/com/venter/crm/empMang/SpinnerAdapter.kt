import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class StateVO(var title: String, var isSelected: Boolean)

class MyAdapter(context: Context, private val listState: List<StateVO>) : ArrayAdapter<StateVO>(context, 0, listState) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var isFromView = false

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        var holder: ViewHolder
        var view = convertView



        return view!!
    }

    private class ViewHolder {
        lateinit var mTextView: TextView
        lateinit var mCheckBox: CheckBox
    }
}
